package it.univaq.disim.mwt.mydemy.presentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import it.univaq.disim.mwt.mydemy.business.BusinessException;
import it.univaq.disim.mwt.mydemy.business.CorsoService;
import it.univaq.disim.mwt.mydemy.repository.CorsoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import it.univaq.disim.mwt.mydemy.business.CategoriaService;
import it.univaq.disim.mwt.mydemy.business.ResponseCategoryItem;
import it.univaq.disim.mwt.mydemy.domain.Categoria;

@Controller
@RequestMapping("/admin/categorie")
public class CategoriaAdminController {
	
	@Autowired
	private CategoriaService serviceCategoria;

	@GetMapping("/list")
	public String list() {
		return "admin/categoria/list";
	}
	
	@RequestMapping(value = "/getTree", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<ResponseCategoryItem> getTree() {
		return serviceCategoria.getTree();
	}
	
	@GetMapping("/create")
	public String createStart(Model model, @RequestParam(required = false) Long parent) {
		Categoria nuova = new Categoria();
		if(parent != null) {
			Optional<Categoria> parentOpt = serviceCategoria.findByID(parent);
			if(parentOpt.isPresent()) {
				nuova.setParent(parentOpt.get());
			}
		}
		model.addAttribute("categoria", nuova);
		List<Categoria> categorie = serviceCategoria.findAll();
		model.addAttribute("categorie", categorie);
		return "admin/categoria/form";
	}
	
	@PostMapping("/create")
	public String create(@Valid @ModelAttribute("categoria") Categoria categoria, Errors errors) {
		if (errors.hasErrors()) {
			return "admin/categoria/form";
		}
		serviceCategoria.create(categoria);
		return "redirect:/admin/categorie/list";
	}
	
	@GetMapping("/update")
	public String updateStart(@RequestParam Long id, Model model) throws BusinessException {
		Optional<Categoria> categoria = serviceCategoria.findByID(id);

		if(categoria.isPresent()) {
			model.addAttribute("categoria", categoria.get());
			List<Categoria> categorie =  serviceCategoria.findAllExceptOne(categoria.get());
			model.addAttribute("categorie", categorie);	
		} else {
			throw new BusinessException("Categoria non trovata");
		}
		return "admin/categoria/form";
	}
	
	@PostMapping("/update")
	public String update(@Valid @ModelAttribute("categoria") Categoria categoria, Errors errors){
		if (errors.hasErrors()) {
			return "admin/categoria/form";
		}
		serviceCategoria.update(categoria);

		return "redirect:/admin/categorie/list";
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam Long id) {
		serviceCategoria.delete(id);
		return "redirect:/admin/categorie/list";
	}

	@GetMapping("/setparent")
	public @ResponseBody void setParent(@RequestParam Long id, @RequestParam Long parentId, HttpServletResponse response) {
		try {
			serviceCategoria.setParent(id, parentId);
		} catch (BusinessException e) {
			response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
		}
	}
	
}
