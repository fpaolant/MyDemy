package it.univaq.disim.mwt.mydemy.repository;

import java.util.List;

import it.univaq.disim.mwt.mydemy.domain.Ruolo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import it.univaq.disim.mwt.mydemy.domain.Utente;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
	List<Utente> findByCognomeContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrNomeContainingIgnoreCase(String cognome, String username, String email, String name, Pageable paging);
	
	Utente findByUsername(final String username);

	List<Utente> findByRuoli_(Ruolo ruolo);
}
