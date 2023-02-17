package it.univaq.disim.mwt.mydemy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import it.univaq.disim.mwt.mydemy.domain.Ruolo;
import org.springframework.stereotype.Repository;

@Repository
public interface RuoloRepository extends JpaRepository<Ruolo, Long> {
	//Implementation AUTO-GENERATED By Spring-Data-Jpa

	public List<Ruolo> findByNomeContainingIgnoreCaseOrCodeContainingIgnoreCase (String nome, String Code, Pageable paging);

	public List<Ruolo> findByNomeIgnoreCaseContaining(String nome);
	
	public Optional<Ruolo> findByCodeIgnoreCase(String code);
}
