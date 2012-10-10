package svnavigatoru.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import svnavigatoru.domain.eventcalendar.CalendarEvent;
import svnavigatoru.domain.forum.Contribution;
import svnavigatoru.domain.users.User;
import svnavigatoru.repository.CalendarEventDao;
import svnavigatoru.repository.forum.ContributionDao;
import svnavigatoru.service.eventcalendar.EventWrapper;
import svnavigatoru.service.forum.contributions.ContributionWrapper;
import svnavigatoru.service.util.DateUtils;
import svnavigatoru.service.util.OrderType;
import svnavigatoru.service.util.UserUtils;

/**
 * Parent of all controllers (except {@link ErrorController}) of the private
 * section in the application. The private section is the one which is
 * accessible only to the registered users.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class PrivateSectionMetaController extends MetaController {

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

		final int eventCount = 2;
		List<CalendarEvent> events = this.eventDao.findFutureEventsOrdered(DateUtils.getToday(), OrderType.ASCENDING);

		List<EventWrapper> futureEvents = new ArrayList<EventWrapper>(eventCount);
		for (int eventNum = 0; (eventNum < eventCount) && (eventNum < events.size()); ++eventNum) {
			CalendarEvent event = events.get(eventNum);
			futureEvents.add(new EventWrapper(event, request));
		}
		return futureEvents;
	}

	@ModelAttribute("lastSavedContributions")
	public List<ContributionWrapper> populateLastSavedContributions(HttpServletRequest request) {

		final int contributionCount = 2;
		List<Contribution> contributions = this.contributionDao.findOrdered("lastSaveTime", OrderType.DESCENDING,
				contributionCount);

		List<ContributionWrapper> lastSavedContributions = new ArrayList<ContributionWrapper>(contributionCount);
		for (int contributionNum = 0; (contributionNum < contributionCount) && (contributionNum < contributions.size()); ++contributionNum) {
			Contribution contribution = contributions.get(contributionNum);
			lastSavedContributions.add(new ContributionWrapper(contribution, request));
		}
		return lastSavedContributions;
	}
}
