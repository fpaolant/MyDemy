package it.univaq.disim.mwt.mydemy.business;

import it.univaq.disim.mwt.mydemy.domain.Utente;
import java.util.Optional;

public interface AdminUtenteService {
	
	Utente findByUsername(String username);
	Optional<Utente> findByID(Long id);
	void create(Utente utente);
	void update(Utente utente) throws BusinessException;
	ResponseGrid<Utente> findAllPaginated(RequestGrid requestGrid);
	void delete(Utente utente) throws BusinessException;
	void delete(Long utenteId) throws BusinessException;
	void enable(Long userId) throws BusinessException;
}
