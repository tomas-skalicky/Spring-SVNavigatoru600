package svnavigatoru.web.records.otherdocuments.contracts;

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
public class DeleteContractDocumentController extends DeleteDocumentController {

	private static final String BASE_URL = "/dalsi-dokumenty/smlouvy/";

	/**
	 * Constructor.
	 */
	@Autowired
	public DeleteContractDocumentController(OtherDocumentRecordDao recordDao, MessageSource messageSource) {
		super(DeleteContractDocumentController.BASE_URL, new PageViews(), OtherDocumentRecordType.CONTRACT, recordDao,
				messageSource);
	}

	@Override
	@RequestMapping(value = DeleteContractDocumentController.BASE_URL + "existujici/{recordId}/smazat/",
			method = RequestMethod.GET)
	public String delete(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
		return super.delete(recordId, request, model);
	}
}
