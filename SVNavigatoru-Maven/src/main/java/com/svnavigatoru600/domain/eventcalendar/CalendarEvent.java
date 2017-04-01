package com.svnavigatoru600.domain.eventcalendar;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import org.springframework.format.annotation.DateTimeFormat;

import com.svnavigatoru600.service.eventcalendar.CalendarEventService;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class CalendarEvent implements Serializable {

    private static final long serialVersionUID = 530408081345869305L;

    private CalendarEventService eventService;

    @Inject
    public void setCalendarEventService(CalendarEventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Updates the persisted copy of this object.
     */
    public void update() {
        this.eventService.update(this);
    }

    private int id;
    private String name;
    @DateTimeFormat(style = "M-")
    private Date date;
    private String description;
    private PriorityType priority;

    /**
     * Initialises no property.
     */
    public CalendarEvent() {
    }

    /**
     * Initialises event's name, date, description and priority. Other properties are not touched.
     */
    public CalendarEvent(String name, Date date, String description, PriorityType priority) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.priority = priority;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Different - not "getPriority" - name of the getter method is necessary. Otherwise, the methods' signatures would
     * be identical.
     */
    public PriorityType getTypedPriority() {
        return this.priority;
    }

    /**
     * This getter is necessary because of Hibernate.
     */
    public String getPriority() {
        return this.priority.name();
    }

    public void setPriority(PriorityType priority) {
        this.priority = priority;
    }

    /**
     * This setter is necessary because of Hibernate.
     */
    public void setPriority(String priority) {
        this.priority = PriorityType.valueOf(priority);
    }

    @Override
    public String toString() {
        return new StringBuilder("[id=").append(this.id).append(", name=").append(this.name).append(", date=")
                .append(this.date).append(", description=").append(this.description).append(", priority=")
                .append(this.priority).append("]").toString();
    }
}
