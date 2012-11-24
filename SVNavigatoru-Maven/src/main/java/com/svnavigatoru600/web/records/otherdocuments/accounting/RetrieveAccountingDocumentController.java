package com.svnavigatoru600.web.records.otherdocuments.accounting;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.web.records.otherdocuments.RetrieveDocumentController;


@Controller
public class RetrieveAccountingDocumentController extends RetrieveDocumentController {

	private static final String BASE_URL = "/dalsi-dokumenty/ucetnictvi/";

	/**
	 * Constructor.
	 */
	@Autowired
	public RetrieveAccountingDocumentController(OtherDocumentRecordDao recordDao, MessageSource messageSource) {
		super(RetrieveAccountingDocumentController.BASE_URL, new PageViews(), OtherDocumentRecordType.ACCOUNTING,
				recordDao, messageSource);
	}

	@Override
	@RequestMapping(value = RetrieveAccountingDocumentController.BASE_URL + "existujici/{recordId}/stahnout/",
			method = RequestMethod.GET)
	public void retrieve(@PathVariable int recordId, HttpServletResponse response, ModelMap model) {
		super.retrieve(recordId, response, model);
	}
}
