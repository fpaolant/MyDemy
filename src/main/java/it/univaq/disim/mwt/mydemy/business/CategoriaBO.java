package it.univaq.disim.mwt.mydemy.business;

import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.Categoria;

public interface CategoriaBO {
	List<Categoria> findAll();
	List<Categoria> findAllRootCategories();
	List<Categoria> findChildCategories(Categoria categoria);
	Optional<Categoria> findByID(Long id);
	void save(Categoria categoria);
	void delete(Categoria categoria);
	int getLevel(Categoria categoria, List<Categoria> categorie);

	List<Categoria> getTree();
}
