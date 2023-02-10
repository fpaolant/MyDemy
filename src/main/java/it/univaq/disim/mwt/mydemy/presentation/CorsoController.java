package it.univaq.disim.mwt.mydemy.presentation;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


import it.univaq.disim.mwt.mydemy.business.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.domain.Iscrizione;
import it.univaq.disim.mwt.mydemy.domain.Utente;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/corso")
public class CorsoController {
	@Autowired
	private CorsoBO serviceCorso;
	@Autowired
	private IscrizioneBO serviceIscrizione;
	@Autowired
	private UtenteBO serviceUtente;
	
	@GetMapping(value = {"{id}", "{id}/{flagInsert}"})
	public String index(@PathVariable Long id, @PathVariable(required = false) boolean flagInsert, Principal principal, Model model) {
		serviceCorso.findByID(id).ifPresentOrElse(c -> {
			model.addAttribute("corso", c);
			
			boolean iscritto = false;
			
			if(principal != null) { // logged in
				Utente utente = Utility.getUtente();
				iscritto = serviceIscrizione.findByUtenteAndCorso(utente, c).size() > 0;		
			}
			
			 model.addAttribute("iscritto", iscritto);
			 
			 // numero iscritti al corso
			 int numIscritti = serviceIscrizione.findByCorso(c).size();
			 model.addAttribute("postiRimanenti", c.getPosti() - numIscritti);
			 
			 List<Corso> corsiCreatore = serviceCorso.findByCreatore(c.getCreatore());
			 
			 // numero corsi del creatore
			 int numCorsiCreatore = corsiCreatore.size();
			 model.addAttribute("numCorsiCreatore", numCorsiCreatore);
			 
			 // totale iscritti a tutti i corsi del creatore
			 int totaleIscrittiCreatore = 0;
			 for (Corso corso : corsiCreatore) {
				 totaleIscrittiCreatore += serviceIscrizione.findByCorso(corso).size();				
			 }
			 model.addAttribute("totaleIscrittiCreatore", totaleIscrittiCreatore);
			 
			 System.out.println(flagInsert);
			 model.addAttribute("notificaIscrizioneSuccesso", flagInsert);

			}, () -> {
				System.out.println("corso non trovato");
			}

		);
		return "public/corso/item";
	}
	
	@PostMapping("/iscrivi")
	public String iscrivi(@ModelAttribute("id") Long id, Principal principal) {
		// TO DO controllare il numero dei posti
		boolean[] iscritto = {false};
		String[] iscrizione = { "" };
		
		serviceCorso.findByID(id).ifPresentOrElse(c -> {
			if(principal != null) { // logged in
				Utente utente = Utility.getUtente();
				iscritto[0] = serviceIscrizione.findByUtenteAndCorso(utente, c).size() > 0;
				if(!iscritto[0]) {
					Iscrizione i = new Iscrizione();
					i.setUtente(utente);
					i.setCorso(c);
					serviceIscrizione.save(i);
					iscrizione[0] = "/true";
				}
			}
		}, () -> { // corso non trovato
			iscrizione[0] = "/false";
		});
		return "redirect:/corso/" + id + iscrizione[0];
	}

	@RequestMapping(value = "/creatorImage/{creatorId}", method = RequestMethod.GET,
			produces = MediaType.IMAGE_JPEG_VALUE)
	public void getCreatorImage(@PathVariable Long creatorId, HttpServletResponse response) throws IOException {
		Optional<Utente> utente = serviceUtente.findByID(creatorId);

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
	
}
