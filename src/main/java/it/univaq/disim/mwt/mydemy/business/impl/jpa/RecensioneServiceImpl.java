package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import it.univaq.disim.mwt.mydemy.business.RecensioneService;
import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.domain.Recensione;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecensioneServiceImpl implements RecensioneService {

	@Override
	public List<Recensione> findByCorso(Corso corso) {
		return null;
	}
	@Override
	public List<Recensione> findByAutore(Utente autore) {
		return null;
	}
	@Override
	public Optional<Recensione> findByID(Long id) {
		return Optional.empty();
	}
	@Override
	public void create(Recensione recensione) {

	}

	@Override
	public void update(Recensione recensione) {

	}

	@Override
	public void delete(Recensione recensione) {

	}
}
