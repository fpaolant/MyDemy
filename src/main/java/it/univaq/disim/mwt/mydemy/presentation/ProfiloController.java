package it.univaq.disim.mwt.mydemy.presentation;

import it.univaq.disim.mwt.mydemy.business.BusinessException;
import it.univaq.disim.mwt.mydemy.business.SignupService;
import it.univaq.disim.mwt.mydemy.domain.CreatoreInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import it.univaq.disim.mwt.mydemy.business.UtenteService;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/common/profilo")
public class ProfiloController {
	@Autowired
	private UtenteService serviceUtente;

	@Autowired
	private SignupService serviceSignup;

	@GetMapping
	public String modificaProfiloStart(Model model) throws BusinessException {
		Optional<Utente> optionalUtente = serviceUtente.findByID(Utility.getUtente().getId());

		if (optionalUtente.isPresent()) {
			model.addAttribute("user", optionalUtente.get());
		} else {
			throw new BusinessException("Utente non trovato");
		}
		return "common/profilo";
	}

	@PostMapping
	public String modificaProfilo(@ModelAttribute Utente nuovoProfilo) throws BusinessException {
		Utente utente = Utility.getUtente();
		nuovoProfilo.setId(utente.getId());
		serviceUtente.updateProfilo(nuovoProfilo);
		
		return "redirect:/common/profilo";
	}

	@PostMapping("/upload")
	public String changeProfilePicture(@RequestParam("foto") MultipartFile foto) throws BusinessException, IOException {
		Utente utenteCorrente = Utility.getUtente();
		serviceUtente.changeProfilePicture(utenteCorrente.getId(), foto);
		return "redirect:/common/profilo";
	}

	@RequestMapping(value = "/profileImage", method = RequestMethod.GET,
			produces = MediaType.IMAGE_JPEG_VALUE)
	public void showImage(HttpServletResponse response) throws IOException {
		Utente utenteCorrente = Utility.getUtente();
		Optional<Utente> utente = serviceUtente.findByID(utenteCorrente.getId());

		if(utente.isPresent()) {
			if(utente.get().getFoto()!=null) {
				StreamUtils.copy(utente.get().getFoto(), response.getOutputStream());
			} else {
				ClassPathResource res = new ClassPathResource("static/dist/img/user1-128x128.jpg");
				StreamUtils.copy(res.getInputStream(), response.getOutputStream());
			}
		}
	}

	@GetMapping("/diventaCreatore")
	public String diventaCreatoreStart(Model model, Principal principal) {
		if(!Utility.userHasRole("CREATOR")) {
			CreatoreInfo creatoreInfo = new CreatoreInfo();
			model.addAttribute("creatoreInfo", creatoreInfo);
		} else {
			model.addAttribute("utente", Utility.getUtente());
		}
		return "common/diventaCreatore";
	}

	@PostMapping("/diventaCreatoreAction")
	public String diventaCreatore(@Valid @ModelAttribute("creatoreInfo") CreatoreInfo creatoreInfo) throws BusinessException {
		Utente currentUtente = Utility.getUtente();
		serviceUtente.becomeCreatore(currentUtente.getId(), creatoreInfo);

		return "redirect:/common/profilo/diventaCreatore";
	}

	@GetMapping("/cambiopassword")
	public String cambioPasswordStart() {
		return "common/cambioPassword";
	}

	@PostMapping("/cambiopassword")
	public String cambioPassword(@RequestParam String password) throws BusinessException {
		Utente currentUtente = Utility.getUtente();
		serviceSignup.changePassword(currentUtente, password);

		return "redirect:/common/profilo/cambiopassword";
	}

}
