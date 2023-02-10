package it.univaq.disim.mwt.mydemy.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.domain.Iscrizione;
import it.univaq.disim.mwt.mydemy.domain.Utente;

public interface IscrizioneRepository extends JpaRepository<Iscrizione, Long> {
	//Implementation AUTO-GENERATED By Spring-Data-Jpa

	List<Iscrizione> findByCorso(Corso corso);
	List<Iscrizione> findByUtenteOrderByDataAsc(Utente utente);
	List<Iscrizione> findByUtenteAndCorsoOrderByDataAsc(Utente utente, Corso corso);
	List<Iscrizione> findAllByCorsoOrderByUtenteCognomeAsc(Corso corso, Pageable paging);
	List<Iscrizione> findAllByCorsoAndUtenteCognomeContainingIgnoreCaseOrderByUtenteCognomeAsc(Corso corso, String needle, Pageable paging);
}
