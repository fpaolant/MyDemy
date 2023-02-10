package it.univaq.disim.mwt.mydemy.presentation;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import it.univaq.disim.mwt.mydemy.business.*;
import it.univaq.disim.mwt.mydemy.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/admin/corsi")
public class CorsoAdminController {
	
	@Autowired
	private CorsoBO serviceCorso;
	@Autowired
	private UtenteBO serviceUtente;
	@Autowired
	private RuoloBO serviceRuolo;
	@Autowired
	private CategoriaBO serviceCategoria;
	@Autowired
	private TagBo serviceTag;
	
	
	
	@GetMapping("/list")
	public String list() throws BusinessException {
		return "admin/corso/list";
	}
	
	@PostMapping("/findallpaginated")
	public @ResponseBody ResponseGrid<Corso> findAllPaginated(@RequestBody RequestGrid requestGrid) {
		ResponseGrid<Corso> rgc = serviceCorso.findAllPaginated(requestGrid);
		return rgc;
	}

	@GetMapping("/findallpaginated")
	public @ResponseBody ResponseGrid<Corso> findAllPaginatedget(@RequestBody RequestGrid requestGrid) {
		ResponseGrid<Corso> rgc = serviceCorso.findAllPaginated(requestGrid);
		return rgc;
	}
	
	@GetMapping("/create")
	public String createStart(Model model) {
		Corso c = new Corso();
		model.addAttribute("corso", c);
		Optional<Ruolo> optionalRuolo = serviceRuolo.findByCode("CREATOR");
		List<Utente> creatori = serviceUtente.findAllByRole(optionalRuolo.get());
		model.addAttribute("creatori", creatori);
		List<Tag> tags = serviceTag.findAll();
		model.addAttribute("tags", tags);
		List<Categoria> categorie = serviceCategoria.findAll();
		model.addAttribute("cats", categorie);
		
		return "/admin/corso/form";
	}
	
	@PostMapping("/create")
	public String create(@Valid @ModelAttribute("corso") Corso corso, Errors errors) {
		if (errors.hasErrors()) {
			return "/admin/corso/form";
		}
		serviceCorso.save(corso);
		return "redirect:/admin/corsi/list";
	}
	
	@GetMapping("/update")
	public String updateStart(@RequestParam Long id, Model model) {
		Optional<Corso> corso = serviceCorso.findByID(id);
		if(corso.isPresent()) {
			Corso c = corso.get();
			model.addAttribute("corso", c);
			Optional<Ruolo> optionalRuolo = serviceRuolo.findByCode("CREATOR");
			List<Utente> creatori = serviceUtente.findAllByRole(optionalRuolo.get());;
			model.addAttribute("creatori", creatori);
			List<Tag> tags = serviceTag.findAll();
			model.addAttribute("tags", tags);
			List<Categoria> categorie = serviceCategoria.findAll();
			model.addAttribute("cats", categorie);
		}
		
		return "/admin/corso/form";
	}
		
	@PostMapping("/update")
	public String update(@Valid @ModelAttribute("corso") Corso corso, Errors errors) {
		if (errors.hasErrors()) {
			return "/admin/corso/form";
		}
		Corso corsoOld = serviceCorso.findByID(corso.getId()).get();
		corso.setVersion(corsoOld.getVersion());
		serviceCorso.update(corso);

		return "redirect:/admin/corsi/list";
	}
	
	@GetMapping("/delete")
	public String deleteStart(@RequestParam Long id, Model model) {
		Optional<Corso> corso = serviceCorso.findByID(id);
		model.addAttribute("corso", corso);
		return "/admin/corso/form";
	}
	
	@PostMapping("/delete")
	public String delete(@ModelAttribute("corso") Corso corso, Errors errors) {
		serviceCorso.delete(corso);
		return "redirect:/admin/corsi/list";
	}
	
	@GetMapping("/approve")
	public String approve(@RequestParam Long id) {
		Optional<Corso> corso = serviceCorso.findByID(id);
		
		if(corso.isPresent()) {		
			Boolean enabled = corso.get().getApprovato();
			corso.get().setApprovato(!enabled);
			serviceCorso.save(corso.get());
		}
		return "redirect:/admin/corsi/list";
	}
	
}
