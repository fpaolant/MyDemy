package it.univaq.disim.mwt.mydemy.business;

import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.domain.Recensione;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface RecensioneService {
	List<Recensione> findByCorso(Corso corso, Pageable pageable);
	List<Recensione> findByAutore(Utente autore, Pageable pageable);
	Optional<Recensione> findByAutoreIdAndCorsoId(Long autoreId, Long corsoId);
	boolean recensito(Long autoreId, Long corsoId);
	Optional<Recensione> findByID(String id);
	void create(Recensione recensione);
	void delete(Recensione recensione);
	void deleteById(String id);
	void deleteByAutore(Utente autore);
	Double calcolaMediaByCorso(Corso corso);
}
