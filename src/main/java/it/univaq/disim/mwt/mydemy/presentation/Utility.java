package it.univaq.disim.mwt.mydemy.presentation;



import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import it.univaq.disim.mwt.mydemy.domain.Utente;
import it.univaq.disim.mwt.mydemy.security.UserDetailsImpl;

public class Utility {

	public static Utente getUtente() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
			return ((UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal()).getUtente();
		} else {
			return null;
		}
	}
	
}

