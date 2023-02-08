package it.univaq.disim.mwt.mydemy.presentation;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import it.univaq.disim.mwt.mydemy.business.BusinessException;
import it.univaq.disim.mwt.mydemy.business.CategoriaBO;
import it.univaq.disim.mwt.mydemy.business.CorsoBO;
import it.univaq.disim.mwt.mydemy.business.RequestGrid;
import it.univaq.disim.mwt.mydemy.business.ResponseGrid;
import it.univaq.disim.mwt.mydemy.domain.Categoria;
import it.univaq.disim.mwt.mydemy.domain.Corso;

@Controller
public class HomeController {
	
	@Autowired
	private CorsoBO serviceCorso;
	
	@Autowired
	private CategoriaBO serviceCategoria;
	
	@GetMapping("/")
	String index(Principal principal, Model model) {
		List<Categoria> categorie = serviceCategoria.findAllRootCategories();
        List <Categoria> cfiltered = categorie.stream().filter(cat -> "senza categoria".compareTo(cat.getNome()) > 0).collect(Collectors.toList());
		model.addAttribute("categorie", cfiltered);

		Sort sortCriteria = Sort.by(Sort.Direction.DESC, "inizio");
		PageRequest pageRequest = PageRequest.of(0, 10, sortCriteria);
		model.addAttribute("corsi", serviceCorso.findAllNextCorsi(pageRequest).getContent());
		
		return "index";
	}
		
	@PostMapping("/corsidisponibili")
	public @ResponseBody ResponseGrid<Corso> findAllPaginated(@RequestBody RequestGrid requestGrid) throws BusinessException {		
		return serviceCorso.findAllPaginated(requestGrid);		
	}
	
}
