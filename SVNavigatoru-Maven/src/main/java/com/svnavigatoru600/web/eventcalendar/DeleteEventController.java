package com.svnavigatoru600.web.eventcalendar;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.service.eventcalendar.CalendarEventService;
import com.svnavigatoru600.web.AbstractMetaController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class DeleteEventController extends AbstractEventController {

    private static final String REQUEST_MAPPING_BASE_URL = DeleteEventController.BASE_URL
            + "existujici/{eventId}/smazat/";
    private static final String SUCCESSFUL_DELETE_URL = DeleteEventController.BASE_URL + "smazano/";
    /**
     * Code of the database error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "event-calendar.deletion-failed-due-to-database-error";
    private ListEventsController listController;

    @Inject
    public void setListController(ListEventsController listController) {
        this.listController = listController;
    }

    /**
     * Constructor.
     */
    @Inject
    public DeleteEventController(CalendarEventService eventService, MessageSource messageSource) {
        super(eventService, messageSource);
    }

    @RequestMapping(value = DeleteEventController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    @Transactional
    public String delete(@PathVariable int eventId, HttpServletRequest request, ModelMap model) {
        try {
            this.getEventService().delete(eventId);

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE,
                    DeleteEventController.SUCCESSFUL_DELETE_URL);
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(e);
            String view = this.listController.initPage(request, model);
            model.addAttribute("error", DeleteEventController.DATABASE_ERROR_MESSAGE_CODE);
            return view;
        }
    }
}
