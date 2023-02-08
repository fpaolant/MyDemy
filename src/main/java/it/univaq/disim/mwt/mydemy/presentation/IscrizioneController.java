package it.univaq.disim.mwt.mydemy.presentation;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.univaq.disim.mwt.mydemy.business.BusinessException;
import it.univaq.disim.mwt.mydemy.business.IscrizioneBO;
import it.univaq.disim.mwt.mydemy.domain.Iscrizione;
import it.univaq.disim.mwt.mydemy.domain.Utente;

@Controller
@RequestMapping("/iscrizioni")
public class IscrizioneController {
	
	@Autowired
	private IscrizioneBO serviceIscrizione;
	
	
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
	
	
	
}
