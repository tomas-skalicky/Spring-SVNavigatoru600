package com.svnavigatoru600.web;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.svnavigatoru600.web.news.ListNewsController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class WelcomePagesController extends AbstractPrivateSectionMetaController {

    @Inject
    private ListNewsController newsController;

    @GetMapping(value = "/")
    public String init(final HttpServletRequest request, final ModelMap model) {
        return newsController.initPage(request, model);
    }
}
