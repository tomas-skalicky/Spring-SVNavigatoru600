package svnavigatoru.web.eventcalendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import svnavigatoru.domain.eventcalendar.CalendarEvent;
import svnavigatoru.repository.CalendarEventDao;
import svnavigatoru.web.Configuration;

@Controller
public class DeleteEventController extends EventController {

	private static final String REQUEST_MAPPING_BASE_URL = DeleteEventController.BASE_URL
			+ "existujici/{eventId}/smazat/";
	private static final String SUCCESSFUL_DELETE_URL = DeleteEventController.BASE_URL + "smazano/";
	/**
	 * Code of the database error message used when the
	 * {@link DataAccessException} is thrown.
	 */
	public static final String DATABASE_ERROR_MESSAGE_CODE = "event-calendar.deletion-failed-due-to-database-error";
	private ListEventsController listController;

	@Autowired
	public void setListController(ListEventsController listController) {
		this.listController = listController;
	}

	/**
	 * Constructor.
	 */
	@Autowired
	public DeleteEventController(CalendarEventDao eventDao, MessageSource messageSource) {
		super(eventDao, messageSource);
	}

	@RequestMapping(value = DeleteEventController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
	public String delete(@PathVariable int eventId, HttpServletRequest request, ModelMap model) {
		try {
			// Deletes the event from the repository.
			CalendarEvent event = this.eventDao.findById(eventId);
			this.eventDao.delete(event);

			// Returns the form success view.
			model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE, DeleteEventController.SUCCESSFUL_DELETE_URL);
			return Configuration.REDIRECTION_PAGE;

		} catch (DataAccessException e) {
			// We encountered a database problem.
			this.logger.error(e);
			String view = this.listController.initPage(request, model);
			model.addAttribute("error", DeleteEventController.DATABASE_ERROR_MESSAGE_CODE);
			return view;
		}
	}
}
