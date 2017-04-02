package com.svnavigatoru600.domain.eventcalendar;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class CalendarEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    @DateTimeFormat(style = "M-")
    private Date date;
    private String description;
    private PriorityTypeEnum priority;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Different - not "getPriority" - name of the getter method is necessary. Otherwise, the methods' signatures would
     * be identical.
     */
    public PriorityTypeEnum getTypedPriority() {
        return priority;
    }

    /**
     * This getter is necessary because of Hibernate.
     */
    public String getPriority() {
        return priority.name();
    }

    public void setPriority(final PriorityTypeEnum priority) {
        this.priority = priority;
    }

    /**
     * This setter is necessary because of Hibernate.
     */
    public void setPriority(final String priority) {
        this.priority = PriorityTypeEnum.valueOf(priority);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("CalendarEvent [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append(", date=");
        builder.append(date);
        builder.append(", description=");
        builder.append(description);
        builder.append(", priority=");
        builder.append(priority);
        builder.append("]");
        return builder.toString();
    }
}
