package it.univaq.disim.mwt.mydemy.presentation;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.univaq.disim.mwt.mydemy.business.CategoriaService;
import it.univaq.disim.mwt.mydemy.business.CorsoService;
import it.univaq.disim.mwt.mydemy.domain.Categoria;

@Controller
public class HomeController {
	@Autowired
	private CorsoService serviceCorso;
	
	@Autowired
	private CategoriaService serviceCategoria;
	
	@GetMapping("/")
	String index(Principal principal, Model model) {
		List<Categoria> categorie = serviceCategoria.findAllRootCategories(Pageable.unpaged());
        model.addAttribute("categorie", categorie);

		Sort sortCriteria = Sort.by(Sort.Direction.DESC, "inizio");
		PageRequest pageRequest = PageRequest.of(0, 10, sortCriteria);
		model.addAttribute("corsi", serviceCorso.findAllNextCorsi(pageRequest));
		
		return "index";
	}

	
}
