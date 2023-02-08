package it.univaq.disim.mwt.mydemy.business;

import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.Ruolo;


public interface RuoloBO {
	
	List<Ruolo> findAll();
	
	ResponseGrid<Ruolo> findAllPaginated(RequestGrid requestGrid);
	
	List<Ruolo> findByNome(String nome);

	Optional<Ruolo> findByID(Long id);
	
	Optional<Ruolo> findByCode(String code);
	
	void save(Ruolo ruolo);
		
	void delete(Ruolo ruolo);

}
