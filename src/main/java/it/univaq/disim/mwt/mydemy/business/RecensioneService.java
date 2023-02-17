package it.univaq.disim.mwt.mydemy.business;

import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.domain.Recensione;
import it.univaq.disim.mwt.mydemy.domain.Utente;

import java.util.List;
import java.util.Optional;


public interface RecensioneService {
	List<Recensione> findByCorso(Corso corso);
	List<Recensione> findByAutore(Utente autore);

	Optional<Recensione> findByID(Long id);

	void create(Recensione recensione);

	void update(Recensione recensione);
		
	void delete(Recensione recensione);
}
