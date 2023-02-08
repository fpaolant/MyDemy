package it.univaq.disim.mwt.mydemy.repository;

import java.util.List;

import it.univaq.disim.mwt.mydemy.domain.Ruolo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import it.univaq.disim.mwt.mydemy.domain.Utente;

public interface UtenteRepository extends JpaRepository<Utente, Long> {
	public List<Utente> findByCognomeContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrNomeContainingIgnoreCase(String cognome, String username, String email, String name, Pageable paging);
	
	public Utente findByUsername(final String username);

	public List<Utente> findByRuoli_(Ruolo ruolo);
}
