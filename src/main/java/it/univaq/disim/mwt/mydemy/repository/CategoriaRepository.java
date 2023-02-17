package it.univaq.disim.mwt.mydemy.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import it.univaq.disim.mwt.mydemy.domain.Categoria;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	//Implementation AUTO-GENERATED By Spring-Data-Jpa

	List<Categoria> findByParentIsNull(Pageable pageable);
	List<Categoria> findCategoriaByParent(Categoria catgeoria);
	List<Categoria> findByIdIsNot(Long id);

}
