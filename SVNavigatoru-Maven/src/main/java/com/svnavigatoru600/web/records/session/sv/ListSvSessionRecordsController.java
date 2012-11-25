package com.svnavigatoru600.web.records.session.sv;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.web.records.session.ListRecordsController;


@Controller
public class ListSvSessionRecordsController extends ListRecordsController {

	private static final String BASE_URL = "/zapisy-z-jednani/sv/";

	/**
	 * Constructor.
	 */
	@Autowired
	public ListSvSessionRecordsController(SessionRecordDao recordDao, MessageSource messageSource) {
		super(ListSvSessionRecordsController.BASE_URL, new PageViews(), SessionRecordType.SESSION_RECORD_OF_SV,
				recordDao, messageSource);
	}

	@Override
	@RequestMapping(value = ListSvSessionRecordsController.BASE_URL, method = RequestMethod.GET)
	public String initPage(HttpServletRequest request, ModelMap model) {
		return super.initPage(request, model);
	}

	@Override
	@RequestMapping(value = ListSvSessionRecordsController.BASE_URL + "vytvoreno/", method = RequestMethod.GET)
	public String initPageAfterCreate(HttpServletRequest request, ModelMap model) {
		return super.initPageAfterCreate(request, model);
	}

	@Override
	@RequestMapping(value = ListSvSessionRecordsController.BASE_URL + "smazano/", method = RequestMethod.GET)
	public String initPageAfterDelete(HttpServletRequest request, ModelMap model) {
		return super.initPageAfterDelete(request, model);
	}
}