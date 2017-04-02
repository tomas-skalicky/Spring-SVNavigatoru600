package com.svnavigatoru600.domain.records;

import java.sql.Blob;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class SessionRecord extends AbstractDocumentRecord {

    private static final long serialVersionUID = 1L;

    /**
     * Type of this {@link SessionRecord}.
     */
    private SessionRecordTypeEnum type;
    /**
     * {@link Date} when an appropriate session took place. The date is determined by the user.
     */
    @DateTimeFormat(style = "L-")
    private Date sessionDate;
    /**
     * The important topics which have been discussed during an appropriate session.
     */
    private String discussedTopics;

    /**
     * Initialises no property.
     */
    public SessionRecord() {
    }

    /**
     * Initialises record's filename, file, type, session date and discussed topics. Other properties are not touched.
     */
    public SessionRecord(final String fileName, final Blob file, final SessionRecordTypeEnum type, final Date sessionDate, final String discussedTopics) {
        super(fileName, file);
        this.type = type;
        this.sessionDate = sessionDate;
        this.discussedTopics = discussedTopics;
    }

    /**
     * Different - not "getType" - name of the getter method is necessary. Otherwise, the methods' signatures would be
     * identical.
     */
    public SessionRecordTypeEnum getTypedType() {
        return type;
    }

    /**
     * This getter is necessary because of Hibernate.
     */
    public String getType() {
        return type.name();
    }

    public void setType(final SessionRecordTypeEnum type) {
        this.type = type;
    }

    /**
     * This setter is necessary because of Hibernate.
     */
    public void setType(final String type) {
        this.type = SessionRecordTypeEnum.valueOf(type);
    }

    public Date getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(final Date sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getDiscussedTopics() {
        return discussedTopics;
    }

    public void setDiscussedTopics(final String discussedTopics) {
        this.discussedTopics = discussedTopics;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SessionRecord [type=");
        builder.append(type);
        builder.append(", sessionDate=");
        builder.append(sessionDate);
        builder.append(", discussedTopics=");
        builder.append(discussedTopics);
        builder.append(", toString()=");
        builder.append(super.toString());
        builder.append("]");
        return builder.toString();
    }
}
