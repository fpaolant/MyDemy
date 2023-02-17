package it.univaq.disim.mwt.mydemy.business;

import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.Ruolo;
import it.univaq.disim.mwt.mydemy.domain.Utente;

public interface UtenteService {
	
	Utente findByUsername(String username);
	Optional<Utente> findByID(Long id);
	void create(Utente utente);
	void update(Utente utente);
	void updateProfilo(Utente nuovoProfilo);
	ResponseGrid<Utente> findAllPaginated(RequestGrid requestGrid);
	void delete(Utente utente);
	void changePassword(Utente utente, String password);
	List<Utente> findAllByRole(Ruolo ruolo);
	Long count();
}
