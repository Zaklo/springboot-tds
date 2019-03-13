package s4.spring.td2.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import s4.spring.td2.entities.Groupe;
import s4.spring.td2.entities.Organization;
import s4.spring.td2.repositories.GroupesRepository;
import s4.spring.td2.repositories.OrgaRepository;

@Controller
@RequestMapping("/orgas/")
public class OrgasController {

	@Autowired // orgasRepo pas instancié sinon (automatiquement Spring se charge de
				// l'instantiation si besoin)
	private OrgaRepository orgasRepo;
	@Autowired
	private GroupesRepository groupeRepo;

	/*
	 * @GetMapping("{path:(?:index)?}") // "?" = il y a 0 ou 1 fois index private
	 * String index() { return "index"; }
	 */

	@GetMapping({ "", "index" })
	public String index(Model model) {
		List<Organization> orgas = orgasRepo.findAll();
		model.addAttribute("orgas", orgas);
		return "orgas/index";
	}

	@PostMapping("submit")
	public RedirectView submit(Organization postedOrga) {
		if (postedOrga.getId() != 0) {
			int id = postedOrga.getId();
			Optional<Organization> opt = orgasRepo.findById(id);
			if (opt.isPresent()) {
				Organization orga = opt.get();
				copyFrom(postedOrga, orga);
				orgasRepo.save(orga);
			}
		} else {
			orgasRepo.save(postedOrga);
		}
		return new RedirectView("/orgas/");
	}

	/*
	 * redirection vers formulaire création organisation
	 */
	@GetMapping("new")
	public String frmNew(Model model) {
		model.addAttribute("orga", new Organization());
		return "orgas/frmCreation";
	}

	/*
	 * Création nouvelle organisation
	 */
	@RequestMapping(value = "new/addNew", method = RequestMethod.POST)
	public RedirectView addNew(HttpServletRequest request, @RequestParam("new_name") String name,
			@RequestParam("new_domain") String domain, @RequestParam("new_aliases") String aliases) {

		Organization organization = new Organization();
		organization.setName(name);
		organization.setDomaine(domain);
		organization.setAliases(aliases);
		orgasRepo.save(organization);

		return new RedirectView("/orgas/");
	}

	/*
	 * Edite une organisation
	 */
	@RequestMapping(value = "edit/edition/{id}/", method = RequestMethod.POST)
	@ResponseBody
	public RedirectView frmEdition(HttpServletRequest request, @RequestParam("new_name") String name,
			@RequestParam("new_domain") String domain, @RequestParam("new_aliases") String aliases,
			@PathVariable int id, Model model) {
		
		orgasRepo.getOne(id).setName(name);
		orgasRepo.getOne(id).setDomaine(domain);
		orgasRepo.getOne(id).setAliases(aliases);
		orgasRepo.save(orgasRepo.getOne(id));
		
		return new RedirectView("/orgas/");
	}

	/*
	 * redirection pour le bouton retour
	 */
	@GetMapping("cancel")
	public RedirectView cancelDisplay(Model model) {
		return new RedirectView("/orgas/");
	}

	@GetMapping("edition/cancel")
	public RedirectView cancelEdition(Model model) {
		return new RedirectView("/orgas/");
	}

	/*
	 * redirige vers la page de consulation
	 */
	@GetMapping("display/{id}")
	public String display(@PathVariable int id, Model model) {
		Optional<Organization> opt = orgasRepo.findById(id);
		if (opt.isPresent()) {
			model.addAttribute("orga", opt.get());
			return "orgas/frmDisplay";
		}
		return "orgas/404";
	}

	/*
	 * redirige vers la page d'édition
	 */
	@GetMapping("edit/{id}")
	public String frmEdit(@PathVariable int id, Model model) {
		Optional<Organization> opt = orgasRepo.findById(id);
		if (opt.isPresent()) {
			model.addAttribute("orga", opt.get());
			return "orgas/frmEdition";
		}
		return "orgas/404";
	}

	/*
	 * supprime une organisation
	 */
	@GetMapping("delete/{id}")
	public RedirectView delete(@PathVariable int id, Model model) {
		Optional<Organization> opt = orgasRepo.findById(id);
		if (opt.isPresent()) {
			model.addAttribute("orga", opt.get());
			orgasRepo.deleteById(id);
			
			return new RedirectView("/orgas/");
		}
		return new RedirectView("/orgas/404");
	}

	/*
	 * redirige vers ERREUR 404
	 */
	@RequestMapping("404")
	public String notFound() {
		return "orgas/404";
	}

	private void copyFrom(Organization source, Organization dest) {
		dest.setName(source.getName());
		dest.setDomaine(source.getDomaine());
		dest.setAliases(source.getAliases());
	}

	/*
	 * Créé une organisation préfaite
	 */
	@RequestMapping("create")
	@ResponseBody
	public String createOrga() {
		Organization organization = new Organization();
		organization.setName("IUT Ifs");
		organization.setDomaine("unicaen.fr");
		organization.setAliases("iutc3.unicaen.fr");
		organization.setCity("Ifs");
		orgasRepo.save(organization);

		return organization + " ajoutée dans la BDD.";
	}

	@RequestMapping("groupes")
	@ResponseBody
	public String createGroupe(GroupesRepository repoGroupes) {
		Groupe groupe = new Groupe();
		repoGroupes.save(groupe);
		return "Groupe créé";
	}

	@RequestMapping("create/groupes/{id}")
	@ResponseBody
	public String createOrgaWithGroupes(@PathVariable int id) {
		Optional<Organization> optOrga = orgasRepo.findById(id);

		if (optOrga.isPresent()) { // si on a récupéré par l'ID
			Organization organization = optOrga.get(); // on récupère (test d'existence)
			Groupe groupe = new Groupe();
			groupe.setName("Etudiants");
			organization.addGroupe(groupe);
			orgasRepo.save(organization);
			return organization + " .ajoutée dans la BDD.";
		}

		return "ORGANISATION INEXISTANTE !";
	}
}
