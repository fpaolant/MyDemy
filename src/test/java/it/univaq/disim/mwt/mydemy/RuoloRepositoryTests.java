package it.univaq.disim.mwt.mydemy;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import it.univaq.disim.mwt.mydemy.domain.Ruolo;
import it.univaq.disim.mwt.mydemy.repository.RuoloRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RuoloRepositoryTests {
	@Autowired RuoloRepository repo;
	
	@Test
	public void testCreaRuoli() {
//		Ruolo user = new Ruolo("UtenteTest", "USERTEST", "ruolo utente del sito");
//		Ruolo admin = new Ruolo("AmministratoreTest", "ADMINTEST", "ruolo admin del sito");
//		
//		repo.saveAll(List.of(user, admin));
//		
//		List<Ruolo> listRuoli = repo.findAll();
//		
//		assertThat(listRuoli.size()).isEqualTo(2);
	}
}
