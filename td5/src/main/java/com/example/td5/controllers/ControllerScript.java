package com.example.td5.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.example.td5.entities.Category;
import com.example.td5.entities.Language;
import com.example.td5.entities.Script;
import com.example.td5.entities.User;
import com.example.td5.repositories.CategoryRepository;
import com.example.td5.repositories.HistoryRepository;
import com.example.td5.repositories.LanguageRepository;
import com.example.td5.repositories.ScriptRepository;
import com.example.td5.repositories.UserRepository;

@Controller
public class ControllerScript {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private LanguageRepository languageRepository;

	@Autowired
	private ScriptRepository scriptRepository;

	@Autowired
	private UserRepository userRepository;

	private User user;

	@RequestMapping("/createAll")
	@ResponseBody
	public String createAll() {
		User u = new User();
		u.setLogin("antoine");
		u.setPassword("antoine");
		u.setIdentity("Antoine Lozach");
		u.setEmail("alozach@gmail.com");
		userRepository.save(u);

		Category cat = new Category();
		cat.setName("Script1");
		categoryRepository.save(cat);

		Language c = new Language();
		c.setName("C#");

		Language java = new Language();
		java.setName("JAVA");
		languageRepository.save(c);
		languageRepository.save(java);

		return "ok";
	}

	@RequestMapping("/login")
	public String loginPage() {
		return "login";
	}

	@PostMapping("loginPost")
	public RedirectView loginPost(HttpServletRequest request) {

		List<User> users = userRepository.findAll();

		User connection = new User();
		connection.setLogin(request.getParameter("login"));
		connection.setPassword(request.getParameter("password"));

		for (User u : users) {
			if (u.getLogin().equals(connection.getLogin()) && u.getPassword().equals(connection.getPassword())) {
				user = u;

				List<Script> allScripts = scriptRepository.findAll();
				List<Script> usersScript = new ArrayList<>();

				for (Script s : allScripts) {
					if (s.getUser().getId() == user.getId())
						usersScript.add(s);
				}
				user.setScripts(usersScript);

				return new RedirectView("index");
			}
		}

		return new RedirectView("login");
	}

	@RequestMapping("/logout")
	public RedirectView logout() {
		user = null;
		return new RedirectView("index");
	}

	@RequestMapping("/index")
	public String index(ModelMap model) {
		if (user != null) {
			model.addAttribute("user", user);
			return "index";
		}
		return "login";
	}

	@RequestMapping("/script/new")
	public String scriptNew(ModelMap model) {
		if (user != null) {
			List<Category> categories = categoryRepository.findAll();

			List<Language> languages = languageRepository.findAll();

			model.addAttribute("categories", categories);
			model.addAttribute("languages", languages);
			return "script_new";
		}
		return "non_connected";
	}

	@PostMapping("/script/submit")
	public RedirectView addScript(Script script) {
		if (user != null) {
			script.setUser(user);
			scriptRepository.save(script);

			return new RedirectView("../index");
		}
		return new RedirectView("../non_connected");
	}

	@RequestMapping("/script/{id}")
	@GetMapping
	public String scriptEdit(@PathVariable("id") int id, ModelMap model) {

		if (user == null)
			return "non_connected";

		Optional<Script> opt = scriptRepository.findById(id);

		if (opt.isPresent()) {
			Script s = opt.get();
			if (s.getUser().getId() == user.getId()) {
				model.addAttribute("script", s);

				List<Category> categories = categoryRepository.findAll();

				List<Language> languages = languageRepository.findAll();

				Category selectedCategory = s.getCategory();
				categories.remove(selectedCategory);
				model.addAttribute("selectedCategory", selectedCategory);

				Language selectedLanguage = s.getLanguage();
				languages.remove(selectedLanguage);
				model.addAttribute("selectedLanguage", selectedLanguage);

				model.addAttribute("categories", categories);
				model.addAttribute("languages", languages);

				return "script_edit";
			}
		}

		model.addAttribute("user", user);
		return "index";

	}

	@RequestMapping("non_connected")
	public String nonConnected() {
		return "non_connected";
	}

}