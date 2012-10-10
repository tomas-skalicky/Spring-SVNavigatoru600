package svnavigatoru.web;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import svnavigatoru.service.util.DateUtils;
import svnavigatoru.service.util.Localization;

/**
 * Parent of all controllers (except {@link ErrorController}) in the application.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class MetaController {

	/**
	 * Logger for this class and subclasses
	 */
	protected final Log logger = LogFactory.getLog(this.getClass());

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
