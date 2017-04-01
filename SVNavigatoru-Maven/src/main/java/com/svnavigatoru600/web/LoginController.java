package com.svnavigatoru600.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.url.LoginUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class LoginController extends AbstractMetaController {

    private static final String PAGE_VIEW = "login";

    @RequestMapping(value = LoginUrlParts.BASE_URL, method = RequestMethod.GET)
    public String login() {
        return LoginController.PAGE_VIEW;
    }

    @RequestMapping(value = LoginUrlParts.FAIL_URL, method = RequestMethod.GET)
    public String loginFailed(final ModelMap model) {
        model.addAttribute("error", Boolean.TRUE.toString());
        return LoginController.PAGE_VIEW;
    }
}
