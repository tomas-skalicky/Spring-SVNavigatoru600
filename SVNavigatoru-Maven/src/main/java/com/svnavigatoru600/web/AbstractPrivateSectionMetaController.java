package com.svnavigatoru600.web;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.domain.forum.ForumContribution;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.eventcalendar.CalendarEventService;
import com.svnavigatoru600.service.forum.ContributionService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.viewmodel.eventcalendar.EventWrapper;
import com.svnavigatoru600.viewmodel.forum.contributions.ContributionWrapper;

/**
 * Parent of all controllers (except {@link ErrorController}) of the private section in the application. The private
 * section is the one which is accessible only to the registered users.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractPrivateSectionMetaController extends AbstractMetaController {

    /**
     * The number of future {@link CalendarEvent calendar event} shown in the sidebar of the web page.
     */
    private static final int FUTURE_EVENT_COUNT = 2;
    /**
     * The number of last saved {@link ForumContribution contributions} shown in the sidebar of the web page.
     */
    private static final int LAST_SAVED_CONTRIBUTION_COUNT = 2;

    private CalendarEventService eventService;
    private ContributionService contributionService;
    private UserService userService;

    @Inject
    public void setCalendarEventService(final CalendarEventService eventService) {
        this.eventService = eventService;
    }

    @Inject
    public void setContributionService(final ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    @ModelAttribute("loggedUser")
    public User populateLoggedUser() {
        return userService.getLoggerUser();
    }

    @ModelAttribute("futureEvents")
    public List<EventWrapper> populateFutureEvents(final HttpServletRequest request) {
        final List<CalendarEvent> events = eventService.findAllFutureEventsOrdered();

        final List<EventWrapper> futureEvents = new ArrayList<EventWrapper>(FUTURE_EVENT_COUNT);
        for (int eventNum = 0, eventCount = events.size(); (eventNum < FUTURE_EVENT_COUNT)
                && (eventNum < eventCount); ++eventNum) {
            futureEvents.add(new EventWrapper(events.get(eventNum), request));
        }
        return futureEvents;
    }

    @ModelAttribute("lastSavedContributions")
    public List<ContributionWrapper> populateLastSavedContributions(final HttpServletRequest request) {
        final List<ForumContribution> contributions = contributionService
                .findLimitedNumberOrdered(LAST_SAVED_CONTRIBUTION_COUNT);

        final List<ContributionWrapper> lastSavedContributions = new ArrayList<ContributionWrapper>(
                LAST_SAVED_CONTRIBUTION_COUNT);
        for (int contributionNum = 0, contributionCount = contributions
                .size(); (contributionNum < LAST_SAVED_CONTRIBUTION_COUNT)
                        && (contributionNum < contributionCount); ++contributionNum) {
            lastSavedContributions.add(new ContributionWrapper(contributions.get(contributionNum), request));
        }
        return lastSavedContributions;
    }

    /**
     * Trivial getter
     */
    protected UserService getUserService() {
        return userService;
    }

    /**
     * Trivial setter
     */
    @Inject
    @Required
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }
}
