package it.univaq.disim.mwt.mydemy.business;

import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.domain.Iscrizione;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import org.docx4j.openpackaging.exceptions.Docx4JException;

import javax.xml.bind.JAXBException;

public interface IscrizioneService {

	Optional<Iscrizione> findById(Long id);
	List<Iscrizione> findByCorso(Corso corso);
	List<Iscrizione> findByUtenteAndCorso(Utente utente, Corso corso);
	List<Iscrizione> findByUtente(Utente utente);
	ResponseGrid<Iscrizione> findAllByCorsoPaginated(Corso corso, RequestGrid requestGrid);
	float getPercentualeSuperatoTotale();
	float getPercentualeSuperato(Corso corso);
	float getPercentualeSuperato(Utente creatore);
	void create(Iscrizione iscrizione);
	void update(Iscrizione iscrizione);
	void delete(Iscrizione iscrizione);
	Long count();
	Long count(Corso corso);
	Long count(Utente creatore);
	void generaCertificato(Iscrizione iscrizione) throws Docx4JException, JAXBException;
}
