package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import java.util.List;
import java.util.Optional;


import it.univaq.disim.mwt.mydemy.business.BusinessException;
import it.univaq.disim.mwt.mydemy.domain.CreatoreInfo;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import it.univaq.disim.mwt.mydemy.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.disim.mwt.mydemy.business.CreatoreInfoService;
import it.univaq.disim.mwt.mydemy.repository.CreatoreInfoRepository;


@Service
@Transactional
public class CreatoreInfoServiceImpl implements CreatoreInfoService {
	
	@Autowired
	CreatoreInfoRepository creatoreInfoRepository;
	@Autowired
	private UtenteRepository utenteRepository;
	@Override
	public Optional<CreatoreInfo> findByID(Long id) {
		return creatoreInfoRepository.findById(id);
	}

	@Override
	public void create(CreatoreInfo creatoreInfo) {
		creatoreInfoRepository.save(creatoreInfo);
	}
	@Override
	public void update(CreatoreInfo creatoreInfo) {
		creatoreInfoRepository.save(creatoreInfo);
	}

	@Override
	@Transactional
	public void updateProfilo(Utente utente, String titolo, String descrizione) throws BusinessException {
		Optional<Utente> optionalUtente = utenteRepository.findById(utente.getId());
		if (optionalUtente.isEmpty()) throw new BusinessException("Utente non trovato");

		Utente u = optionalUtente.get();
		CreatoreInfo ci = optionalUtente.get().getCreatoreInfo();
		if(ci != null) {
			ci.setTitolo(titolo);
			ci.setDescrizione(descrizione);
		} else {
			ci = new CreatoreInfo(titolo, descrizione);
			u.setCreatoreInfo(ci);
		}
		utenteRepository.save(u);
	}

	@Override
	public void delete(CreatoreInfo creatoreInfo) {
		creatoreInfoRepository.delete(creatoreInfo);
	}

	@Override
	public List<CreatoreInfo> findAll() {
		Sort sortCriteria = Sort.by("cognome");
		return creatoreInfoRepository.findAll(sortCriteria);
	}

}