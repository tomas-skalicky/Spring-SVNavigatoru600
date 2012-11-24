package com.svnavigatoru600.web.records;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.svnavigatoru600.domain.records.DocumentRecord;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.web.PrivateSectionMetaController;


/**
 * Parent of all controllers which handle all operations upon the
 * {@link DocumentRecord}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class DocumentRecordController extends PrivateSectionMetaController {

	/**
	 * The part of the URL which is common for all operations performed on the
	 * considered documents.
	 */
	protected final String BASE_URL;
	protected final PageViews VIEWS;
	protected MessageSource messageSource;

	/**
	 * Constructs a controller which considers all {@link DocumentRecord}s.
	 */
	public DocumentRecordController(String baseUrl, PageViews views, MessageSource messageSource) {
		this.BASE_URL = baseUrl;
		this.VIEWS = views;
		this.messageSource = messageSource;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// Nasty since the format is localized.
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.CALENDAR_DATE_FORMAT);

		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
