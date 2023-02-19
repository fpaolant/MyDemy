package it.univaq.disim.mwt.mydemy.presentation;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import it.univaq.disim.mwt.mydemy.business.BusinessException;
import it.univaq.disim.mwt.mydemy.business.CorsoService;
import it.univaq.disim.mwt.mydemy.business.RecensioneService;
import it.univaq.disim.mwt.mydemy.domain.Corso;
import it.univaq.disim.mwt.mydemy.domain.Recensione;
import it.univaq.disim.mwt.mydemy.repository.IscrizioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import it.univaq.disim.mwt.mydemy.business.IscrizioneService;
import it.univaq.disim.mwt.mydemy.domain.Iscrizione;
import it.univaq.disim.mwt.mydemy.domain.Utente;

import javax.validation.Valid;

@Controller
@RequestMapping("/iscrizioni")
public class IscrizioneController {
	
	@Autowired
	private IscrizioneService serviceIscrizione;
	@Autowired
	private CorsoService serviceCorso;
	@Autowired
	private RecensioneService serviceRecensione;


	@GetMapping
	public String index(Principal principal, Model model) {
			if(principal != null) {
				Utente utente = Utility.getUtente();
				List<Iscrizione> iscrizioni = serviceIscrizione.findByUtente(utente);
				model.addAttribute("iscrizioni", iscrizioni);
			} else {
				return "redirect:loginstart.html";
			}
		return "public/iscrizione/index";
	}

	@GetMapping("scriviRecensione")
	public String scriviRecensioneStart(@RequestParam Long corsoId, Model model) throws BusinessException {
		Utente utente = Utility.getUtente();
		Optional<Corso> corso = serviceCorso.findById(corsoId);
		if (corso.isEmpty()) throw new BusinessException("Corso non trovato");
		Optional<Iscrizione> iscrizione = serviceIscrizione.findByUtenteAndCorso(utente, corso.get());
		if(iscrizione.isEmpty()) throw new BusinessException("Non sei iscritto al corso");
		if(!iscrizione.get().getSuperato()) throw new BusinessException("non hai ancora terminato il corso");

		boolean recensionePresente = serviceRecensione.recensito(utente.getId(), corsoId);
		if(!recensionePresente) {
			Recensione nuovaRecensione = new Recensione();
			nuovaRecensione.setCorsoId(corsoId);
			System.out.println("recensione lasciata " + recensionePresente);
			model.addAttribute("corso", corso.get());
			model.addAttribute("recensione", nuovaRecensione);
			model.addAttribute("action_url", "/iscrizioni/scriviRecensione");
			model.addAttribute("cancel_url", "/iscrizioni");
		}
		System.out.println("recensione lasciata " + recensionePresente);
		model.addAttribute("recensionePresente", recensionePresente);
		return "public/iscrizione/nuova_recensione";
	}
	@PostMapping("scriviRecensione")
	public String scriviRecensione(@Valid @ModelAttribute("recensione") Recensione recensione, Errors errors) {
		if (errors.hasErrors()) {
			return "/iscrizioni/scriviRecensione";
		}
		Utente utente = Utility.getUtente();
		recensione.setAutore(utente.getUsername());
		recensione.setAutoreId(utente.getId());
		serviceRecensione.create(recensione);

		return "redirect:/iscrizioni";
	}

	@GetMapping("getCertificato")
	public String getCerificato(@RequestParam Long iscrizioneId, Principal principal, Model model) {
		if(principal != null) { // logged in
			Utente utente = Utility.getUtente();
			Optional<Iscrizione> iscrizione = serviceIscrizione.findById(iscrizioneId);

			if(iscrizione.isPresent() && iscrizione.get().getSuperato() && iscrizione.get().getUtente().equals(utente)) {
				// generate business certificate & download TODO

			}
			// TODO getcertificato
			//model.addAttribute("iscrizioni", iscrizioni);
		} else {
			//model.addAttribute("iscrizioni", new HashSet<Iscrizione>());
		}
		return "public/iscrizione/index";
	}
	
	
	
}
