package com.svnavigatoru600.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class News implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private String text;
    @DateTimeFormat(style = "M-")
    private Date creationTime;
    private Date lastSaveTime;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
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

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("News [id=");
        builder.append(id);
        builder.append(", title=");
        builder.append(title);
        builder.append(", text=");
        builder.append(text);
        builder.append(", creationTime=");
        builder.append(creationTime);
        builder.append(", lastSaveTime=");
        builder.append(lastSaveTime);
        builder.append("]");
        return builder.toString();
    }
}
