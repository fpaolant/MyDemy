package it.univaq.disim.mwt.mydemy.business;

import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.Tag;

public interface TagService {
	
	List<Tag> findByNometag(String nomeTag);
	List<Tag> findAll();
	Optional<Tag> findByID(Long id);
	void create(Tag tag);
	void update(Tag tag);
	void delete(Tag tag);
}
