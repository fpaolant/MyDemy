package it.univaq.disim.mwt.mydemy.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import it.univaq.disim.mwt.mydemy.domain.Ordine;
import it.univaq.disim.mwt.mydemy.domain.StatoOrdine;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long> {
	public List<Ordine> findByOrdinante(Utente ordinante);
	public List<Ordine> findByStato(StatoOrdine stato);
	public List<Ordine> findByOrdinanteContainingIgnoreCase(String ordinante, Pageable pageable);
}
