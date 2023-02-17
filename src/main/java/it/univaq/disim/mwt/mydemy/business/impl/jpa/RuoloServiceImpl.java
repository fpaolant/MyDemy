package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.disim.mwt.mydemy.business.RequestGrid;
import it.univaq.disim.mwt.mydemy.business.ResponseGrid;
import it.univaq.disim.mwt.mydemy.business.RuoloService;
import it.univaq.disim.mwt.mydemy.domain.Ruolo;
import it.univaq.disim.mwt.mydemy.repository.RuoloRepository;

@Service
@Transactional
public class RuoloServiceImpl implements RuoloService {
	
	@Autowired RuoloRepository ruoloRepository;

	@Override
	public List<Ruolo> findByNome(String nome) {
		return ruoloRepository.findByNomeIgnoreCaseContaining(nome);
	}

	@Override
	public Optional<Ruolo> findByID(Long id) {
		return ruoloRepository.findById(id);
	}

	@Override
	public void create(Ruolo ruolo) {
		ruoloRepository.save(ruolo);
	}

	@Override
	public void update(Ruolo ruolo) {
		ruoloRepository.save(ruolo);
	}

	@Override
	public void delete(Ruolo ruolo) {
		ruoloRepository.delete(ruolo);
		
	}

	@Override
	public List<Ruolo> findAll() {
		return ruoloRepository.findAll(Sort.by("nome"));
	}

	@Override
	public Optional<Ruolo> findByCode(String code) {
		return ruoloRepository.findByCodeIgnoreCase(code);
	}
	
	@Override
	public ResponseGrid<Ruolo> findAllPaginated(RequestGrid requestGrid) {
		String sortCol = requestGrid.getSortCol();
				
		List<Ruolo> ruoli = null;
		// sort order and value
		Sort sortCriteria = Sort.by(sortCol);
		if(requestGrid.getSortDir().equalsIgnoreCase("desc")) sortCriteria.descending();
		// pageable
		PageRequest pageRequest = PageRequest.of(requestGrid.getStart(), requestGrid.getLength(), sortCriteria);
		
		if("".equals(requestGrid.getSearch().getValue())) {			
			ruoli = ruoloRepository.findAll(pageRequest).stream().collect(Collectors.toList());
		} else {
			String searchString = requestGrid.getSearch().getValue();
			ruoli = ruoloRepository.findByNomeContainingIgnoreCaseOrCodeContainingIgnoreCase(searchString, searchString, pageRequest);
		}
		
		int numTotali = ruoloRepository.findAll().size();
		
		return new ResponseGrid<Ruolo>(requestGrid.getDraw(), numTotali, ruoli.size(), ruoli);
	}

}
