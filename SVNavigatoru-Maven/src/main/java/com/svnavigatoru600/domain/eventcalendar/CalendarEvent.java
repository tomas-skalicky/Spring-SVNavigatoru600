package com.svnavigatoru600.domain.eventcalendar;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.svnavigatoru600.repository.CalendarEventDao;

public class CalendarEvent implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 530408081345869305L;

    private CalendarEventDao eventDao;

    public void setCalendarEventDao(CalendarEventDao eventDao) {
        this.eventDao = eventDao;
    }

    public void update() {
        this.eventDao.update(this);
    }

    private int id;
    private String name;
    @DateTimeFormat(style = "M-")
    private Date date;
    private String description;
    private PriorityType priority;

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
     * Different - not "getPriority" - name of the getter method is necessary. Otherwise, the methods'
     * signatures would be identical.
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
}
