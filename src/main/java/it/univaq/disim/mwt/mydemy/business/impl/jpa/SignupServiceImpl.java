package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import it.univaq.disim.mwt.mydemy.business.*;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import it.univaq.disim.mwt.mydemy.repository.RuoloRepository;
import it.univaq.disim.mwt.mydemy.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashSet;
import java.util.Optional;


@Service
@Transactional
public class SignupServiceImpl implements SignupService {
	
	@Autowired
	private UtenteRepository utenteRepository;
	@Autowired
	private RuoloRepository ruoloRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void signUp(Utente utente) throws BusinessException {
		if(utenteRepository.findByUsername(utente.getUsername())!=null) {
			throw new BusinessException("Username non disponibile");
		}

		// encode password
		final String password = passwordEncoder.encode(utente.getPassword());
		utente.setPassword(password);

		utenteRepository.save(utente);
	}

	@Override
	public void changePassword(Utente utente, String password) throws BusinessException {
		Optional<Utente> optUtente = utenteRepository.findById(utente.getId());
		if(optUtente.isEmpty()) throw new BusinessException("Utente non trovato");
		Utente u = optUtente.get();
		u.setPassword(passwordEncoder.encode(password));
		utenteRepository.save(u);
	}


}
