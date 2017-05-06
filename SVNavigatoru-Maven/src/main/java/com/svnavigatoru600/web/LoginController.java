package com.svnavigatoru600.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.svnavigatoru600.web.url.LoginUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class LoginController extends AbstractMetaController {

    private static final String PAGE_VIEW = "login";

    @GetMapping(value = LoginUrlParts.BASE_URL)
    public String login() {
        return LoginController.PAGE_VIEW;
    }

    @GetMapping(value = LoginUrlParts.FAIL_URL)
    public String loginFailed(final ModelMap model) {
        model.addAttribute("error", Boolean.TRUE.toString());
        return LoginController.PAGE_VIEW;
    }
}
