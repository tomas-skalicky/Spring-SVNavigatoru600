package svnavigatoru.service.eventcalendar;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import svnavigatoru.domain.eventcalendar.CalendarEvent;
import svnavigatoru.service.util.DateUtils;
import svnavigatoru.service.util.Localization;
import svnavigatoru.web.PrivateSectionMetaController;

/**
 * Used in the {@link PrivateSectionMetaController}.
 * 
 * @author Tomas Skalicky
 */
public class EventWrapper {

	private CalendarEvent event = null;
	private String middleDateFormattedDate = null;

	public EventWrapper(CalendarEvent event, HttpServletRequest request) {
		this.event = event;
		Locale locale = Localization.getLocale(request);
		this.middleDateFormattedDate = DateUtils.format(event.getDate(), DateUtils.MIDDLE_DATE_FORMATS.get(locale),
				locale);
	}

	public CalendarEvent getEvent() {
		return this.event;
	}

	public void setEvent(CalendarEvent event) {
		this.event = event;
	}

	public String getmiddleDateFormattedDate() {
		return this.middleDateFormattedDate;
	}

	public void setmiddleDateFormattedDate(String middleDateFormattedDate) {
		this.middleDateFormattedDate = middleDateFormattedDate;
	}
}
