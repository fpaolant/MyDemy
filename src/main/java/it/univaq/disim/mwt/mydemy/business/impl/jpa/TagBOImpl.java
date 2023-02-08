package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.disim.mwt.mydemy.business.TagBo;
import it.univaq.disim.mwt.mydemy.domain.Tag;
import it.univaq.disim.mwt.mydemy.repository.TagRepository;

@Service
@Transactional
public class TagBOImpl implements TagBo {
	
	@Autowired TagRepository tagRepository;

	@Override
	public List<Tag> findByNometag(String nomeTag) {
		return tagRepository.findByNomeIgnoreCaseContaining(nomeTag);
	}
	
	@Override
	public List<Tag> findAll() {
		Sort sortCriteria = Sort.by("nome");
		return tagRepository.findAll(sortCriteria);
	}

	@Override
	public Optional<Tag> findByID(Long id) {
		return tagRepository.findById(id);
	}

	@Override
	public void save(Tag tag) {
		tagRepository.save(tag);
	}

	@Override
	public void delete(Tag tag) {
		tagRepository.delete(tag);
		
	}

}
