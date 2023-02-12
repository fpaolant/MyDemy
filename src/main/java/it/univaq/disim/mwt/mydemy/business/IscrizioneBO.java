package it.univaq.disim.mwt.mydemy.business;

import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.domain.Iscrizione;
import it.univaq.disim.mwt.mydemy.domain.Utente;

public interface IscrizioneBO {

	Optional<Iscrizione> findById(Long id);
	List<Iscrizione> findByCorso(Corso corso);
	List<Iscrizione> findByUtenteAndCorso(Utente utente, Corso corso);
	List<Iscrizione> findByUtente(Utente utente);
	ResponseGrid<Iscrizione> findAllByCorsoPaginated(Corso corso, RequestGrid requestGrid);
	float getPercentualeSuperatoTotale();
	float getPercentualeSuperato(Corso corso);
	float getPercentualeSuperato(Utente creatore);
	void save(Iscrizione iscrizione);
	void delete(Iscrizione iscrizione);
	Long count();
	Long count(Corso corso);
	Long count(Utente creatore);
}
