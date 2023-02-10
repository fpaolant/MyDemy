package it.univaq.disim.mwt.mydemy.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class DashboardAdminController {

	
	@GetMapping("/index")
	String index(Model model) {

		return "admin/dashboard/index";
	}
		

	
}
