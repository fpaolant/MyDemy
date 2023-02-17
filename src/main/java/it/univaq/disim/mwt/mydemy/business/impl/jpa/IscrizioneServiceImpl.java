package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.business.BusinessException;
import it.univaq.disim.mwt.mydemy.business.RequestGrid;
import it.univaq.disim.mwt.mydemy.business.ResponseGrid;
import it.univaq.disim.mwt.mydemy.repository.CorsoRepository;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
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

import javax.xml.bind.JAXBException;


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
	public List<Iscrizione> findByUtenteAndCorso(Utente utente, Corso corso) {
		return iscrizioneRepository.findByUtenteAndCorsoOrderByDataAsc(utente, corso);
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


	private void generaCertificato(Iscrizione iscrizione) throws Docx4JException, JAXBException {
		File templateDoc = new File("src/main/resources/templates/doc/template_certificato_udemy.docx");

		WordprocessingMLPackage template = Docx4J.load(templateDoc);

		MainDocumentPart documentPart = template.getMainDocumentPart();

		HashMap<String, String> mappings = new HashMap<>();
		mappings.put(iscrizione.getUtente().getNome() + " " + iscrizione.getUtente().getCognome(), "NOMINATIVOSTUDENTE");
		mappings.put(iscrizione.getCorso().getTitolo(), "NOMECORSO");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/mm/YYYY HH:mm");
		mappings.put(iscrizione.getCorso().getInizio().format(formatter), "DATAINIZIOCORSO");
		mappings.put(iscrizione.getCorso().getFine().format(formatter), "DATAFINECORSO");
		mappings.put(iscrizione.getCorso().getCreatore().getNome() + " " + iscrizione.getCorso().getCreatore().getCognome(), "NOMINATIVOPROFESSORE");

		documentPart.variableReplace(mappings);
		File exportFile = new File("certificato_" +iscrizione.getCorso().getTitolo() + "_" + iscrizione.getUtente().getNome() + "_" + iscrizione.getUtente().getCognome() + ".docx");
		template.save(exportFile);
	}
}
