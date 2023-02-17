package it.univaq.disim.mwt.mydemy.business;

import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.Ruolo;
import org.springframework.data.domain.Pageable;


public interface RuoloService {
	
	List<Ruolo> findAll();
	List<Ruolo> findAllAdditionalRoles(Pageable pageable);
	ResponseGrid<Ruolo> findAllPaginated(RequestGrid requestGrid);
	List<Ruolo> findByNome(String nome);
	Optional<Ruolo> findByID(Long id);
	Optional<Ruolo> findByCode(String code);
	void create(Ruolo ruolo);
	void update(Ruolo ruolo);
	void delete(Ruolo ruolo);
}
