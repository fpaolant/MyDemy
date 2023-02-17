package it.univaq.disim.mwt.mydemy.presentation;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.univaq.disim.mwt.mydemy.business.IscrizioneService;
import it.univaq.disim.mwt.mydemy.domain.Iscrizione;
import it.univaq.disim.mwt.mydemy.domain.Utente;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/iscrizioni")
public class IscrizioneController {
	
	@Autowired
	private IscrizioneService serviceIscrizione;
	
	
	@GetMapping
	public String index(Principal principal, Model model) {
			if(principal != null) { // logged in
				Utente utente = Utility.getUtente();
				List<Iscrizione> iscrizioni = serviceIscrizione.findByUtente(utente);

				model.addAttribute("iscrizioni", iscrizioni);
			} else {
				model.addAttribute("iscrizioni", new HashSet<Iscrizione>());
			}
		return "public/iscrizione/index";
	}

	@GetMapping("getCertificato")
	public String getCerificato(@RequestParam Long iscrizioneId, Principal principal, Model model) {
		if(principal != null) { // logged in
			Utente utente = Utility.getUtente();
			Optional<Iscrizione> iscrizione = serviceIscrizione.findById(iscrizioneId);

			if(iscrizione.isPresent() && iscrizione.get().getSuperato() && iscrizione.get().getUtente().equals(utente)) {
				// generate business certificate & download TODO

			}

			//model.addAttribute("iscrizioni", iscrizioni);
		} else {
			//model.addAttribute("iscrizioni", new HashSet<Iscrizione>());
		}
		return "public/iscrizione/index";
	}
	
	
	
}
