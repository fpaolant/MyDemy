package it.univaq.disim.mwt.mydemy.presentation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.univaq.disim.mwt.mydemy.business.CategoriaService;
import it.univaq.disim.mwt.mydemy.domain.Categoria;

@Controller
public class CategoriaController {
	@Autowired
	private CategoriaService serviceCategoria;
	@GetMapping("/categoria/{id}")
	String index(Model model, @PathVariable Long id) {
		Optional<Categoria> optCategoria = serviceCategoria.findByID(id);
		if(optCategoria.isPresent()) {
		 	model.addAttribute("categoria", optCategoria.get());
			return "public/categoria/item";
		} else {
			return "redirect:/error";
		}
	}

}
