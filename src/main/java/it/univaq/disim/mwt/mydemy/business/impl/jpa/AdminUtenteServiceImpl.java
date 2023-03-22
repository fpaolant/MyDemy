package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import it.univaq.disim.mwt.mydemy.business.*;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import it.univaq.disim.mwt.mydemy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AdminUtenteServiceImpl implements AdminUtenteService {
	
	@Autowired
	private UtenteRepository utenteRepository;
	@Autowired
	private IscrizioneRepository iscrizioneRepository;
	@Autowired
	private RuoloRepository ruoloRepository;

	@Autowired
	private CorsoRepository corsoRepository;
	@Autowired
	private RecensioneRepository recensioneRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional(readOnly = true)
	public Utente findByUsername(String username) {
		return utenteRepository.findByUsername(username);
	}

	@Override
	public Optional<Utente> findByID(Long id) {
		return utenteRepository.findById(id);
	}

	@Override
	public void create(Utente utente) {
		// encode password
		final String password = passwordEncoder.encode(utente.getPassword());
		utente.setPassword(password);

		utenteRepository.save(utente);
	}


	@Override
	@Transactional
	public void update(Utente utente) throws BusinessException {
		Optional<Utente> optionalUtente = utenteRepository.findById(utente.getId());

		if(optionalUtente.isEmpty()) throw new BusinessException("Utente non trovato");

		utente.setVersion(optionalUtente.get().getVersion());

		if(!utente.getPassword().equalsIgnoreCase("")) {
			// encode password
			final String password = passwordEncoder.encode(utente.getPassword());
			// System.out.println("password inviata codificata:" + utente.getPassword());
			utente.setPassword(password);
		} else {
			utente.setPassword(optionalUtente.get().getPassword());
		}
		utenteRepository.save(utente);
	}

	@Override
	@Transactional(readOnly = true)
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
	@Transactional
	public void delete(Utente utente) throws BusinessException {
		if(canBeDeleted(utente)) {
			recensioneRepository.deleteByAutoreId(utente.getId());
			iscrizioneRepository.deleteByUtente(utente);
			utenteRepository.delete(utente);
		}
	}

	@Override
	@Transactional
	public void delete(Long utenteId) throws BusinessException {
		Optional<Utente> optUtente = utenteRepository.findById(utenteId);
		if (optUtente.isEmpty()) throw new BusinessException("Utente non trovato");

		if(canBeDeleted(optUtente.get())) {
			iscrizioneRepository.deleteByUtente(optUtente.get());
			utenteRepository.deleteById(utenteId);
		}
	}

	private boolean canBeDeleted(Utente utente) throws BusinessException {
		// check if user has role admin (cannot be deleted)
		if (utente.getRuoli().stream().anyMatch(ruolo -> { return ruolo.getCode().equalsIgnoreCase("ADMIN"); })) {
			throw new BusinessException("L'Utente possiede il ruolo amministratore e non può essere eliminato, rimuoverlo prima di eliminare");
		}
		// check if own course(s) approved
		if(corsoRepository.findAllByCreatoreOrderByFineDescApprovatoDesc(utente, Pageable.unpaged()).size()>0) {
			throw new BusinessException("L'Utente è creatore di corsi approvati e non può essere eliminato");
		}
		return true;
	}

	@Override
	@Transactional
	public void enable(Long userId) throws BusinessException {
		Optional<Utente> optUtente = utenteRepository.findById(userId);

		if(optUtente.isPresent()) {
			Utente utente = optUtente.get();
			boolean enabled = utente.getEnabled();
			utente.setEnabled(!enabled);
			utenteRepository.save(utente);
		} else {
			throw new BusinessException("utente non trovato");
		}
	}

}
