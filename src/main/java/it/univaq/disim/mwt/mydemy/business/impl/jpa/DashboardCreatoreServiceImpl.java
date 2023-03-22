package it.univaq.disim.mwt.mydemy.business.impl.jpa;


import it.univaq.disim.mwt.mydemy.business.*;
import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import it.univaq.disim.mwt.mydemy.repository.CorsoRepository;
import it.univaq.disim.mwt.mydemy.repository.IscrizioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class DashboardCreatoreServiceImpl implements DashboardCreatoreService {
	@Autowired
	CorsoRepository corsoRepository;
	@Autowired
	IscrizioneRepository iscrizioneRepository;


	@Override
	public ResponsePieData getVendite(Utente creatore) {
		ResponsePieData entity = new ResponsePieData();

		ResponsePieDataDataset ds = new ResponsePieDataDataset();
		entity.addDataset(ds);

		List<Corso> corsi = corsoRepository.findByCreatore(creatore);
		corsi.stream().forEach(c->{
			entity.addLabel(c.getTitolo());
			ds.addData(c.getIscrizioni().size());
		});

		return entity;
	}
}
