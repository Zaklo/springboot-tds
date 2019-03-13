package s4.spring.td2.components;

import java.io.IOException;

import io.github.jeemv.springboot.vuejs.components.VueComponent;

public class ConfirmButton {

	public static void main(String[] args) throws IOException {
		VueComponent compo = new VueComponent("m-confirm-button");
		compo.addData("dialog", false);
		compo.setProps("title", "message", "validatecaption", "cancelcaption");
		compo.addProp("width", 500);
		compo.addMethod("cancelation", "this.$emit('cancelation');this.dialog=false;");
		compo.addMethod("validation", "this.$emit('validation');this.dialog=false;");
		compo.setDefaultTemplateFile();
		compo.createFile(false);
	}

}
