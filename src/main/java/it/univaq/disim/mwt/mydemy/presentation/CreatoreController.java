package it.univaq.disim.mwt.mydemy.presentation;

import it.univaq.disim.mwt.mydemy.business.*;
import it.univaq.disim.mwt.mydemy.business.impl.jpa.IscrizioneServiceImpl;
import it.univaq.disim.mwt.mydemy.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/creatore")
public class CreatoreController {
	
	@Autowired
	private CorsoService serviceCorso;
	@Autowired
	private CategoriaService serviceCategoria;
	@Autowired
	private TagService serviceTag;
	@Autowired
	private IscrizioneServiceImpl serviceIscrizione;
	@Autowired
	private UtenteService utenteService;
	@Autowired
	private IscrizioneService iscrizioneService;
	@Autowired
	private DashboardCreatoreService dashboardCreatoreService;
	@Autowired
	private CreatoreInfoService creatoreInfoService;

	@GetMapping("/index")
	public String index(Principal principal, Model model) throws BusinessException {
		Utente creatore = Utility.getUtente();

		// Iscritti del creatore
		Long iscrizioniCount = iscrizioneService.count(creatore);
		model.addAttribute("iscrizioniCount", iscrizioniCount);
		model.addAttribute("usersCount", iscrizioniCount);
		// Percentuale di superato sul numero di icritti
		float percSuperato = iscrizioneService.getPercentualeSuperato(creatore);
		model.addAttribute("percSuperato", String.format("%.0f%%",percSuperato));
		// numero di corsi del creatore
		int corsiCount = serviceCorso.findByCreatore(creatore).size();
		model.addAttribute("corsiCount", corsiCount);

		return "creatore/dashboard";
	}

	@RequestMapping(value = "/getVendite", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponsePieData getVendite() {
		return dashboardCreatoreService.getVendite(Utility.getUtente());
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
		serviceCorso.proponi(corso,creatore);

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
		} else throw new BusinessException("Corso non trovato");

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
		Optional<Utente> optUtente = utenteService.findByID(utenteSessione.getId());
		if(optUtente.isEmpty()) throw new BusinessException();
		Utente utente = optUtente.get();
		model.addAttribute("utente", utente);

		return "creatore/profilo";
	}

	@PostMapping("/profilo")
	public String modificaProfilo(@RequestParam String titolo, @RequestParam String descrizione) throws BusinessException {
		Utente utente = Utility.getUtente();
		creatoreInfoService.updateProfilo(utente, titolo, descrizione);

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
	public @ResponseBody ResponseGrid<Iscrizione> findAllIscrizioniByCorsoPaginated(@RequestParam Long id, @RequestBody RequestGrid requestGrid) throws BusinessException {
		return serviceIscrizione.findAllByCorsoCreatorePaginated(id, Utility.getUtente(), requestGrid);
	}

	@GetMapping("/setSuperato")
	public String setSuperato(@RequestParam Long iscrizioneId) throws BusinessException {
		Iscrizione iscrizione = serviceCorso.setSuperato(iscrizioneId);
		return "redirect:/creatore/iscritti?id="+iscrizione.getCorso().getId();
	}
	
}
