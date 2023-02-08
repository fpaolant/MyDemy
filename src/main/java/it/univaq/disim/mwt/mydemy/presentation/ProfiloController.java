package it.univaq.disim.mwt.mydemy.presentation;

import it.univaq.disim.mwt.mydemy.business.CreatoreInfoBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import it.univaq.disim.mwt.mydemy.business.BusinessException;
import it.univaq.disim.mwt.mydemy.business.UtenteBO;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/common/profilo")
public class ProfiloController {

	@Autowired
	private UtenteBO serviceUtente;
	@Autowired
	private CreatoreInfoBO serviceCreatore;

	@GetMapping
	public String modificaProfiloStart(Model model) {
		Utente utenteSessione = Utility.getUtente();
		Utente utente = serviceUtente.findByUsername(utenteSessione.getUsername());
		
		model.addAttribute("user", utente);

		return "common/profilo";
	}

	@PostMapping
	public String modificaProfilo(@ModelAttribute Utente nuovoProfilo) {
		Utente utente = Utility.getUtente();
		nuovoProfilo.setId(utente.getId());
		serviceUtente.updateProfilo(nuovoProfilo);
		
		return "redirect:/common/profilo";
	}

	@PostMapping("/upload")
	public String changeProfilePicture(@RequestParam("foto") MultipartFile foto) throws IOException, IOException {
		Utente utenteCorrente = Utility.getUtente();
		Optional<Utente> utente = serviceUtente.findByID(utenteCorrente.getId());
		if(utente.isPresent()) {
			utente.get().setFoto(foto.getBytes());
			serviceUtente.save(utente.get());
		}

		return "redirect:/common/profilo";
	}

	@RequestMapping(value = "/profileImage", method = RequestMethod.GET,
			produces = MediaType.IMAGE_JPEG_VALUE)
	public void showImage(HttpServletResponse response)
			throws ServletException, IOException {
		Utente utenteCorrente = Utility.getUtente();
		Optional<Utente> utente = serviceUtente.findByID(utenteCorrente.getId());

		if(utente.isPresent()) {
			if(utente.get().getFoto()!=null) {
				System.out.println(utente.get().getFoto().length);
				StreamUtils.copy(utente.get().getFoto(), response.getOutputStream());
			} else {
				ClassPathResource res = new ClassPathResource("static/dist/img/user1-128x128.jpg");
				StreamUtils.copy(res.getInputStream(), response.getOutputStream());
			}
		}
	}

	@GetMapping("/diventaCreatore")
	public String diventaCreatoreStart(Model model) {
		/*boolean isCreatore = (Utility.getCreatore() != null);
		model.addAttribute("isCreatore", isCreatore);
*/
		return "public/diventaCreatore";
	}

	@GetMapping("/diventaCreatoreAction")
	public String diventaCreatore(Model model) {
		Utente currentUtente = Utility.getUtente();



		return "public/diventaCreatore";
	}

}
