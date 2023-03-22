package it.univaq.disim.mwt.mydemy.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import it.univaq.disim.mwt.mydemy.domain.Ruolo;
import it.univaq.disim.mwt.mydemy.domain.Utente;

public class UserDetailsImpl implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	private static final String ROLE_PREFIX = ""; //"ROLE_";
	
	private Utente utente;

	public UserDetailsImpl(Utente utente) {
		super();
		this.utente = utente;
	}
	@Override
	@Transactional
	public Collection<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> result = new ArrayList<>();
		result.add(new SimpleGrantedAuthority("USER"));
		for (Ruolo ruolo : utente.getRuoli()) {
			result.add(new SimpleGrantedAuthority(ruolo.getCode()));
		}
		return result;
	}
	@Override
	public String getPassword() {
		return utente.getPassword();
	}
	@Override
	public String getUsername() {
		return utente.getUsername();
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return utente.getEnabled();
	}
	@Override
	public String toString() {
		return "UserDetailsImpl [username=" + utente.getUsername() + " ruolo=" + utente.getRuoli().toString() + "]";
	}
	public Utente getUtente() {
		return utente;
	}
}
