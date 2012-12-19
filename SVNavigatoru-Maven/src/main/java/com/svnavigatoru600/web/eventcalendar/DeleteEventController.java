package com.svnavigatoru600.web.eventcalendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.repository.CalendarEventDao;
import com.svnavigatoru600.web.Configuration;

@Controller
public class DeleteEventController extends EventController {

    private static final String REQUEST_MAPPING_BASE_URL = DeleteEventController.BASE_URL
            + "existujici/{eventId}/smazat/";
    private static final String SUCCESSFUL_DELETE_URL = DeleteEventController.BASE_URL + "smazano/";
    /**
     * Code of the database error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "event-calendar.deletion-failed-due-to-database-error";
    private ListEventsController listController;

    @Autowired
    public void setListController(final ListEventsController listController) {
        this.listController = listController;
    }

    /**
     * Constructor.
     */
    @Autowired
    public DeleteEventController(final CalendarEventDao eventDao, final MessageSource messageSource) {
        super(eventDao, messageSource);
    }

    @RequestMapping(value = DeleteEventController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    @Transactional
    public String delete(@PathVariable int eventId, final HttpServletRequest request, final ModelMap model) {
        try {
            // Deletes the event from the repository.
            final CalendarEvent event = this.eventDao.findById(eventId);
            this.eventDao.delete(event);

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE,
                    DeleteEventController.SUCCESSFUL_DELETE_URL);
            return Configuration.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(e);
            final String view = this.listController.initPage(request, model);
            model.addAttribute("error", DeleteEventController.DATABASE_ERROR_MESSAGE_CODE);
            return view;
        }
    }
}
