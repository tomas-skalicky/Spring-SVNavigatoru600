package com.svnavigatoru600.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.CalendarEventDao;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.OrderType;
import com.svnavigatoru600.service.util.UserUtils;
import com.svnavigatoru600.viewmodel.eventcalendar.EventWrapper;
import com.svnavigatoru600.viewmodel.forum.contributions.ContributionWrapper;

/**
 * Parent of all controllers (except {@link ErrorController}) of the private section in the application. The
 * private section is the one which is accessible only to the registered users.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class PrivateSectionMetaController extends MetaController {

    /**
     * The number of future {@link CalendarEvent calendar event} shown in the sidebar of the web page.
     */
    private static final int FUTURE_EVENT_COUNT = 2;
    /**
     * The number of last saved {@link Contribution contributions} shown in the sidebar of the web page.
     */
    private static final int LAST_SAVED_CONTRIBUTION_COUNT = 2;

    private CalendarEventDao eventDao;
    private ContributionDao contributionDao;

    @Autowired
    public void setCalendarEventDao(CalendarEventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Autowired
    public void setContributionDao(ContributionDao contributionDao) {
        this.contributionDao = contributionDao;
    }

    @ModelAttribute("loggedUser")
    public User populateLoggedUser() {
        return UserUtils.getLoggedUser();
    }

    @ModelAttribute("futureEvents")
    public List<EventWrapper> populateFutureEvents(HttpServletRequest request) {

        List<CalendarEvent> events = this.eventDao.findFutureEventsOrdered(DateUtils.getToday(),
                OrderType.ASCENDING);

        List<EventWrapper> futureEvents = new ArrayList<EventWrapper>(FUTURE_EVENT_COUNT);
        for (int eventNum = 0, eventCount = events.size(); (eventNum < FUTURE_EVENT_COUNT)
                && (eventNum < eventCount); ++eventNum) {
            CalendarEvent event = events.get(eventNum);
            futureEvents.add(new EventWrapper(event, request));
        }
        return futureEvents;
    }

    @ModelAttribute("lastSavedContributions")
    public List<ContributionWrapper> populateLastSavedContributions(HttpServletRequest request) {

        List<Contribution> contributions = this.contributionDao.findOrdered("lastSaveTime",
                OrderType.DESCENDING, LAST_SAVED_CONTRIBUTION_COUNT);

        List<ContributionWrapper> lastSavedContributions = new ArrayList<ContributionWrapper>(
                LAST_SAVED_CONTRIBUTION_COUNT);
        for (int contributionNum = 0, contributionCount = contributions.size(); (contributionNum < LAST_SAVED_CONTRIBUTION_COUNT)
                && (contributionNum < contributionCount); ++contributionNum) {
            Contribution contribution = contributions.get(contributionNum);
            lastSavedContributions.add(new ContributionWrapper(contribution, request));
        }
        return lastSavedContributions;
    }
}
