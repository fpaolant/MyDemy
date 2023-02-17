package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import java.util.List;
import java.util.Optional;


import it.univaq.disim.mwt.mydemy.domain.CreatoreInfo;
import it.univaq.disim.mwt.mydemy.domain.Utente;
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

	@Override
	public CreatoreInfo findByUtente(Utente utente) {
		return creatoreInfoRepository.findByUtente(utente);
	}

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
	public void updateProfilo(CreatoreInfo nuovoProfilo) {
		creatoreInfoRepository.save(nuovoProfilo);
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