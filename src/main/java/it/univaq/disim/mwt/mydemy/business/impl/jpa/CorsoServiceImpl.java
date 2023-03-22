package it.univaq.disim.mwt.mydemy.business.impl.jpa;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import it.univaq.disim.mwt.mydemy.business.*;
import it.univaq.disim.mwt.mydemy.domain.*;
import it.univaq.disim.mwt.mydemy.repository.IscrizioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.disim.mwt.mydemy.repository.CorsoRepository;


@Service
@Transactional
public class CorsoServiceImpl implements CorsoService {
	@Autowired
	CorsoRepository corsoRepository;
	@Autowired
	IscrizioneRepository iscrizioneRepository;

	@Override
	public void create(Corso corso) {
		corsoRepository.save(corso);
	}
	@Override
	@Transactional(readOnly = true)
	public List<Corso> findAllNextCorsi(PageRequest pageRequest) {
		return corsoRepository.findByApprovatoIsTrue(pageRequest);
	}
	@Override
	public Optional<Corso> findById(Long id) {
		return corsoRepository.findById(id);
	}
	@Override
	@Transactional(readOnly = true)
	public List<Corso> findByTitoloContainingIgnoreCase(String searchString, PageRequest pageRequest) {
		return corsoRepository.findByTitoloContainingIgnoreCaseAndApprovatoIsTrue(searchString, pageRequest);
	}
	@Override
	@Transactional
	public void update(Corso corso) throws BusinessException {
		Optional<Corso> optionalCorso = corsoRepository.findById(corso.getId());
		if(optionalCorso.isEmpty()) throw new BusinessException("Corso non trovato");
		Corso corsoOld = optionalCorso.get();
		corso.setVersion(corsoOld.getVersion());
		corsoRepository.save(corso);
	}
	/*@Override
	@Transactional(readOnly = true)
	public ResponseGrid<Corso> findAllApprovedPaginated(RequestGrid requestGrid) {
		String sortCol = requestGrid.getSortCol();
		
		List<Corso> corsi = null;
		// sort order and value
		Sort sortCriteria = Sort.by(sortCol);
		if(requestGrid.getSortDir().equalsIgnoreCase("desc")) sortCriteria.descending();
		// pageable
		PageRequest pageRequest = PageRequest.of(requestGrid.getStart(), requestGrid.getLength(), sortCriteria);
		
		if("".equals(requestGrid.getSearch().getValue())) {			
			corsi = corsoRepository.findByApprovatoIsTrue(pageRequest).stream().collect(Collectors.toList());
		} else {
			corsi = corsoRepository.findByTitoloContainingIgnoreCaseAndApprovatoIsTrue(requestGrid.getSearch().getValue(), pageRequest);
		}
		
		int numTotali = corsoRepository.findByApprovatoIsTrue(PageRequest.of(0,Integer.MAX_VALUE)).size();
		
		return new ResponseGrid<Corso>(requestGrid.getDraw(), numTotali, corsi.size(), corsi);
		
	}*/
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

		int numTotali = corsoRepository.findByApprovatoIsTrue(PageRequest.of(0,Integer.MAX_VALUE)).size();

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
			c.setNumIscritti(c.getIscrizioni().size());
		});
		return new ResponseGrid<Corso>(requestGrid.getDraw(), numTotali, corsi.size(), corsi);
	}

	@Override
	public List<Corso> findAllCriteria(String searchTitleString, PageRequest pageRequest) {
		return corsoRepository.findByTitoloContainingIgnoreCaseOrTagsNomeContainingIgnoreCaseOrCreatoreNomeContainingIgnoreCaseOrCreatoreCognomeContainingIgnoreCaseAndApprovatoIsTrue(searchTitleString, searchTitleString, searchTitleString, searchTitleString, pageRequest);
	}
	@Override
	public List<Corso> findAllCriteriaInCategoria(Categoria categoria, String searchTitleString,  PageRequest pageRequest) {
		return corsoRepository.findByCategorie_AndTitoloContainingIgnoreCaseOrTagsNomeContainingIgnoreCaseOrCreatoreNomeContainingIgnoreCaseOrCreatoreCognomeContainingIgnoreCaseAndApprovatoIsTrue(categoria, searchTitleString, searchTitleString, searchTitleString, searchTitleString, pageRequest);
	}
	@Override
	public void delete(Corso corso) throws BusinessException {
		if(corso.getIscrizioni().size()==0) {
			corsoRepository.delete(corso);
		} else {
			throw new BusinessException("Sono presenti iscritti, non è possibile eliminare il corso");
		}
	}

	@Override
	public void deleteById(Long id) throws BusinessException {
		Optional<Corso> optCorso = corsoRepository.findById(id);
		if(optCorso.isPresent()) {
			if (optCorso.get().getIscrizioni().size()==0) {
				corsoRepository.deleteById(id);
			} else {
				throw new BusinessException("Sono presenti iscritti, non è possibile eliminare il corso");
			}
		} else {
			throw new BusinessException("Il corso non è stato trovato");
		}
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

	@Override
	@Transactional(readOnly = true)
	public Long count() {
		return corsoRepository.count();
	}

	@Override
	public void iscrivi(Long corsoId, Utente utente) throws BusinessException {
		Optional<Corso> optCorso = corsoRepository.findById(corsoId);

		if(optCorso.isPresent()) {
			if(iscrizioneRepository.findByUtenteAndCorso(utente, optCorso.get()).isEmpty()) {
				Iscrizione i = new Iscrizione();
				i.setUtente(utente);
				i.setCorso(optCorso.get());
				iscrizioneRepository.save(i);
			} else {
				throw new BusinessException("Utente già iscritto");
			}
		} else {
			throw new BusinessException("Corso non trovato");
		}
	}
	@Override
	@Transactional
	public void proponi(Corso corso, Utente creatore) {
		corso.setCreatore(creatore);
		corso.setApprovato(false);
		corsoRepository.save(corso);
	}
	@Override
	public Iscrizione setSuperato(Long iscrizioneId) throws BusinessException {
		Optional<Iscrizione> iscrizioneOpt = iscrizioneRepository.findById(iscrizioneId);

		if(iscrizioneOpt.isPresent()) {
			Iscrizione iscrizione = iscrizioneOpt.get();
			iscrizione.setSuperato(true);
			iscrizioneRepository.save(iscrizione);
			// TODO invia certificato
			return iscrizione;// return "redirect:/creatore/iscritti?id="+iscrizione.getCorso().getId();
		} else {
			throw new BusinessException("Iscrizione non presente");
		}
	}

}
