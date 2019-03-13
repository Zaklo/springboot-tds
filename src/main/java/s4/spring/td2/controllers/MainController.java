package s4.spring.td2.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import s4.spring.td2.entities.Organization;

@Controller
public class MainController {
	
	/*
	 * redirection vers accueil
	 */
	@GetMapping("/")
	public String frmNew(Model model) {
		return "orgas/";
	}
	
}
