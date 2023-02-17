package it.univaq.disim.mwt.mydemy.presentation;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;


import it.univaq.disim.mwt.mydemy.business.*;
import it.univaq.disim.mwt.mydemy.domain.Corso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import it.univaq.disim.mwt.mydemy.domain.Utente;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/corso")
public class CorsoController {
	@Autowired
	private CorsoService serviceCorso;
	@Autowired
	private IscrizioneService serviceIscrizione;
	@Autowired
	private UtenteService serviceUtente;
	
	@GetMapping(value = {"{id}", "{id}/{flagInsert}"})
	public String index(@PathVariable Long id, @PathVariable(required = false) boolean flagInsert, Principal principal, Model model) {
		Optional<Corso> optCorso = serviceCorso.findById(id);
		if (optCorso.isPresent()) {
			Corso corso = optCorso.get();
			model.addAttribute("corso", corso);

			boolean iscritto = false;
			boolean isCreatore = false;

			if(principal != null) { // logged in
				Utente utente = Utility.getUtente();
				iscritto = serviceIscrizione.findByUtenteAndCorso(utente, corso).size() > 0;
				// utente e creatore sono la stessa persona
				if(corso.getCreatore().equals(utente)) {
					isCreatore = true;
				}
			}
			model.addAttribute("iscritto", iscritto);
			model.addAttribute("isCreatore", isCreatore);

			// numero iscritti al corso
			int numIscritti = corso.getIscrizioni().size();
			model.addAttribute("postiRimanenti", corso.getPosti() - numIscritti);

			// numero corsi del creatore
			int numCorsiCreatore = serviceCorso.findByCreatore(corso.getCreatore()).size();
			model.addAttribute("numCorsiCreatore", numCorsiCreatore);

			// totale iscritti a tutti i corsi del creatore
			model.addAttribute("totaleIscrittiCreatore", serviceIscrizione.count(corso.getCreatore()));

			model.addAttribute("notificaIscrizioneSuccesso", flagInsert);
		}

		return "public/corso/item";
	}
	@PostMapping("/iscrivi")
	public String iscrivi(@ModelAttribute("id") Long id) {
		try {
			serviceCorso.iscrivi(id, Utility.getUtente());
			return "redirect:/corso/" + id + "/true";
		} catch (BusinessException e) {
			return "redirect:/corso/" + id + "/false";
		}
	}
	@RequestMapping(value = "/creatorImage/{creatorId}", method = RequestMethod.GET,
			produces = MediaType.IMAGE_JPEG_VALUE)
	public void getCreatorImage(@PathVariable Long creatorId, HttpServletResponse response) throws IOException {
		Optional<Utente> utente = serviceUtente.findByID(creatorId);

		if(utente.isPresent()) {
			if(utente.get().getFoto()!=null) {
				StreamUtils.copy(utente.get().getFoto(), response.getOutputStream());
			} else {
				ClassPathResource res = new ClassPathResource("static/dist/img/user1-128x128.jpg");
				StreamUtils.copy(res.getInputStream(), response.getOutputStream());
			}
		}
	}
	
}
