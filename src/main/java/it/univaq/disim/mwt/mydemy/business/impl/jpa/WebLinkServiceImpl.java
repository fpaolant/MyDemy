package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.disim.mwt.mydemy.business.WebLinkService;
import it.univaq.disim.mwt.mydemy.domain.WebLink;
import it.univaq.disim.mwt.mydemy.repository.WebLinkRepository;

@Service
@Transactional
public class WebLinkServiceImpl implements WebLinkService {
	@Autowired WebLinkRepository webLinkRepository;

	@Override
	public Optional<WebLink> findByID(Long id) {
		return webLinkRepository.findById(id);
	}
	@Override
	public void create(WebLink wl) {
		webLinkRepository.save(wl);
	}
	@Override
	public void update(WebLink wl) {
		webLinkRepository.save(wl);
	}
	@Override
	public void delete(WebLink wl) {
		webLinkRepository.delete(wl);
	}

}
