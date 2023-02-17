package it.univaq.disim.mwt.mydemy.business;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.domain.CreatoreInfo;
import it.univaq.disim.mwt.mydemy.domain.Ruolo;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import org.springframework.web.multipart.MultipartFile;

public interface UtenteService {
	
	Utente findByUsername(String username);
	Optional<Utente> findByID(Long id);
	void create(Utente utente);
	void update(Utente utente);
	void updateProfilo(Utente nuovoProfilo);
	ResponseGrid<Utente> findAllPaginated(RequestGrid requestGrid);
	void delete(Utente utente);
	void changePassword(Utente utente, String password);
	List<Utente> findAllByRole(Ruolo ruolo);
	Long count();
	void changeProfilePicture(Long userId, MultipartFile foto) throws BusinessException, IOException;

	void becomeCreatore(Long userId, CreatoreInfo infoCreatore) throws BusinessException;

	void enable(Long userId) throws BusinessException;
}
