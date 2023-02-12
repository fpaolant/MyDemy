package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.business.RequestGrid;
import it.univaq.disim.mwt.mydemy.business.ResponseGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.disim.mwt.mydemy.business.IscrizioneBO;
import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.domain.Iscrizione;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import it.univaq.disim.mwt.mydemy.repository.IscrizioneRepository;


@Service
@Transactional
public class IscrizioneBOImpl implements IscrizioneBO {
	@Autowired IscrizioneRepository iscrizioneRepository;


	@Override
	public Optional<Iscrizione> findById(Long id) {
		return iscrizioneRepository.findById(id);
	}

	@Override
	public List<Iscrizione> findByCorso(Corso corso) {
		return iscrizioneRepository.findByCorso(corso);
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
	public ResponseGrid<Iscrizione> findAllByCorsoPaginated(Corso corso, RequestGrid requestGrid) {
		String sortCol = requestGrid.getSortCol();

		List<Iscrizione> iscrizioni = null;
		// sort order and value
		Sort sortCriteria = Sort.by(sortCol);
		if(requestGrid.getSortDir().equalsIgnoreCase("desc")) sortCriteria.descending();
		// pageable
		PageRequest pageRequest = PageRequest.of(requestGrid.getStart(), requestGrid.getLength(), sortCriteria);

		if("".equals(requestGrid.getSearch().getValue())) {
			iscrizioni = iscrizioneRepository.findAllByCorsoOrderByUtenteCognomeAsc(corso, pageRequest); //.findAll(pageRequest);
		} else {
			//corsi = corsoRepository.findAllPaginatedWithSearchValue(ConversionUtility.addPercentSuffix(requestGrid.getSearch().getValue()), pageRequest);
			iscrizioni = iscrizioneRepository.findAllByCorsoAndUtenteCognomeContainingIgnoreCaseOrderByUtenteCognomeAsc(corso, requestGrid.getSearch().getValue(), pageRequest);
		}
		int numTotali = iscrizioneRepository.findAllByCorsoOrderByUtenteCognomeAsc(corso, null).size();


		return new ResponseGrid<Iscrizione>(requestGrid.getDraw(), numTotali, iscrizioni.size(), iscrizioni);
	}

	@Override
	public void save(Iscrizione iscrizione) {
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

	@Override
	@Transactional(readOnly = true)
	public float getPercentualeSuperato(Corso corso) {
		Long iscrittiTotali = iscrizioneRepository.countByCorso(corso);
		Long iscrittiSuperato = iscrizioneRepository.countBySuperatoIsTrueAndCorsoIs(corso);
		return (iscrittiSuperato*100 / iscrittiTotali);
	}

	@Override
	@Transactional(readOnly = true)
	public Long count() {
		return iscrizioneRepository.count();
	}

	@Override
	@Transactional(readOnly = true)
	public Long countByCorso(Corso corso) { return  iscrizioneRepository.countByCorso(corso); }
}
