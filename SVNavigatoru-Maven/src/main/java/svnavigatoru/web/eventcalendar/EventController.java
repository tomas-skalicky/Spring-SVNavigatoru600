package svnavigatoru.web.eventcalendar;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import svnavigatoru.domain.eventcalendar.CalendarEvent;
import svnavigatoru.repository.CalendarEventDao;
import svnavigatoru.service.util.DateUtils;
import svnavigatoru.web.PrivateSectionMetaController;

/**
 * Parent of all controllers which handle all operations upon the
 * {@link CalendarEvent}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class EventController extends PrivateSectionMetaController {

	protected static final String BASE_URL = "/kalendar-akci/";
	protected CalendarEventDao eventDao;
	protected MessageSource messageSource;

	public EventController(CalendarEventDao eventDao, MessageSource messageSource) {
		this.eventDao = eventDao;
		this.messageSource = messageSource;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// Nasty since the format is localized.
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.CALENDAR_DATE_FORMAT);

		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
