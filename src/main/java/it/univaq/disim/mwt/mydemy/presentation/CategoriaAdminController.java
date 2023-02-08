package it.univaq.disim.mwt.mydemy.presentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.Valid;

import it.univaq.disim.mwt.mydemy.repository.CategoriaRepository;
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

import it.univaq.disim.mwt.mydemy.business.BusinessException;
import it.univaq.disim.mwt.mydemy.business.CategoriaBO;
import it.univaq.disim.mwt.mydemy.business.ResponseCategoryItem;
import it.univaq.disim.mwt.mydemy.domain.Categoria;

@Controller
@RequestMapping("/admin/categorie")
public class CategoriaAdminController {
	
	@Autowired
	private CategoriaBO serviceCategoria;
	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private CorsoRepository corsoRepository;

	@GetMapping("/list")
	public String list() {
		return "admin/categoria/list";
	}
	
	@RequestMapping(value = "/getTree", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<ResponseCategoryItem> getTree() {
		List<ResponseCategoryItem> entities = new ArrayList<>();
		List<Categoria> categorie = serviceCategoria.findAll();
	    for (Categoria c : categorie) {
	    	long pid = (c.getParent()!=null)? c.getParent().getId(): 0;
	        entities.add(new ResponseCategoryItem(c.getId(), pid, c.getNome(), serviceCategoria.getLevel(c, categorie)));
	    }
	    return entities;		
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
		
		serviceCategoria.save(categoria);
		return "redirect:/admin/categorie/list";
	}
	
	@GetMapping("/update")
	public String updateStart(@RequestParam Long id, Model model) {
		Optional<Categoria> categoria = serviceCategoria.findByID(id);

		if(categoria.isPresent()) {
			model.addAttribute("categoria", categoria.get());
			// get selectable parent categories
			Predicate<Categoria> isNotCurrent = c -> (c.getId()!=categoria.get().getId());
			List<Categoria> categorie =  serviceCategoria.findAll().stream().filter(isNotCurrent).collect(Collectors.toList());
			model.addAttribute("categorie", categorie);	
		}

		return "admin/categoria/form";
	}
	
	@PostMapping("/update")
	public String update(@Valid @ModelAttribute("categoria") Categoria categoria, Errors errors){
		if (errors.hasErrors()) {
			return "admin/categoria/form";
		}
		serviceCategoria.save(categoria);
		return "redirect:/admin/categorie/list";
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam Long id) {
		Optional<Categoria> categoria = serviceCategoria.findByID(id);
		if(categoria.isPresent()) {
			Categoria cat = categoria.get();
			cat.getCorsi().forEach(c -> {
				c.removeCategoria(id);
				corsoRepository.save(c);
			});
			serviceCategoria.delete(categoria.get());
		}
		return "redirect:/admin/categorie/list";
	}

	@GetMapping("/setparent")
	public @ResponseBody String deleteStart(@RequestParam Long id, @RequestParam Long parentId) {
		Optional<Categoria> opt = serviceCategoria.findByID(id);
		if(opt.isPresent()) {
			Categoria categoria = opt.get();

			Optional<Categoria> optParent = serviceCategoria.findByID(parentId);
			if(optParent.isPresent()) {
				categoria.setParent(optParent.get());
				serviceCategoria.save(categoria);
			}
		}
		return "";
	}
	
}
