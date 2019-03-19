package com.example.td5.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerMain {

	// redirige vers l'acceuil

	@GetMapping("/")
	public String frmNew(Model model) {
		return "index/";
	}

}
