package it.univaq.disim.mwt.mydemy.presentation;

import it.univaq.disim.mwt.mydemy.business.*;
import it.univaq.disim.mwt.mydemy.domain.Categoria;
import it.univaq.disim.mwt.mydemy.domain.Corso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cerca")
public class CercaController {

    @Autowired
    private CorsoBO serviceCorso;
    @Autowired
    private CategoriaBO serviceCategoria;
    @Autowired
    private TagBo serviceTag;

    @GetMapping("/")
    public String findEnhancedStart(Model model) {
        List<Categoria> categorie = serviceCategoria.findAll();
        model.addAttribute("categorie", categorie);

        return "common/risultati-ricerca";
    }

    @PostMapping("/")
    public String find(@RequestParam String searchString, Model model) {
        model.addAttribute("searchString", searchString);
        List<Corso> corsi = serviceCorso.findByTitoloContainingIgnoreCase(searchString, PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, "titolo"));
        model.addAttribute("corsi", corsi);
        List<Categoria> categorie = serviceCategoria.findAll();
        model.addAttribute("categorie", categorie);

        return "common/risultati-ricerca";
    }

    @PostMapping("/enhanced")
    public String findEnhanced(@RequestParam String searchString, @RequestParam Long categoriaId, @RequestParam String orderBy, @RequestParam String sortOrder, Model model) {
        model.addAttribute("searchString", searchString);

        List<Categoria> categorie = serviceCategoria.findAll();
        model.addAttribute("categorie", categorie);
        model.addAttribute("categoriaId", categoriaId);
        List<Corso> corsi;
        Optional<Categoria> optionalCategoria = serviceCategoria.findByID(categoriaId);
        if (optionalCategoria.isPresent()) {
            corsi = serviceCorso.findAllCriteriaInCategoria(
                    optionalCategoria.get(), searchString, PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.fromString(sortOrder), orderBy));
        } else {
            corsi = serviceCorso.findAllCriteria(searchString, PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, orderBy));
        }
        model.addAttribute("corsi", corsi);
        return "common/risultati-ricerca";
    }


}
