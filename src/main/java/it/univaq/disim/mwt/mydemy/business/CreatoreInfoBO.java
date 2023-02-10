package it.univaq.disim.mwt.mydemy.business;

import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.CreatoreInfo;
import it.univaq.disim.mwt.mydemy.domain.Utente;

public interface CreatoreInfoBO {
	
	CreatoreInfo findByUtente(Utente utente);

	Optional<CreatoreInfo> findByID(Long id);
	
	void save(CreatoreInfo creatoreInfo);
	
	void updateProfilo(CreatoreInfo nuovoProfilo);
	
	List<CreatoreInfo> findAll();
	
	void delete(CreatoreInfo creatoreInfo);

}
