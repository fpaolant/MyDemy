package it.univaq.disim.mwt.mydemy.repository;

import it.univaq.disim.mwt.mydemy.domain.CreatoreInfo;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatoreInfoRepository extends JpaRepository<CreatoreInfo, Long> {
    CreatoreInfo findByUtente(Utente utente);
}
