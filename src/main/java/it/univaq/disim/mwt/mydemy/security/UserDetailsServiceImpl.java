package it.univaq.disim.mwt.mydemy.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import it.univaq.disim.mwt.mydemy.business.UtenteBO;
import it.univaq.disim.mwt.mydemy.domain.Utente;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UtenteBO service;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Utente utente = service.findByUsername(username);
		
		if(utente == null) {
			throw new UsernameNotFoundException("utente " + username + " non trovato");
		}
		return new UserDetailsImpl(utente);
	}

}
