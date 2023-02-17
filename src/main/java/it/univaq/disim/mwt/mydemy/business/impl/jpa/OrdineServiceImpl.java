package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import it.univaq.disim.mwt.mydemy.business.OrdineService;
import it.univaq.disim.mwt.mydemy.business.RequestGrid;
import it.univaq.disim.mwt.mydemy.business.ResponseGrid;
import it.univaq.disim.mwt.mydemy.domain.Ordine;
import it.univaq.disim.mwt.mydemy.domain.StatoOrdine;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import it.univaq.disim.mwt.mydemy.repository.OrdineRepository;


@Service
@Transactional
public class OrdineServiceImpl implements OrdineService {
	
	@Autowired OrdineRepository ordineRepository;

	@Override
	public Optional<Ordine> findByID(Long id) {
		return ordineRepository.findById(id);
	}

	@Override
	public void create(Ordine ordine) {
		ordineRepository.save(ordine);
	}

	@Override
	public void update(Ordine ordine) {
		ordineRepository.save(ordine);
	}

	@Override
	public ResponseGrid<Ordine> findAllPaginated(RequestGrid requestGrid) {
		String sortCol = requestGrid.getSortCol();
		
		List<Ordine> ordini = null;
		// sort order and value
		Sort sortCriteria = Sort.by(sortCol);
		if(requestGrid.getSortDir().equalsIgnoreCase("desc")) sortCriteria.descending();
		// pageable
		PageRequest pageRequest = PageRequest.of(requestGrid.getStart(), requestGrid.getLength(), sortCriteria);
		
		
		if("".equals(requestGrid.getSearch().getValue())) {			
			ordini = ordineRepository.findAll(pageRequest).stream().collect(Collectors.toList());
		} else {	
			ordini = ordineRepository.findByOrdinanteContainingIgnoreCase(requestGrid.getSearch().getValue(), pageRequest);
		}
		
		return new ResponseGrid<Ordine>(requestGrid.getDraw(), ordini.size(), ordini.size(), ordini);
	}

	@Override
	public void delete(Ordine ordine) {
		ordineRepository.delete(ordine);
	}

	@Override
	public List<Ordine> findByOrdinante(Utente utente) {
		return (List<Ordine>) ordineRepository.findByOrdinante(utente);
	}

	@Override
	public List<Ordine> findByStato(StatoOrdine stato) {
		return (List<Ordine>) ordineRepository.findByStato(stato);
	} 

}