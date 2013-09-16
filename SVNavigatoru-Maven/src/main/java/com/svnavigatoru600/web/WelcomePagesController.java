package com.svnavigatoru600.web;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.web.news.ListNewsController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class WelcomePagesController extends AbstractPrivateSectionMetaController {

    @Inject
    private ListNewsController newsController;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String init(HttpServletRequest request, ModelMap model) {
        return this.newsController.initPage(request, model);
    }
}
