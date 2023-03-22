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
	void updateProfilo(Utente nuovoProfilo) throws BusinessException;
	ResponseGrid<Utente> findAllPaginated(RequestGrid requestGrid);
	List<Utente> findAllByRole(Ruolo ruolo);
	Long count();
	void changeProfilePicture(Long userId, MultipartFile foto) throws BusinessException, IOException;
	void becomeCreatore(Long userId, CreatoreInfo infoCreatore) throws BusinessException;
}
