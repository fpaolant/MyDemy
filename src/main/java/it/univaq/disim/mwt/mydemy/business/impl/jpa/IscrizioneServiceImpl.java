package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.business.BusinessException;
import it.univaq.disim.mwt.mydemy.business.RequestGrid;
import it.univaq.disim.mwt.mydemy.business.ResponseGrid;
import it.univaq.disim.mwt.mydemy.repository.CorsoRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.apache.pdfbox.util.Matrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.disim.mwt.mydemy.business.IscrizioneService;
import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.domain.Iscrizione;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import it.univaq.disim.mwt.mydemy.repository.IscrizioneRepository;


@Service
@Transactional
public class IscrizioneServiceImpl implements IscrizioneService {
	@Autowired IscrizioneRepository iscrizioneRepository;
	@Autowired
	private CorsoRepository corsoRepository;


	@Override
	public Optional<Iscrizione> findById(Long id) {
		return iscrizioneRepository.findById(id);
	}

	@Override
	public Optional<Iscrizione> findByUtenteAndCorso(Utente utente, Corso corso) {
		return iscrizioneRepository.findByUtenteAndCorso(utente, corso);
	}

	@Override
	public List<Iscrizione> findByUtente(Utente utente) {
		return iscrizioneRepository.findByUtenteOrderByDataAsc(utente);
	}


	@Override
	@Transactional(readOnly = true)
	public ResponseGrid<Iscrizione> findAllByCorsoCreatorePaginated(Long corsoId, Utente creatore, RequestGrid requestGrid) throws BusinessException {
		Optional<Corso> optCorso = corsoRepository.findByIdAndCreatore(corsoId, creatore);
		if(optCorso.isEmpty()) {
			throw new BusinessException("Corso non trovato");
		}

		String sortCol = requestGrid.getSortCol();

		List<Iscrizione> iscrizioni = null;
		// sort order and value
		Sort sortCriteria = Sort.by(sortCol);
		if(requestGrid.getSortDir().equalsIgnoreCase("desc")) sortCriteria.descending();
		// pageable
		PageRequest pageRequest = PageRequest.of(requestGrid.getStart(), requestGrid.getLength(), sortCriteria);

		if("".equals(requestGrid.getSearch().getValue())) {
			iscrizioni = iscrizioneRepository.findAllByCorsoOrderByUtenteCognomeAsc(optCorso.get(), pageRequest); //.findAll(pageRequest);
		} else {
			//corsi = corsoRepository.findAllPaginatedWithSearchValue(ConversionUtility.addPercentSuffix(requestGrid.getSearch().getValue()), pageRequest);
			iscrizioni = iscrizioneRepository.findAllByCorsoAndUtenteCognomeContainingIgnoreCaseOrderByUtenteCognomeAsc(optCorso.get(), requestGrid.getSearch().getValue(), pageRequest);
		}
		int numTotali = iscrizioneRepository.findAllByCorsoOrderByUtenteCognomeAsc(optCorso.get(), null).size();


		return new ResponseGrid<Iscrizione>(requestGrid.getDraw(), numTotali, iscrizioni.size(), iscrizioni);
	}

	@Override
	public void create(Iscrizione iscrizione) {
		iscrizioneRepository.save(iscrizione);
	}
	@Override
	public void update(Iscrizione iscrizione) {
		iscrizioneRepository.save(iscrizione);
	}

	@Override
	public void delete(Iscrizione iscrizione) {
		iscrizioneRepository.delete(iscrizione);
	}

	@Override
	@Transactional(readOnly = true)
	public float getPercentualeSuperatoTotale() {
		Long iscrittiTotali = iscrizioneRepository.count();
		Long iscrittiSuperato = iscrizioneRepository.countBySuperatoIsTrue();
		return (iscrittiSuperato*100 / iscrittiTotali);
	}

	/*@Override
	@Transactional(readOnly = true)
	public float getPercentualeSuperato(Corso corso) {
		Long iscrittiTotali = iscrizioneRepository.countByCorso(corso);
		Long iscrittiSuperato = iscrizioneRepository.countBySuperatoIsTrueAndCorsoIs(corso);
		return (iscrittiSuperato*100 / iscrittiTotali);
	}*/

	@Override
	@Transactional(readOnly = true)
	public float getPercentualeSuperato(Utente creatore) {
		Long iscrittiTotali = iscrizioneRepository.countByCorsoCreatoreAndCorsoApprovatoIsTrue(creatore);
		Long iscrittiSuperato = iscrizioneRepository.countBySuperatoIsTrueAndCorsoCreatoreIs(creatore);
		if(iscrittiTotali>0) return (iscrittiSuperato*100 / iscrittiTotali);
		else return 0;
	}

	@Override
	@Transactional(readOnly = true)
	public Long count() {
		return iscrizioneRepository.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Long count(Corso corso) { return  iscrizioneRepository.countByCorso(corso); }

	@Override
	@Transactional(readOnly = true)
	public Long count(Utente creatore) {
		return iscrizioneRepository.countByCorsoCreatore(creatore);
	}

	@Override
	public File generaCertificato(Utente utente, Long iscrizioneId) throws IOException, BusinessException {
		Optional<Iscrizione> iscrizioneOpt = iscrizioneRepository.findById(iscrizioneId);

		if(iscrizioneOpt.isEmpty() && !iscrizioneOpt.get().getSuperato() && !iscrizioneOpt.get().getUtente().equals(utente)) {
			throw new BusinessException("Non hai diritto di accedere alla risorsa");
		}

		Iscrizione iscrizione = iscrizioneOpt.get();

		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		page.setRotation(90); // landscape
		document.addPage( page );

		// Create a new font object selecting one of the PDF base fonts
		PDFont font = PDType1Font.HELVETICA_BOLD;
		PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;

		// Start a new content stream which will "hold" the to be created content
		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		PDRectangle pageSize = page.getMediaBox();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/mm/YYYY");

		// set initial
		this.addCenteredText("MYDEMY", PDType1Font.HELVETICA_BOLD, 44, contentStream, page, new Point2D.Float(0, 200));
		// set initial
		this.addCenteredText("certifica che ", PDType1Font.HELVETICA, 16, contentStream, page, new Point2D.Float(0, 150));
		// set Student name
		StringBuilder studentName = new StringBuilder(iscrizione.getUtente().getNome());
		studentName.append(" ");
		studentName.append(iscrizione.getUtente().getCognome());
		this.addCenteredText(studentName.toString(), PDType1Font.HELVETICA_BOLD_OBLIQUE, 22, contentStream, page, new Point2D.Float(0, 100));
		// set middle
		StringBuilder middle = new StringBuilder("dal ");
		middle.append(iscrizione.getCorso().getInizio().format(formatter));
		middle.append(" al ");
		middle.append(iscrizione.getCorso().getFine().format(formatter));
		middle.append(" ha seguito e superato il corso di: ");
		this.addCenteredText(middle.toString(), PDType1Font.HELVETICA, 16, contentStream, page, new Point2D.Float(0, 50));
		// set course title
		String courseName = iscrizione.getCorso().getTitolo();
		this.addCenteredText(courseName, PDType1Font.HELVETICA_BOLD, 32, contentStream, page, new Point2D.Float(0, 0));
		// set teacher
		StringBuilder teacher = new StringBuilder("tenuto dal docente ");
		teacher.append(iscrizione.getCorso().getCreatore().getNome());
		teacher.append(" ");
		teacher.append(iscrizione.getCorso().getCreatore().getCognome());
		this.addCenteredText(teacher.toString(), PDType1Font.HELVETICA_OBLIQUE, 16, contentStream, page, new Point2D.Float(0, -80));

		// set crediti
		StringBuilder crediti = new StringBuilder("il corso dà diritto all'acquisizione di n.");
		crediti.append(iscrizione.getCorso().getCrediti());
		crediti.append(" crediti formativi.");
		this.addCenteredText(crediti.toString(), PDType1Font.HELVETICA_OBLIQUE, 14, contentStream, page, new Point2D.Float(0, -100));

		// Make sure that the content stream is closed:
		contentStream.close();



		// PROTECT DOCUMENT
		// Define the length of the encryption key.
		// Possible values are 40, 128 or 256.
		int keyLength = 256;

		AccessPermission ap = new AccessPermission();
		ap.setCanPrint(true);
		ap.setCanExtractContent(false);
		ap.setCanModify(false);

		// Owner password (to open the file with all permissions) is "12345"
		// User password (to open the file but with restricted permissions, is empty here)
		StandardProtectionPolicy spp = new StandardProtectionPolicy("12345", "", ap);
		spp.setEncryptionKeyLength(keyLength);

		//Apply protection
		document.protect(spp);
		String tmpdir = System.getProperty("java.io.tmpdir");
		File f = new File(tmpdir + "certificato_" + courseName + "_" + studentName + ".pdf"  );

		// Save the results and ensure that the document is properly closed:
		document.save(f);
		document.close();

		return f;
	}

	private void addCenteredText(String text, PDFont font, int fontSize, PDPageContentStream content, PDPage page, Point2D.Float offset) throws IOException {
		content.setFont(font, fontSize);
		content.beginText();


		PDRectangle pageSize = page.getMediaBox();
		float pageWidth = pageSize.getHeight();
		float pageHeight = pageSize.getWidth();

		Point2D.Float pageCenter = new Point2D.Float(pageWidth / 2F, pageHeight / 2F);

		// We use the text's width to place it at the center of the page
		float stringWidth = font.getStringWidth(text) * fontSize / 1000F;
		float textX = pageCenter.x - stringWidth / 2F + offset.x;
		float textY = pageCenter.y - offset.y;
		// Swap X and Y due to the rotation
		content.setTextMatrix(Matrix.getRotateInstance(Math.PI / 2, textY, textX));
		content.showText(text);
		content.endText();
	}
}
