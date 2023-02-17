package it.univaq.disim.mwt.mydemy.business.impl.jpa;


import it.univaq.disim.mwt.mydemy.business.DashboardAdminService;
import it.univaq.disim.mwt.mydemy.business.ResponsePieData;
import it.univaq.disim.mwt.mydemy.business.ResponsePieDataDataset;
import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.repository.CorsoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class DashboardAdminServiceImpl implements DashboardAdminService {
	@Autowired
	CorsoRepository corsoRepository;


	@Override
	public ResponsePieData getVendite() {
		ResponsePieData entity = new ResponsePieData();
		ResponsePieDataDataset ds = new ResponsePieDataDataset();
		entity.addDataset(ds);

		List<Corso> corsi = corsoRepository.findByApprovatoIsTrue(PageRequest.of(0, Integer.MAX_VALUE));
		corsi.stream().forEach(c->{
			entity.addLabel(c.getTitolo());
			ds.addData(c.getIscrizioni().size());
		});

		return entity;
	}
}
