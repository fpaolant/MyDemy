package it.univaq.disim.mwt.mydemy.business;

import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.CreatoreInfo;
import it.univaq.disim.mwt.mydemy.domain.Utente;

public interface CreatoreInfoService {

	Optional<CreatoreInfo> findByID(Long id);
	void create(CreatoreInfo creatoreInfo);
	void update(CreatoreInfo creatoreInfo);
	List<CreatoreInfo> findAll();
	void updateProfilo(Utente utente, String titolo, String descrizione) throws BusinessException;
	void delete(CreatoreInfo creatoreInfo);
}
