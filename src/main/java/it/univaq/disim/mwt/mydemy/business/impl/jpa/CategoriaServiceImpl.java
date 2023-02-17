package it.univaq.disim.mwt.mydemy.business.impl.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.business.BusinessException;
import it.univaq.disim.mwt.mydemy.business.ResponseCategoryItem;
import it.univaq.disim.mwt.mydemy.repository.OrdineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.univaq.disim.mwt.mydemy.business.CategoriaService;
import it.univaq.disim.mwt.mydemy.domain.Categoria;
import it.univaq.disim.mwt.mydemy.repository.CategoriaRepository;


@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {
	@Autowired CategoriaRepository categoriaRepository;
	@Autowired
	private OrdineRepository ordineRepository;

	@Override
	public void create(Categoria categoria) {
		categoriaRepository.save(categoria);
	}

	@Override
	@Transactional
	public void update(Categoria categoria) {
		Optional<Categoria> categoriaOld = categoriaRepository.findById(categoria.getId());
		if(categoriaOld.isPresent()) {
			categoria.setVersion(categoriaOld.get().getVersion());
			categoriaRepository.save(categoria);
		}
		categoriaRepository.save(categoria);
	}

	@Override
	@Transactional
	public void setParent(Long id, Long parentId) throws BusinessException {
		Optional<Categoria> opt = categoriaRepository.findById(id);
		if(opt.isPresent()) {
			Categoria categoria = opt.get();

			if(!parentId.equals(new Long(0))) {
				Optional<Categoria> optParent = categoriaRepository.findById(parentId);
				if(optParent.isPresent()) {
					categoria.setParent(optParent.get());
					categoriaRepository.save(categoria);
				} else {
					throw new BusinessException("Categoria parent non valida");
				}
			} else {
				categoria.setParent(null);
				categoriaRepository.save(categoria);
			}
		} else {
			throw new BusinessException("Categoria non valida");
		}
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
	public void delete(Long id) {
		categoriaRepository.deleteById(id);
	}

	@Override
	public List<Categoria> findAllRootCategories() {
		return (List<Categoria>) categoriaRepository.findByParentIsNull();
	}

	@Override
	public List<Categoria> findChildCategories(Categoria categoria) {
		return categoriaRepository.findCategoriaByParent(categoria);
	}

	@Override
	public List<Categoria> findAllExceptOne(Categoria categoria) {
		return categoriaRepository.findByIdIsNot(categoria.getId());
	}

	@Override
	public int getLevel(Categoria categoria, List<Categoria> categorie) {
		if(categoria.getParent()==null) return 1;
		else return getLevel(categoria.getParent(), categorie) + 1;
	}
	@Override
	public List<ResponseCategoryItem> getTree() {
		List<ResponseCategoryItem> entities = new ArrayList<>();


		List<Categoria> rootCategorie = categoriaRepository.findByParentIsNull();

		List<Categoria> orderedCategorie = new ArrayList<>();
		for (Categoria categoria: rootCategorie) {
			orderedCategorie.add(categoria);
			getTreeInner(categoria, orderedCategorie);
		}

		for (Categoria c : orderedCategorie) {
			long pid = (c.getParent()!=null)? c.getParent().getId(): 0;
			entities.add(new ResponseCategoryItem(c.getId(), pid, c.getNome(), this.getLevel(c, orderedCategorie)));
		}
		return entities;
	}

	private void getTreeInner(Categoria father, List<Categoria> ordered) {
		for(Categoria child : father.getSubCategorie()) {
			ordered.add(child);
			getTreeInner(child, ordered);
		}
	}

}
