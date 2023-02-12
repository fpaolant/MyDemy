package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import it.univaq.disim.mwt.mydemy.domain.Ruolo;
import it.univaq.disim.mwt.mydemy.repository.IscrizioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.disim.mwt.mydemy.business.RequestGrid;
import it.univaq.disim.mwt.mydemy.business.ResponseGrid;
import it.univaq.disim.mwt.mydemy.business.UtenteBO;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import it.univaq.disim.mwt.mydemy.repository.UtenteRepository;


@Service
@Transactional
public class UtenteBoImpl implements UtenteBO {
	
	@Autowired UtenteRepository utenteRepository;
	@Autowired
	IscrizioneRepository iscrizioneRepository;

	@Override
	public Utente findByUsername(String username) {
		return utenteRepository.findByUsername(username);
	}

	@Override
	public Optional<Utente> findByID(Long id) {
		return utenteRepository.findById(id);
	}

	@Override
	public void save(Utente utente) {
		utenteRepository.save(utente);
	}

	@Override
	public void updateProfilo(Utente nuovoProfilo) {
		utenteRepository.save(nuovoProfilo);		
	}

	@Override
	public ResponseGrid<Utente> findAllPaginated(RequestGrid requestGrid) {
		String sortCol = requestGrid.getSortCol();
				
		List<Utente> utenti = null;
		// sort order and value
		Sort sortCriteria = Sort.by(sortCol);
		if(requestGrid.getSortDir().equalsIgnoreCase("desc")) sortCriteria.descending();
		// pageable
		PageRequest pageRequest = PageRequest.of(requestGrid.getStart(), requestGrid.getLength(), sortCriteria);
		
		
		if("".equals(requestGrid.getSearch().getValue())) {			
			utenti = utenteRepository.findAll(pageRequest).stream().collect(Collectors.toList());
		} else {
			String searchString = requestGrid.getSearch().getValue();
			utenti = utenteRepository.findByCognomeContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrNomeContainingIgnoreCase(searchString, searchString, searchString, searchString, pageRequest);
		}
		
		int numTotali = utenteRepository.findAll().size();
		
		return new ResponseGrid<Utente>(requestGrid.getDraw(), numTotali, utenti.size(), utenti);
	}

	@Override
	public void delete(Utente utente) {
		iscrizioneRepository.deleteByUtente(utente);
		utenteRepository.delete(utente);
	}

	@Override
	public void changePassword(Utente utente, String password) {
		utente.setPassword(password);
		utenteRepository.save(utente);		
	}

	@Override
	public List<Utente> findAllByRole(Ruolo ruolo) {
		return utenteRepository.findByRuoli_(ruolo);
	}

	@Override
	public Long count() {
		return utenteRepository.count();
	}
}
