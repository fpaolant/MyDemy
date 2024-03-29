package it.univaq.disim.mwt.mydemy.business;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.domain.Iscrizione;
import it.univaq.disim.mwt.mydemy.domain.Utente;


public interface IscrizioneService {

	Optional<Iscrizione> findById(Long id);
	Optional<Iscrizione> findByUtenteAndCorso(Utente utente, Corso corso);
	List<Iscrizione> findByUtente(Utente utente);
	ResponseGrid<Iscrizione> findAllByCorsoCreatorePaginated(Long corsoId, Utente creatore, RequestGrid requestGrid) throws BusinessException;
	float getPercentualeSuperatoTotale();
	float getPercentualeSuperato(Utente creatore);
	void create(Iscrizione iscrizione);
	void update(Iscrizione iscrizione);
	void delete(Iscrizione iscrizione);
	Long count();
	Long count(Corso corso);
	Long count(Utente creatore);

	File generaCertificato(Utente utente, Long iscrizioneId) throws IOException, BusinessException;
}
