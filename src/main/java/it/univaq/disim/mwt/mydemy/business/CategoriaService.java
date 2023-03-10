package it.univaq.disim.mwt.mydemy.business;

import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.Categoria;
import org.springframework.data.domain.Pageable;

public interface CategoriaService {
	List<Categoria> findAll();
	List<Categoria> findAllRootCategories(Pageable pageable);
	List<Categoria> findChildCategories(Categoria categoria);
	List<Categoria> findAllExceptOne(Categoria categoria);
	Optional<Categoria> findByID(Long id);
	void create(Categoria categoria);
	void update(Categoria categoria) throws BusinessException;
	void setParent(Long id , Long parentId) throws BusinessException;
	void delete(Long id);
	int getLevel(Categoria categoria, List<Categoria> categorie);

	List<ResponseCategoryItem> getTree();
}
