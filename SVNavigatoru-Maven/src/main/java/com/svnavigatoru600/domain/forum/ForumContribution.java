package com.svnavigatoru600.domain.forum;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.svnavigatoru600.domain.users.User;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class ForumContribution implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String CLASS_FULL_NAME = "com.svnavigatoru600.domain.forum.ForumContribution";

    private int id;
    private ForumThread thread;
    private String text;
    @DateTimeFormat(style = "LS")
    private Date creationTime;
    @DateTimeFormat(style = "LS")
    private Date lastSaveTime;
    private User author;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public ForumThread getThread() {
        return thread;
    }

    public void setThread(final ForumThread thread) {
        this.thread = thread;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(final Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastSaveTime() {
        return lastSaveTime;
    }

    public void setLastSaveTime(final Date lastSaveTime) {
        this.lastSaveTime = lastSaveTime;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(final User author) {
        this.author = author;
    }

    /**
     * ATTENTION: Customized toString!
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Contribution [id=");
        builder.append(id);
        builder.append(", thread.id=");
        builder.append(thread.getId());
        builder.append(", text=");
        builder.append(text);
        builder.append(", creationTime=");
        builder.append(creationTime);
        builder.append(", lastSaveTime=");
        builder.append(lastSaveTime);
        builder.append(", author=");
        builder.append(author);
        builder.append("]");
        return builder.toString();
    }
}
