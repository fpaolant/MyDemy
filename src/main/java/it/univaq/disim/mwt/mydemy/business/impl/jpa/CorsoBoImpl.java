package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import it.univaq.disim.mwt.mydemy.domain.Utente;
import it.univaq.disim.mwt.mydemy.repository.IscrizioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.disim.mwt.mydemy.business.CorsoBO;
import it.univaq.disim.mwt.mydemy.business.RequestGrid;
import it.univaq.disim.mwt.mydemy.business.ResponseGrid;
import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.repository.CorsoRepository;

import javax.persistence.EntityManager;

@Service
@Transactional
public class CorsoBoImpl implements CorsoBO {
	
	@Autowired CorsoRepository corsoRepository;
	@Autowired
	private IscrizioneRepository iscrizioneRepository;

//	static final Logger logger = Logger.getLogger(CorsoBoImpl.class);

	@Override
	public void save(Corso corso) {
		corsoRepository.save(corso);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Corso> findAllCorsi() {
		return (List<Corso>) corsoRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Corso> findAllNextCorsi(PageRequest pageRequest) {
		return corsoRepository.findAll(pageRequest);
	}

	@Override
	public Optional<Corso> findByID(Long id) {
		return corsoRepository.findById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Optional<Corso> findByTitolo(String titolo) {
		return corsoRepository.findByTitolo(titolo);
	}

	@Override
	public void update(Corso corso) {
		corsoRepository.save(corso);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseGrid<Corso> findAllPaginated(RequestGrid requestGrid) {
		String sortCol = requestGrid.getSortCol();
		
		List<Corso> corsi = null;
		// sort order and value
		Sort sortCriteria = Sort.by(sortCol);
		if(requestGrid.getSortDir().equalsIgnoreCase("desc")) sortCriteria.descending();
		// pageable
		PageRequest pageRequest = PageRequest.of(requestGrid.getStart(), requestGrid.getLength(), sortCriteria);
		
		
		if("".equals(requestGrid.getSearch().getValue())) {			
			corsi = corsoRepository.findAll(pageRequest).stream().collect(Collectors.toList());
		} else {
			corsi = corsoRepository.findByTitoloContainingIgnoreCase(requestGrid.getSearch().getValue(), pageRequest);
		}
		
		int numTotali = corsoRepository.findAll().size();
		
		return new ResponseGrid<Corso>(requestGrid.getDraw(), numTotali, corsi.size(), corsi);
		
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseGrid<Corso> findAllByCreatorePaginated(Utente creatore, RequestGrid requestGrid) {
		String sortCol = requestGrid.getSortCol();

		List<Corso> corsi = null;
		// sort order and value
		Sort sortCriteria = Sort.by(sortCol);
		if(requestGrid.getSortDir().equalsIgnoreCase("desc")) sortCriteria.descending();
		// pageable
		PageRequest pageRequest = PageRequest.of(requestGrid.getStart(), requestGrid.getLength(), sortCriteria);

		if("".equals(requestGrid.getSearch().getValue())) {
			corsi = corsoRepository.findAllByCreatoreOrderByFineDescApprovatoDesc(creatore, pageRequest);
		} else {
			corsi = corsoRepository.findAllByCreatoreAndTitoloContainingIgnoreCaseOrderByFineDescApprovatoDesc(creatore, requestGrid.getSearch().getValue(), pageRequest);
		}
		int numTotali = corsoRepository.findAllByCreatoreOrderByFineDescApprovatoDesc(creatore, null).size();

		corsi.forEach(c->{
			c.setNumIscritti(iscrizioneRepository.findByCorso(c).size());
		});
		return new ResponseGrid<Corso>(requestGrid.getDraw(), numTotali, corsi.size(), corsi);
	}

	@Override
	public void delete(Corso corso) {
		corsoRepository.delete(corso);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Corso> findByCreatore(Utente creatore) {
		return (List<Corso>) corsoRepository.findByCreatore(creatore);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Corso> findByIdAndCreatore(Long id, Utente creatore) {
		return corsoRepository.findByIdAndCreatore(id, creatore);
	}



}
