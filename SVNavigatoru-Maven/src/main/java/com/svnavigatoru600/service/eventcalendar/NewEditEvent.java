package com.svnavigatoru600.service.eventcalendar;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.domain.eventcalendar.PriorityType;

@Service
public abstract class NewEditEvent {

    private CalendarEvent event = null;
    private Map<Integer, String> priorityCheckboxId = null;
    private Map<Integer, String> localizedPriorityCheckboxTitles = null;
    /**
     * Holds a value of the <code>newPriority</code> radiobutton. The value equals a localization of the
     * selected {@link PriorityType}. See the <code>NewEditEventController.populatePriorityTypeList</code>
     * method.
     */
    private String newPriority;

    public CalendarEvent getEvent() {
        return this.event;
    }

    public void setEvent(CalendarEvent event) {
        this.event = event;
    }

    public Map<Integer, String> getPriorityCheckboxId() {
        return this.priorityCheckboxId;
    }

    public void setPriorityCheckboxId(Map<Integer, String> priorityCheckboxId) {
        this.priorityCheckboxId = priorityCheckboxId;
    }

    public Map<Integer, String> getLocalizedPriorityCheckboxTitles() {
        return this.localizedPriorityCheckboxTitles;
    }

    public void setLocalizedPriorityCheckboxTitles(Map<Integer, String> localizedPriorityCheckboxTitles) {
        this.localizedPriorityCheckboxTitles = localizedPriorityCheckboxTitles;
    }

    public String getNewPriority() {
        return this.newPriority;
    }

    public void setNewPriority(String newPriority) {
        this.newPriority = newPriority;
    }
}
