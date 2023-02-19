package it.univaq.disim.mwt.mydemy.presentation;

import it.univaq.disim.mwt.mydemy.business.*;
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
	UtenteService utenteService;
	@Autowired
    CorsoService corsoService;
	@Autowired
	IscrizioneService iscrizioneService;
	@Autowired
	DashboardAdminService dashboardAdminService;

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
		return dashboardAdminService.getVendite();
	}
}
