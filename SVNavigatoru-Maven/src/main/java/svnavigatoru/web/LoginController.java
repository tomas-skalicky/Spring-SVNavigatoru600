package svnavigatoru.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController extends MetaController {

	private static final String PAGE_VIEW = "login";

	@RequestMapping(value = "/prihlaseni/", method = RequestMethod.GET)
	public String login() {
		return LoginController.PAGE_VIEW;
	}

	@RequestMapping(value = "/prihlaseni/neuspech/", method = RequestMethod.GET)
	public String loginFailed(ModelMap model) {
		model.addAttribute("error", "true");
		return LoginController.PAGE_VIEW;
	}
}
