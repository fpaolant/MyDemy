package it.univaq.disim.mwt.mydemy.presentation;


import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import it.univaq.disim.mwt.mydemy.business.UtenteBO;
import it.univaq.disim.mwt.mydemy.domain.Ruolo;
import it.univaq.disim.mwt.mydemy.domain.Utente;

@Controller
@RequestMapping("/admin/utenti")
public class UtenteAdminController {
	
	@Autowired
	private UtenteBO serviceUtente;
	@Autowired
	private RuoloBO serviceRuolo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/list")
	public String list() throws BusinessException {
		return "admin/utente/list";
	}
	
	@PostMapping("/findallpaginated")
	public @ResponseBody ResponseGrid<Utente> findAllPaginated(@RequestBody RequestGrid requestGrid) {
		return serviceUtente.findAllPaginated(requestGrid);		
	}
	
	@GetMapping("/create")
	public String createStart(Model model) {
		model.addAttribute("utente", new Utente());
		
		Predicate<Ruolo> isNotUser = r -> !r.getCode().equalsIgnoreCase("USER");
		
		List<Ruolo> ruoli =  serviceRuolo.findAll().stream().filter(isNotUser).collect(Collectors.toList());
		model.addAttribute("ruoli", ruoli);
		return "admin/utente/form";
	}
	
	@PostMapping("/create")
	public String create(@Valid @ModelAttribute("utente") Utente utente, BindingResult result, Errors errors) {
		if(serviceUtente.findByUsername(utente.getUsername()) != null) {
			ObjectError error = new ObjectError("globalError", "Username non disponibile");
	        result.addError(error);
		}
		
		if (errors.hasErrors() || result.hasErrors()) {
			return "admin/ruolo/form";
		}
		if (errors.hasErrors()) {
			return "admin/utente/form";
		}
		// set default user role
		Optional<Ruolo> ruoloUser = serviceRuolo.findByCode("USER");
		if(ruoloUser.isPresent()) utente.addRuolo(ruoloUser.get());
		// encode password
		final String password = passwordEncoder.encode(utente.getPassword());
		utente.setPassword(password);
		
		serviceUtente.save(utente);
		return "redirect:/admin/utenti/list";
	}
	
	@GetMapping("/update")
	public String updateStart(@RequestParam Long id, Model model) {
		Optional<Utente> utente = serviceUtente.findByID(id);
		
		if(utente.isPresent()) {
			Utente u = utente.get();
			u.setPassword("");
			model.addAttribute("utente", u);
		}
		
		Predicate<Ruolo> isNotUser = r -> !r.getCode().equalsIgnoreCase("USER");
		List<Ruolo> ruoli =  serviceRuolo.findAll().stream().filter(isNotUser).collect(Collectors.toList());
		model.addAttribute("ruoli", ruoli);
		return "admin/utente/form";
	}
	
	@PostMapping("/update")
	public String update(@Valid @ModelAttribute("utente") Utente utente, Errors errors) {

		if (errors.hasErrors()) {
			return "admin/utente/form";
		}
		if(!utente.getPassword().equalsIgnoreCase("")) {
			// encode password
			final String password = passwordEncoder.encode(utente.getPassword());
			utente.setPassword(password);
		}
		
		return "admin/utente/form";
	}
	
	@GetMapping("/enable")
	public String enable(@RequestParam Long id) {
		Optional<Utente> utente = serviceUtente.findByID(id);
		
		if(utente.isPresent() && !utente.get().getUsername().equalsIgnoreCase("admin")) {
			Boolean enabled = utente.get().getEnabled();
			utente.get().setEnabled(!enabled);
			serviceUtente.save(utente.get());
		}
		return "redirect:/admin/utenti/list";
	}
	
	@GetMapping("/delete")
	public String deleteStart(@RequestParam Long id, Model model) {
		Optional<Utente> utente = serviceUtente.findByID(id);
		if(utente.isPresent() && !utente.get().getUsername().equalsIgnoreCase("admin")) {
			serviceUtente.delete(utente.get());
		}
		return "redirect:/admin/utenti/list";
	}
	
}
