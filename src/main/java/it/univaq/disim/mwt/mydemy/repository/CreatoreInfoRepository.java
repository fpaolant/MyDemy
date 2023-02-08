package it.univaq.disim.mwt.mydemy.repository;

import java.util.List;

import it.univaq.disim.mwt.mydemy.domain.CreatoreInfo;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CreatoreInfoRepository extends JpaRepository<CreatoreInfo, Long> {
    public CreatoreInfo findByUtente(Utente utente);
}
