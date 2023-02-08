package it.univaq.disim.mwt.mydemy.business;

import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.Tag;

public interface TagBo {
	
	List<Tag> findByNometag(String nomeTag);
	
	List<Tag> findAll();

	Optional<Tag> findByID(Long id);
	
	void save(Tag tag);
		
	void delete(Tag tag);


}
