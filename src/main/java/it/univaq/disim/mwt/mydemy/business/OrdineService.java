package it.univaq.disim.mwt.mydemy.business;

import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.Ordine;
import it.univaq.disim.mwt.mydemy.domain.StatoOrdine;
import it.univaq.disim.mwt.mydemy.domain.Utente;

public interface OrdineService {
	List<Ordine> findByOrdinante(Utente utente);
	List<Ordine> findByStato(StatoOrdine stato);
	Optional<Ordine> findByID(Long id);
	void create(Ordine ordine);
	void update(Ordine ordine);
	ResponseGrid<Ordine> findAllPaginated(RequestGrid requestGrid);
	void delete(Ordine ordine);
}
