package s4.spring.td2.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.jeemv.springboot.vuejs.VueJS;
import io.github.jeemv.springboot.vuejs.utilities.Http;
import io.github.jeemv.springboot.vuejs.utilities.JsArray;
import s4.spring.td2.entities.Organization;
import s4.spring.td2.repositories.OrgaRepository;

@Controller
@RequestMapping("/vue/")
public class TestVueController {

	@Autowired
	private VueJS vue;

	@Autowired
	private OrgaRepository repo;

	@GetMapping("test")
	public String test(Model model) {
		model.addAttribute("vue", vue);
		vue.addMethod("update", "this.message='Message modifié !';");
		vue.addMethod("testAjax", "var self=this;" + Http.post("/vue/test/ajax", "{v:self.inputValue}",
				"self.ajaxMessage=response.data;self.alertVisible=true;"));
		vue.addData("message", "Hello world !");
		vue.addData("alertVisible", false);
		vue.addData("ajaxMessage");
		vue.addData("inputValue");
		return "vueJS/test";
	}

	@PostMapping("test/ajax")
	@ResponseBody
	public String testAjax(@RequestBody String v) {
		return "Test OK" + v;
	}

	@GetMapping("/orgas/")
	public String geneSpaOrgas(Model model) {
		model.addAttribute("vue", vue);
		List<Organization> orgas = repo.findAll();
		vue.addData("orgas", orgas);
		vue.addData("editedIndex", -1);
		vue.addDataRaw("headers",
				"[{text:'Name', value:'name'},{text:'Domain', value:'domain'},{text:'Aliases', value:'aliases'}]");
		vue.addDataRaw("dialog", "false");
		vue.addDataRaw("editedItem", "{}");
		vue.addDataRaw("editedIndex", "-1");
		vue.addComputed("formTitle", "(this.itemIndex==-1)?'Nouvelle orga':'Modification orga'");
		vue.addMethod("close", "this.dialog=false;");
		vue.addMethod("save", "var self=this;this.editedIndex=-1;this.dialog=false;"
				+ Http.post("/rest/orgas/create", "self.editedItem", ""));
		vue.addMethod("deleteItem",
				"var self=this;" + Http.post("/rest/orgas/delete", "item", JsArray.remove("self.orgas", "item")),
				"item");
		vue.addMethod("editItem",
				"this.editedIndex=1;this.editedItem.name=item.name;this.editedItem.domaine=item.domaine;this.editedItem.aliases=item.aliases;",
				"item");
		return "vueJS/index";
	}
}
