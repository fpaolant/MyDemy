package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import java.util.List;
import java.util.Optional;


import it.univaq.disim.mwt.mydemy.domain.CreatoreInfo;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.disim.mwt.mydemy.business.CreatoreInfoBO;
import it.univaq.disim.mwt.mydemy.business.RequestGrid;
import it.univaq.disim.mwt.mydemy.business.ResponseGrid;
import it.univaq.disim.mwt.mydemy.repository.CreatoreInfoRepository;


@Service
@Transactional
public class CreatoreInfoBoImpl implements CreatoreInfoBO {
	
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
	public void save(CreatoreInfo creatoreInfo) {
		creatoreInfoRepository.save(creatoreInfo);
		
	}

	@Override
	public void updateProfilo(CreatoreInfo nuovoProfilo) {
		creatoreInfoRepository.save(nuovoProfilo);
	}

	/*@Override
	public ResponseGrid<CreatoreInfo> findAllPaginated(RequestGrid requestGrid) {
		String sortCol = requestGrid.getSortCol();
				
		List<CreatoreInfo> utenti = null;
		// sort order and value
		Sort sortCriteria = Sort.by(sortCol);
		if(requestGrid.getSortDir().equalsIgnoreCase("desc")) sortCriteria.descending();
		// pageable
		PageRequest pr = PageRequest.of(requestGrid.getStart(), requestGrid.getLength(), sortCriteria);
		
		
		if("".equals(requestGrid.getSearch().getValue())) {			
			utenti = creatoreInfoRepository.findAllPaginated(pr);
		} else {	
			utenti = creatoreInfoRepository.findAllPaginatedWithSearchValue(ConversionUtility.addPercentSuffix(requestGrid.getSearch().getValue()), pr);
		}
		
		return new ResponseGrid<CreatoreInfo>(requestGrid.getDraw(), utenti.size(), utenti.size(), utenti);
	}*/

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
