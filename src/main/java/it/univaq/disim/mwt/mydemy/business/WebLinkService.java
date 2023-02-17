package it.univaq.disim.mwt.mydemy.business;

import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.WebLink;

public interface WebLinkService {

	Optional<WebLink> findByID(Long id);
	void create(WebLink wl);
	void update(WebLink wl);
	void delete(WebLink wl);
}
