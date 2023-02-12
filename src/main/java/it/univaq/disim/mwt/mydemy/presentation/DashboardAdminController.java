package it.univaq.disim.mwt.mydemy.presentation;

import it.univaq.disim.mwt.mydemy.business.*;
import it.univaq.disim.mwt.mydemy.domain.Corso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class DashboardAdminController {

	@Autowired
	UtenteBO utenteService;
	@Autowired
	CorsoBO corsoService;
	@Autowired
	IscrizioneBO iscrizioneService;

	@GetMapping("/index")
	String index(Model model) {
		Long usersCount = utenteService.count();
		model.addAttribute("usersCount", usersCount);

		Long iscrizioniCount = iscrizioneService.count();
		model.addAttribute("iscrizioniCount", iscrizioniCount);

		float percSuperato = iscrizioneService.getPercentualeSuperatoTotale();
		model.addAttribute("percSuperato", String.format("%.0f%%",percSuperato));

		Long corsiCount = corsoService.count();
		model.addAttribute("corsiCount", corsiCount);

		return "admin/dashboard/index";
	}

	@RequestMapping(value = "/getVendite", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponsePieData getVendite() {
		ResponsePieData entity = new ResponsePieData();

		ResponsePieDataDataset ds = new ResponsePieDataDataset();
		entity.addDataset(ds);

		List<Corso> corsi = corsoService.findAllCorsiApprovati();
		corsi.stream().forEach(c->{
			entity.addLabel(c.getTitolo());
			ds.addData(iscrizioneService.count(c));
		});

		return entity;
	}
		

	
}
