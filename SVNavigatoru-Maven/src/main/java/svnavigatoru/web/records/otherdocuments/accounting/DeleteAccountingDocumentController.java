package svnavigatoru.web.records.otherdocuments.accounting;

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
public class DeleteAccountingDocumentController extends DeleteDocumentController {

	private static final String BASE_URL = "/dalsi-dokumenty/ucetnictvi/";

	/**
	 * Constructor.
	 */
	@Autowired
	public DeleteAccountingDocumentController(OtherDocumentRecordDao recordDao, MessageSource messageSource) {
		super(DeleteAccountingDocumentController.BASE_URL, new PageViews(), OtherDocumentRecordType.ACCOUNTING,
				recordDao, messageSource);
	}

	@Override
	@RequestMapping(value = DeleteAccountingDocumentController.BASE_URL + "existujici/{recordId}/smazat/",
			method = RequestMethod.GET)
	public String delete(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
		return super.delete(recordId, request, model);
	}
}
