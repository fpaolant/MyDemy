package it.univaq.disim.mwt.mydemy.business;

import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.Categoria;
import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import org.springframework.data.domain.PageRequest;

public interface CorsoBO {

	void save(Corso corso);

	List<Corso> findAllCorsi();

	List<Corso> findAllNextCorsi(PageRequest pageRequest);
	
	List<Corso> findByCreatore(Utente creatore);

	Optional<Corso> findByIdAndCreatore(Long id, Utente creatore);

	Optional<Corso> findByID(Long id);
	
	List<Corso> findByTitoloContainingIgnoreCase(String searchString, PageRequest pageRequest);

	void update(Corso corso);

	ResponseGrid<Corso> findAllPaginated(RequestGrid requestGrid);

	ResponseGrid<Corso> findAllApprovedPaginated(RequestGrid requestGrid);

	public ResponseGrid<Corso> findAllByCreatorePaginated(Utente creatore, RequestGrid requestGrid);

	public List<Corso> findAllByCategoriaAndTitoloContainingIgnoreCasePaginatedSortBy(Categoria categoria, String searchTitleString, PageRequest pageRequest);

	void delete(Corso corso);
}
