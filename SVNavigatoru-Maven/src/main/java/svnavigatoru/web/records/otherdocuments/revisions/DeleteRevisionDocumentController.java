package svnavigatoru.web.records.otherdocuments.revisions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import svnavigatoru.domain.records.OtherDocumentRecordType;
import svnavigatoru.repository.records.OtherDocumentRecordDao;
import svnavigatoru.web.records.otherdocuments.DeleteDocumentController;

@Controller
public class DeleteRevisionDocumentController extends DeleteDocumentController {

	private static final String BASE_URL = "/dalsi-dokumenty/pravidelne-revize/";

	/**
	 * Constructor.
	 */
	@Autowired
	public DeleteRevisionDocumentController(OtherDocumentRecordDao recordDao, MessageSource messageSource) {
		super(DeleteRevisionDocumentController.BASE_URL, new PageViews(), OtherDocumentRecordType.REGULAR_REVISION,
				recordDao, messageSource);
	}

	@Override
	@RequestMapping(value = DeleteRevisionDocumentController.BASE_URL + "existujici/{recordId}/smazat/",
			method = RequestMethod.GET)
	public String delete(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
		return super.delete(recordId, request, model);
	}
}
