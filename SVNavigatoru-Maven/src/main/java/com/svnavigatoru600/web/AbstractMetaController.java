package com.svnavigatoru600.web;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.Localization;

/**
 * Parent of all controllers (except {@link ErrorController}) in the application.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractMetaController {

    /**
     * The JSP page which helps to clear the {@link org.springframework.ui.ModelMap ModelMap}.
     */
    protected static final String REDIRECTION_PAGE = "page-for-redirection";
    /**
     * The attribute which holds the destination URL of the redirection done by
     * {@link AbstractMetaController#REDIRECTION_PAGE redirection page}.
     */
    public static final String REDIRECTION_ATTRIBUTE = "redirectTo";

    @ModelAttribute("homeUrl")
    public String populateHomeUrl(HttpServletRequest request) {
        return request.getContextPath();
    }

    @ModelAttribute("currentYear")
    public String populateCurrentYear(HttpServletRequest request) {
        Locale locale = Localization.getLocale(request);
        return DateUtils.format(new Date(), DateUtils.LONG_YEAR_FORMAT, locale);
    }

    @ModelAttribute("myCssHome")
    public String populateMyCssHome(HttpServletRequest request) {
        return "/lib/css/";
    }

    @ModelAttribute("myJsHome")
    public String populateMyJsHome(HttpServletRequest request) {
        return "/lib/js/";
    }
}
