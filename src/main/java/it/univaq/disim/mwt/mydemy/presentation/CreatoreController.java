package it.univaq.disim.mwt.mydemy.presentation;

import it.univaq.disim.mwt.mydemy.business.*;
import it.univaq.disim.mwt.mydemy.business.impl.jpa.IscrizioneBOImpl;
import it.univaq.disim.mwt.mydemy.domain.*;
import it.univaq.disim.mwt.mydemy.repository.IscrizioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/creatore")
public class CreatoreController {
	
	@Autowired
	private CorsoBO serviceCorso;
	@Autowired
	private CategoriaBO serviceCategoria;
	@Autowired
	private TagBo serviceTag;
	@Autowired
	private IscrizioneBOImpl serviceIscrizione;

	@Autowired
	private UtenteBO serviceUtente;
	@Autowired
	private IscrizioneRepository iscrizioneRepository;

	@GetMapping("/index")
	public String index(Principal principal, Model model) throws BusinessException {
		// to do

		return "creatore/dashboard";
	}

	@GetMapping("/corsi/proponi")
	public String proponiStart(Principal principal, Model model) throws BusinessException {
		Corso c = new Corso();
		model.addAttribute("corso", c);
		List<Tag> tags = serviceTag.findAll();
		model.addAttribute("tags", tags);
		List<Categoria> categorie = serviceCategoria.findAll();
		model.addAttribute("cats", categorie);
		
		return "creatore/proponi";
	}

	@PostMapping("/corsi/proponi")
	public String proponi(@ModelAttribute Corso corso) throws BusinessException {
		// to do
		Utente creatore = Utility.getUtente();
		corso.setCreatore(creatore);
		corso.setApprovato(false);
		serviceCorso.save(corso);

		return "redirect:/creatore/corsi/list";
	}

	@GetMapping("/corsi/update")
	public String updateStart(@RequestParam Long id, Model model) throws BusinessException {
		Optional<Corso> corso = serviceCorso.findByIdAndCreatore(id, Utility.getUtente());
		if(corso.isPresent()) {
			Corso c = corso.get();
			model.addAttribute("corso", c);
			List<Tag> tags = serviceTag.findAll();
			model.addAttribute("tags", tags);
			List<Categoria> categorie = serviceCategoria.findAll();
			model.addAttribute("cats", categorie);
		}

		return "/creatore/modifica_corso";
	}

	@PostMapping("/corsi/update")
	public String update(@Valid @ModelAttribute("corso") Corso corso, Errors errors) throws BusinessException {
		if (errors.hasErrors()) {
			return "/creatore/modifica_corso";
		}
		Corso corsoOld = serviceCorso.findByIdAndCreatore(corso.getId(), Utility.getUtente()).get();
		corso.setCreatore(Utility.getUtente());
		corso.setVersion(corsoOld.getVersion());
		serviceCorso.update(corso);

		return "redirect:/creatore/corsi/list";
	}

	@GetMapping("/profilo")
	public String modificaProfiloStart(Model model) throws BusinessException {
		Utente utenteSessione = Utility.getUtente();
		Utente utente = serviceUtente.findByID(utenteSessione.getId()).get();
		model.addAttribute("utente", utente);

		return "creatore/profilo";
	}

	@PostMapping("/profilo")
	public String modificaProfilo(@RequestParam String titolo, @RequestParam String descrizione) {
		Utente utenteSessione = Utility.getUtente();
		Utente utente = serviceUtente.findByID(utenteSessione.getId()).get();

		CreatoreInfo ci = utente.getCreatoreInfo();
		if(ci != null) {
			ci.setTitolo(titolo);
			ci.setDescrizione(descrizione);
		} else {
			ci = new CreatoreInfo(titolo, descrizione);
			utente.setCreatoreInfo(ci);
		}
		serviceUtente.save(utente);

		return "redirect:/creatore/profilo";
	}

	@GetMapping("/corsi/list")
	public String corsi(Model model) throws BusinessException {
		Utente creatore = Utility.getUtente();
		List<Corso> corsi = serviceCorso.findByCreatore(creatore);
		model.addAttribute("corsi", corsi);

		return "creatore/corsi";
	}

	@PostMapping("/findallcorsipaginated")
	public @ResponseBody ResponseGrid<Corso> findAllCorsiPaginated(@RequestBody RequestGrid requestGrid) {
		Utente creatore = Utility.getUtente();
		return serviceCorso.findAllByCreatorePaginated(creatore, requestGrid);
	}

	@GetMapping("/iscritti")
	public String iscritti(@RequestParam Long id, Model model) throws BusinessException {
		Utente creatore = Utility.getUtente();
		Optional<Corso> corso = serviceCorso.findByIdAndCreatore(id, creatore);
		if (corso.isPresent()) {
			model.addAttribute("corso", corso.get());
		}
		return "creatore/iscritti";
	}

	@PostMapping("/findallIscrizioniByCorsoPaginated")
	public @ResponseBody ResponseGrid<Iscrizione> findAllIscrizioniByCorsoPaginated(@RequestParam Long id, @RequestBody RequestGrid requestGrid) {
		Utente creatore = Utility.getUtente();
		Optional<Corso> corso = serviceCorso.findByIdAndCreatore(id, creatore);

		if(corso.isPresent()) {
			ResponseGrid<Iscrizione> iscrizioni = serviceIscrizione.findAllByCorsoPaginated(corso.get(), requestGrid);
			return iscrizioni;
		}

		ResponseGrid<Iscrizione> iscrizioniEmpty = new ResponseGrid<>(requestGrid.getDraw(), 0, 0, new ArrayList<Iscrizione>());
		return iscrizioniEmpty;
	}

	@GetMapping("/setSuperato")
	public String approve(@RequestParam Long iscrizioneId) {
		Optional<Iscrizione> iscrizioneOpt = serviceIscrizione.findById(iscrizioneId);

		if(iscrizioneOpt.isPresent()) {
			Iscrizione iscrizione = iscrizioneOpt.get();
			iscrizione.setSuperato(true);
			iscrizioneRepository.save(iscrizione);
			// todo invia certificato
			return "redirect:/creatore/iscritti?id="+iscrizione.getCorso().getId();
		}
		return "redirect:/creatore/corsi/list";
	}





	

	
	
	
}
