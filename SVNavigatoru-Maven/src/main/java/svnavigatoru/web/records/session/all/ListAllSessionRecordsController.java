package svnavigatoru.web.records.session.all;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import svnavigatoru.repository.records.SessionRecordDao;
import svnavigatoru.web.records.session.ListRecordsController;

@Controller
public class ListAllSessionRecordsController extends ListRecordsController {

	private static final String BASE_URL = "/zapisy-z-jednani/";

	/**
	 * Constructor.
	 */
	@Autowired
	public ListAllSessionRecordsController(SessionRecordDao recordDao, MessageSource messageSource) {
		super(ListAllSessionRecordsController.BASE_URL, new PageViews(), recordDao, messageSource);
	}

	@Override
	@RequestMapping(value = ListAllSessionRecordsController.BASE_URL, method = RequestMethod.GET)
	public String initPage(HttpServletRequest request, ModelMap model) {
		return super.initPage(request, model);
	}

	@Override
	@RequestMapping(value = ListAllSessionRecordsController.BASE_URL + "vytvoreno/", method = RequestMethod.GET)
	public String initPageAfterCreate(HttpServletRequest request, ModelMap model) {
		return super.initPageAfterCreate(request, model);
	}

	@Override
	@RequestMapping(value = ListAllSessionRecordsController.BASE_URL + "smazano/", method = RequestMethod.GET)
	public String initPageAfterDelete(HttpServletRequest request, ModelMap model) {
		return super.initPageAfterDelete(request, model);
	}
}
