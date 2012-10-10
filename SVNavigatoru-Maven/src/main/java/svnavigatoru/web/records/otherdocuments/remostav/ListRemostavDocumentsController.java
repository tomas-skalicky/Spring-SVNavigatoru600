package svnavigatoru.web.records.otherdocuments.remostav;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import svnavigatoru.domain.records.OtherDocumentRecordType;
import svnavigatoru.repository.records.OtherDocumentRecordDao;
import svnavigatoru.web.records.otherdocuments.ListDocumentsController;

@Controller
public class ListRemostavDocumentsController extends ListDocumentsController {

	private static final String BASE_URL = "/remostav/dokumentace/";

	/**
	 * Constructor.
	 */
	@Autowired
	public ListRemostavDocumentsController(OtherDocumentRecordDao recordDao, MessageSource messageSource) {
		super(ListRemostavDocumentsController.BASE_URL, new PageViews(), OtherDocumentRecordType.REMOSTAV, recordDao,
				messageSource);
	}

	@RequestMapping(value = ListRemostavDocumentsController.BASE_URL, method = RequestMethod.GET)
	public String initPage(HttpServletRequest request, ModelMap model) {
		return super.initPage(request, model);
	}

	@RequestMapping(value = ListRemostavDocumentsController.BASE_URL + "vytvoreno/", method = RequestMethod.GET)
	public String initPageAfterCreate(HttpServletRequest request, ModelMap model) {
		return super.initPageAfterCreate(request, model);
	}

	@RequestMapping(value = ListRemostavDocumentsController.BASE_URL + "smazano/", method = RequestMethod.GET)
	public String initPageAfterDelete(HttpServletRequest request, ModelMap model) {
		return super.initPageAfterDelete(request, model);
	}
}
