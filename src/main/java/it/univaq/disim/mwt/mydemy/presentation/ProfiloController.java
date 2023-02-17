package it.univaq.disim.mwt.mydemy.presentation;

import it.univaq.disim.mwt.mydemy.business.CreatoreInfoService;
import it.univaq.disim.mwt.mydemy.business.RuoloService;
import it.univaq.disim.mwt.mydemy.domain.CreatoreInfo;
import it.univaq.disim.mwt.mydemy.domain.Ruolo;
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

import javax.servlet.ServletException;
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
	private RuoloService serviceRuolo;
	@Autowired
	private CreatoreInfoService serviceCreatore;

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
			serviceUtente.update(utente.get());
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
	public String diventaCreatore(@Valid @ModelAttribute("creatoreInfo") CreatoreInfo creatoreInfo) {
		Utente currentUtente = Utility.getUtente();

		if(!Utility.userHasRole("CREATOR")) {
			Optional<Utente> optionalUtente = serviceUtente.findByID(currentUtente.getId());
			Optional<Ruolo> optionalRuolo = serviceRuolo.findByCode("CREATOR");
			if(optionalUtente.isPresent() && optionalRuolo.isPresent()) {
				Utente utente = optionalUtente.get();
				utente.addRuolo(optionalRuolo.get());
				utente.setCreatoreInfo(creatoreInfo);
				serviceUtente.update(utente);
				Utility.addRole("CREATOR");
			}
		}

		return "redirect:/common/profilo/diventaCreatore";
	}

}
