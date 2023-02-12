package it.univaq.disim.mwt.mydemy.presentation;

import it.univaq.disim.mwt.mydemy.business.*;
import it.univaq.disim.mwt.mydemy.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;

@Controller
public class SignupController {
	
	@Autowired
	private UtenteBO serviceUtente;

	@Autowired
	private RuoloBO serviceRuolo;

	@Autowired
	private PasswordEncoder passwordEncoder;


	
	@GetMapping("/signup")
	public String signUpStart(Model model) {
		Utente u = new Utente();
		model.addAttribute("utente", u);
		
		return "/common/signup";
	}

	@PostMapping("/signup")
	public String create(@Valid @ModelAttribute("utente") Utente utente, BindingResult result, Errors errors) {
		if(serviceUtente.findByUsername(utente.getUsername()) != null) {
			ObjectError error = new ObjectError("globalError", "Username non disponibile");
			result.addError(error);
		}

		if (errors.hasErrors() || result.hasErrors()) {
			return "common/signup";
		}
		if (errors.hasErrors()) {
			return "common/signup";
		}
		// set default user role
		utente.setRuoli(new HashSet<>());
		Optional<Ruolo> ruoloUser = serviceRuolo.findByCode("USER");
		if(ruoloUser.isPresent()) utente.addRuolo(ruoloUser.get());

		// encode password
		final String password = passwordEncoder.encode(utente.getPassword());
		utente.setPassword(password);

		serviceUtente.save(utente);
		return "redirect:/signup/welcome";
	}

	@GetMapping("/signup/welcome")
	public String signUpWelcome(Model model) {
		Utente u = new Utente();
		model.addAttribute("utente", u);

		return "/common/signupWelcome";
	}
	
	
	
}
