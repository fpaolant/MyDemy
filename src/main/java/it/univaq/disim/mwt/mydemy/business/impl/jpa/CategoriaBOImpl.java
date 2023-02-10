package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.disim.mwt.mydemy.business.CategoriaBO;
import it.univaq.disim.mwt.mydemy.domain.Categoria;
import it.univaq.disim.mwt.mydemy.repository.CategoriaRepository;


@Service
@Transactional
public class CategoriaBOImpl implements CategoriaBO {
	@Autowired CategoriaRepository categoriaRepository;

	@Override
	public void save(Categoria categoria) {
		categoriaRepository.save(categoria);
	}

	@Override
	public List<Categoria> findAll() {
		return (List<Categoria>) categoriaRepository.findAll();
	}

	@Override
	public Optional<Categoria> findByID(Long id) {
		return categoriaRepository.findById(id);
	}

	@Override
	public void delete(Categoria categoria) {
		categoriaRepository.delete(categoria);
	}

	@Override
	public List<Categoria> findAllRootCategories() {
		return (List<Categoria>) categoriaRepository.findByParentIsNull();
	}

	@Override
	public List<Categoria> findChildCategories(Categoria categoria) {
		return (List<Categoria>) categoriaRepository.findCategoriaByParent(categoria);
	}

	@Override
	public int getLevel(Categoria categoria, List<Categoria> categorie) {
		if(categoria.getParent()==null) return 1;
		else return getLevel(categoria.getParent(), categorie) + 1;
	}

}
