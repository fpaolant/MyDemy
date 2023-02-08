package it.univaq.disim.mwt.mydemy.presentation;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.univaq.disim.mwt.mydemy.business.CategoriaBO;
import it.univaq.disim.mwt.mydemy.business.CorsoBO;
import it.univaq.disim.mwt.mydemy.domain.Categoria;

@Controller
public class CategoriaController {
	
	@Autowired
	private CorsoBO serviceCorso;
	
	@Autowired
	private CategoriaBO serviceCategoria;
	
	@GetMapping("/categoria/{id}")
	String index(Principal principal, Model model, @PathVariable Long id) {
		
		serviceCategoria.findByID(id).ifPresentOrElse(cat -> {
			 
			 model.addAttribute("categoria", cat);
			 
			 List<Categoria> sottoCategorie = serviceCategoria.findChildCategories(cat);

			 model.addAttribute("sottoCategorie", sottoCategorie);

			 
			}, () -> {
				System.out.println("categoria non trovata");
			
			}
		
		);
		
		return "public/categoria/item";
	}
	
	
	
}
