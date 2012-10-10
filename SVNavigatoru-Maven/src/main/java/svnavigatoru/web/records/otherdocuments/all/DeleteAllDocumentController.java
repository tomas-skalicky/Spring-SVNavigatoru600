package svnavigatoru.web.records.otherdocuments.all;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import svnavigatoru.repository.records.OtherDocumentRecordDao;
import svnavigatoru.web.records.otherdocuments.DeleteDocumentController;

@Controller
public class DeleteAllDocumentController extends DeleteDocumentController {

	private static final String BASE_URL = "/dalsi-dokumenty/";

	/**
	 * Constructor.
	 */
	@Autowired
	public DeleteAllDocumentController(OtherDocumentRecordDao recordDao, MessageSource messageSource) {
		super(DeleteAllDocumentController.BASE_URL, new PageViews(), recordDao, messageSource);
	}

	@Override
	@RequestMapping(value = DeleteAllDocumentController.BASE_URL + "existujici/{recordId}/smazat/",
			method = RequestMethod.GET)
	public String delete(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
		return super.delete(recordId, request, model);
	}
}
