package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import it.univaq.disim.mwt.mydemy.business.RecensioneService;
import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.domain.Recensione;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import it.univaq.disim.mwt.mydemy.repository.mongo.RecensioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecensioneServiceImpl implements RecensioneService {

	@Autowired
	RecensioneRepository recensioneRepository;

	@Override
	public List<Recensione> findByCorso(Corso corso, Pageable pageable) {
		return recensioneRepository.findByCorsoId(corso.getId(), pageable);
	}
	@Override
	public double calcolaMediaByCorso(Corso corso) {
		return recensioneRepository.calcolaMediaVotoByCorsoId(corso.getId());
	}
	@Override
	public List<Recensione> findByAutore(Utente autore, Pageable pageable) {
		return recensioneRepository.findByAutoreId(autore.getId(), pageable);
	}
	@Override
	public Optional<Recensione> findByAutoreIdAndCorsoId(Long autoreId, Long corsoId) {
		return  recensioneRepository.findByAutoreIdAndCorsoId(autoreId, corsoId);
	}
	@Override
	public boolean recensito(Long autoreId, Long corsoId) {
		return recensioneRepository.findByAutoreIdAndCorsoId(autoreId, corsoId).isPresent();
	}
	@Override
	public Optional<Recensione> findByID(String id) {
		return recensioneRepository.findById(id);
	}
	@Override
	public void create(Recensione recensione) {
		recensioneRepository.save(recensione);
	}
	@Override
	public void delete(Recensione recensione) {
		recensioneRepository.delete(recensione);
	}
	@Override
	public void deleteById(String id) {
		recensioneRepository.deleteById(id);
	}
	@Override
	public void deleteByAutore(Utente autore) {
		recensioneRepository.deleteByAutoreId(autore.getId());
	}
}
