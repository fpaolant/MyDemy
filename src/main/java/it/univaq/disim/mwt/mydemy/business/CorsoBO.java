package it.univaq.disim.mwt.mydemy.business;

import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface CorsoBO {

	void save(Corso corso);

	List<Corso> findAllCorsi();

	Page<Corso> findAllNextCorsi(PageRequest pageRequest);
	
	List<Corso> findByCreatore(Utente creatore);

	Optional<Corso> findByIdAndCreatore(Long id, Utente creatore);

	Optional<Corso> findByID(Long id);
	
	Optional<Corso> findByTitolo(String titolo);

	void update(Corso corso);

	ResponseGrid<Corso> findAllPaginated(RequestGrid requestGrid);

	public ResponseGrid<Corso> findAllByCreatorePaginated(Utente creatore, RequestGrid requestGrid);

	void delete(Corso corso);
	
	

}
