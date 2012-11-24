package com.svnavigatoru600.web.records.session.all;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.web.records.session.DeleteRecordController;


@Controller
public class DeleteAllSessionRecordController extends DeleteRecordController {

	private static final String BASE_URL = "/zapisy-z-jednani/";

	/**
	 * Constructor.
	 */
	@Autowired
	public DeleteAllSessionRecordController(SessionRecordDao recordDao, MessageSource messageSource) {
		super(DeleteAllSessionRecordController.BASE_URL, new PageViews(), recordDao, messageSource);
	}

	@Override
	@RequestMapping(value = DeleteAllSessionRecordController.BASE_URL + "existujici/{recordId}/smazat/",
			method = RequestMethod.GET)
	public String delete(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
		return super.delete(recordId, request, model);
	}
}
