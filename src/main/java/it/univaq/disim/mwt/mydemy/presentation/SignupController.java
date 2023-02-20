package it.univaq.disim.mwt.mydemy.presentation;

import it.univaq.disim.mwt.mydemy.business.*;
import it.univaq.disim.mwt.mydemy.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class SignupController {

	@Autowired
	private SignupService service;

	
	@GetMapping("/signup")
	public String signUpStart(Model model) {
		Utente u = new Utente();
		model.addAttribute("utente", u);
		
		return "/common/signup";
	}

	@PostMapping("/signup")
	public String signUp(@Valid @ModelAttribute("utente") Utente utente, BindingResult result, Errors errors) {
		if (errors.hasErrors() || result.hasErrors()) {
			return "common/signup";
		}
		if (errors.hasErrors()) {
			return "common/signup";
		}

		try {
			service.signUp(utente);
			return "redirect:/signup/welcome";
		} catch (BusinessException e) {
			ObjectError error = new ObjectError("globalError", "Username non disponibile");
			result.addError(error);
			return "common/signup";
		}
	}

	@GetMapping("/signup/welcome")
	public String signUpWelcome(Model model) {
		Utente u = new Utente();
		model.addAttribute("utente", u);

		return "/common/signupWelcome";
	}
	
}
