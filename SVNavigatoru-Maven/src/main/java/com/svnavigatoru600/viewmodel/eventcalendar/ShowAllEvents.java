package com.svnavigatoru600.viewmodel.eventcalendar;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class ShowAllEvents {

    private List<CalendarEvent> events;
    private Map<CalendarEvent, String> localizedDeleteQuestions = null;
    private boolean eventCreated = false;
    private boolean eventDeleted = false;

    public List<CalendarEvent> getEvents() {
        return this.events;
    }

    public void setEvents(List<CalendarEvent> events) {
        this.events = events;
    }

    public Map<CalendarEvent, String> getLocalizedDeleteQuestions() {
        return this.localizedDeleteQuestions;
    }

    public void setLocalizedDeleteQuestions(Map<CalendarEvent, String> localizedDeleteQuestions) {
        this.localizedDeleteQuestions = localizedDeleteQuestions;
    }

    public boolean isEventCreated() {
        return this.eventCreated;
    }

    public void setEventCreated(boolean eventCreated) {
        this.eventCreated = eventCreated;
    }

    public boolean isEventDeleted() {
        return this.eventDeleted;
    }

    public void setEventDeleted(boolean eventDeleted) {
        this.eventDeleted = eventDeleted;
    }
}
