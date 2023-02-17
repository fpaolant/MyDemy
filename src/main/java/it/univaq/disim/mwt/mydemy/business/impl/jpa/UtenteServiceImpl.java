package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import it.univaq.disim.mwt.mydemy.business.BusinessException;
import it.univaq.disim.mwt.mydemy.domain.CreatoreInfo;
import it.univaq.disim.mwt.mydemy.domain.Ruolo;
import it.univaq.disim.mwt.mydemy.presentation.Utility;
import it.univaq.disim.mwt.mydemy.repository.IscrizioneRepository;
import it.univaq.disim.mwt.mydemy.repository.RuoloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.disim.mwt.mydemy.business.RequestGrid;
import it.univaq.disim.mwt.mydemy.business.ResponseGrid;
import it.univaq.disim.mwt.mydemy.business.UtenteService;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import it.univaq.disim.mwt.mydemy.repository.UtenteRepository;
import org.springframework.web.multipart.MultipartFile;


@Service
@Transactional
public class UtenteServiceImpl implements UtenteService {
	
	@Autowired UtenteRepository utenteRepository;
	@Autowired
	IscrizioneRepository iscrizioneRepository;
	@Autowired
	private RuoloRepository ruoloRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Utente findByUsername(String username) {
		return utenteRepository.findByUsername(username);
	}

	@Override
	public Optional<Utente> findByID(Long id) {
		return utenteRepository.findById(id);
	}

	@Override
	public void create(Utente utente) {
		// set default user role
		Optional<Ruolo> ruoloUser = ruoloRepository.findByCodeIgnoreCase("USER");
		utente.addRuolo(ruoloUser.get());
		// encode password
		final String password = passwordEncoder.encode(utente.getPassword());
		utente.setPassword(password);
		utenteRepository.save(utente);
	}

	public void update(Utente utente) {
		if(!utente.getPassword().equalsIgnoreCase("")) {
			// encode password
			final String password = passwordEncoder.encode(utente.getPassword());
			utente.setPassword(password);
		}

		utenteRepository.save(utente);
	}

	@Override
	public void updateProfilo(Utente nuovoProfilo) {
		utenteRepository.save(nuovoProfilo);		
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
	@Transactional(readOnly = true)
	public Long count() {
		return utenteRepository.count();
	}

	@Override
	public void changeProfilePicture(Long userId, MultipartFile foto) throws BusinessException, IOException {

		Optional<Utente> utente = utenteRepository.findById(userId);
		if(utente.isPresent()) {
			utente.get().setFoto(foto.getBytes());
			utenteRepository.save(utente.get());
		} else {
			throw new BusinessException("utente non trovato");
		}
	}

	@Override
	@Transactional
	public void becomeCreatore(Long userId, CreatoreInfo infoCreatore) throws BusinessException {
		Optional<Utente> optionalUtente = utenteRepository.findById(userId);
		if(optionalUtente.isPresent()) {
			Utente utente = optionalUtente.get();
			Ruolo ruoloCreatore = ruoloRepository.findByCodeIgnoreCase("CREATOR").get();
			if(!utente.getRuoli().contains(ruoloCreatore)) {
				utente.addRuolo(ruoloCreatore);
				utente.setCreatoreInfo(infoCreatore);
				utenteRepository.save(utente);
				Utility.addRole(ruoloCreatore.getCode());
			}
		} else {
			throw new BusinessException("utente non presente");
		}
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
