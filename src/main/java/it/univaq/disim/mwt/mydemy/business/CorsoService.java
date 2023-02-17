package it.univaq.disim.mwt.mydemy.business;

import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.Categoria;
import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.domain.Iscrizione;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import org.springframework.data.domain.PageRequest;

public interface CorsoService {
	List<Corso> findAllNextCorsi(PageRequest pageRequest);
	List<Corso> findByCreatore(Utente creatore);
	Optional<Corso> findByIdAndCreatore(Long id, Utente creatore);
    Optional<Corso> findById(Long id);
    List<Corso> findByTitoloContainingIgnoreCase(String searchString, PageRequest pageRequest);
	ResponseGrid<Corso> findAllPaginated(RequestGrid requestGrid);
	ResponseGrid<Corso> findAllByCreatorePaginated(Utente creatore, RequestGrid requestGrid);
	List<Corso> findAllCriteria(String searchTitleString, PageRequest pageRequest);
	List<Corso> findAllCriteriaInCategoria(Categoria categoria, String searchTitleString, PageRequest pageRequest);
	Long count();

	void create(Corso corso);

	void update(Corso corso) throws BusinessException;
	void delete(Corso corso) throws BusinessException;

	void deleteById(Long id) throws BusinessException;

	void iscrivi(Long corsoId, Utente utente) throws BusinessException;

	void proponi(Corso corso, Utente creatore);

	Iscrizione setSuperato(Long iscrizioneId) throws BusinessException;
}
