package svnavigatoru.web.records.otherdocuments.contracts;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import svnavigatoru.domain.records.OtherDocumentRecordType;
import svnavigatoru.repository.records.OtherDocumentRecordDao;
import svnavigatoru.web.records.otherdocuments.RetrieveDocumentController;

@Controller
public class RetrieveContractDocumentController extends RetrieveDocumentController {

	private static final String BASE_URL = "/dalsi-dokumenty/smlouvy/";

	/**
	 * Constructor.
	 */
	@Autowired
	public RetrieveContractDocumentController(OtherDocumentRecordDao recordDao, MessageSource messageSource) {
		super(RetrieveContractDocumentController.BASE_URL, new PageViews(), OtherDocumentRecordType.CONTRACT,
				recordDao, messageSource);
	}

	@Override
	@RequestMapping(value = RetrieveContractDocumentController.BASE_URL + "existujici/{recordId}/stahnout/",
			method = RequestMethod.GET)
	public void retrieve(@PathVariable int recordId, HttpServletResponse response, ModelMap model) {
		super.retrieve(recordId, response, model);
	}
}
