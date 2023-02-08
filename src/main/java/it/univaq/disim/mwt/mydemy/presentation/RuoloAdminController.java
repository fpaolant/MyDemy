package it.univaq.disim.mwt.mydemy.presentation;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.univaq.disim.mwt.mydemy.business.BusinessException;
import it.univaq.disim.mwt.mydemy.business.RequestGrid;
import it.univaq.disim.mwt.mydemy.business.ResponseGrid;
import it.univaq.disim.mwt.mydemy.business.RuoloBO;
import it.univaq.disim.mwt.mydemy.domain.Ruolo;

@Controller
@RequestMapping("/admin/ruoli")
public class RuoloAdminController {
	
	private final List<String> reservedCodes = List.of("USER", "ADMIN", "CREATOR");
	
	@Autowired
	private RuoloBO serviceRuolo;
	
	@GetMapping("/list")
	public String list() throws BusinessException {
		return "admin/ruolo/list";
	}
	
	@PostMapping("/findallpaginated")
	public @ResponseBody ResponseGrid<Ruolo> findAllPaginated(@RequestBody RequestGrid requestGrid) {
		ResponseGrid<Ruolo> ruoli = serviceRuolo.findAllPaginated(requestGrid); 
		return ruoli;		
	}
	
	@GetMapping("/create")
	public String createStart(Model model) {
		model.addAttribute("ruolo", new Ruolo());
		return "admin/ruolo/form";
	}
	
	@PostMapping("/create")
	public String create(@Valid @ModelAttribute("ruolo") Ruolo ruolo, BindingResult result, Errors errors) {
		if(serviceRuolo.findByCode(ruolo.getCode()).isPresent()) {
			ObjectError error = new ObjectError("globalError", "Il codice è già utilizzato");
	        result.addError(error);
		}
		
		if (errors.hasErrors() || result.hasErrors()) {
			return "admin/ruolo/form";
		}
		
		serviceRuolo.save(ruolo);
		return "redirect:/admin/ruoli/list";
	}
	
	@GetMapping("/update")
	public String updateStart(@RequestParam Long id, Model model) {
		Optional<Ruolo> ruolo = serviceRuolo.findByID(id);
		if(this.reservedCodes.contains(ruolo.get().getCode())) {
			return "redirect:/admin/ruoli/list";
		}
		
		if(ruolo.isPresent()) {
			if(this.reservedCodes.contains(ruolo.get().getCode())) {
				return "redirect:/admin/ruoli/list";
			}
			model.addAttribute("ruolo", ruolo.get());
		}
		return "admin/ruolo/form";
	}
	
	@PostMapping("/update")
	public String update(@Valid @ModelAttribute("ruolo") Ruolo ruolo, BindingResult result, Errors errors) {
		if(this.reservedCodes.contains(ruolo.getCode())) {
			ObjectError error = new ObjectError("globalError", "Il ruolo è riservato e non può essere modificato");
			result.addError(error);
		}
		Optional<Ruolo> or = serviceRuolo.findByCode(ruolo.getCode());
		if(or.isPresent() && (or.get().getId() != ruolo.getId())) {
			ObjectError error = new ObjectError("globalError", "Il codice è già utilizzato");
	        result.addError(error);
		}
		
		if (errors.hasErrors() || result.hasErrors()) {
			return "admin/ruolo/form";
		}
		
		serviceRuolo.save(ruolo);
		return "redirect:/admin/ruoli/list";
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam Long id) {
		Optional<Ruolo> opt = serviceRuolo.findByID(id);
		if(!opt.isPresent()) {
			return "redirect:/admin/ruoli/list";
		}
		Ruolo ruolo = opt.get();
		if(this.reservedCodes.contains(ruolo.getCode())) {
			return "redirect:/admin/ruoli/list";
		}
		serviceRuolo.delete(ruolo);
		return "redirect:/admin/ruoli/list";
	}
	
}
