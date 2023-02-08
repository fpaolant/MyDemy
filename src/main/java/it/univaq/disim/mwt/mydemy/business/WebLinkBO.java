package it.univaq.disim.mwt.mydemy.business;

import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.WebLink;

public interface WebLinkBO {

	Optional<WebLink> findByID(Long id);
	
	void save(WebLink wl);
		
	void delete(WebLink wl);


}
