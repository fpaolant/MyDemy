package it.univaq.disim.mwt.mydemy.repository.mongo;

import it.univaq.disim.mwt.mydemy.domain.Recensione;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecensioneRepository extends MongoRepository<Recensione, String> {

    Optional<Recensione> findByAutoreIdAndCorsoId(Long autoreId, Long corsoId);
    List<Recensione> findByAutoreId(Long autoreId, Pageable pageable);
    List<Recensione> findByCorsoId(Long corsoId, Pageable pageable);
    void deleteByAutoreId(Long autoreId);
    @Aggregation(pipeline = {
            "{$match :  { 'corsoId': ?0}}",
            "{$group: { _id: '', total: {$avg: $voto }}}" })
    Double calcolaMediaVotoByCorsoId(Long corsoId);
}