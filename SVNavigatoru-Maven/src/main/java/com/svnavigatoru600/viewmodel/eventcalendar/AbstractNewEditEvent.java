package com.svnavigatoru600.viewmodel.eventcalendar;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.viewmodel.SendNotification;
import com.svnavigatoru600.viewmodel.SendNotificationViewModel;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractNewEditEvent implements SendNotificationViewModel {

    private CalendarEvent event = null;
    private Map<Integer, String> priorityCheckboxId = null;
    private Map<Integer, String> localizedPriorityCheckboxTitles = null;
    /**
     * Holds a value of the <code>newPriority</code> radiobutton. The value equals a localization of the
     * selected {@link com.svnavigatoru600.domain.eventcalendar.PriorityType PriorityType}. See the
     * <code>NewEditEventController.populatePriorityTypeList</code> method.
     */
    private String newPriority;
    private SendNotification sendNotification = null;

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

    @Override
    public SendNotification getSendNotification() {
        return this.sendNotification;
    }

    @Override
    public void setSendNotification(SendNotification sendNotification) {
        this.sendNotification = sendNotification;
    }

    @Override
    public String toString() {
        return new StringBuilder("[event=").append(this.event).append(", priorityCheckboxId=")
                .append(this.priorityCheckboxId).append(", localizedPriorityCheckboxTitles=")
                .append(this.localizedPriorityCheckboxTitles).append(", newPriority=")
                .append(this.newPriority).append(", sendNotification=").append(this.sendNotification)
                .append("]").toString();
    }
}
