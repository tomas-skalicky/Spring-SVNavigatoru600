package com.svnavigatoru600.domain.forum;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import org.springframework.format.annotation.DateTimeFormat;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.forum.ContributionService;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class Contribution implements Serializable {

    private static final long serialVersionUID = 6558138282365185827L;

    @SuppressWarnings("unused")
    private ContributionService contributionService;

    @Inject
    public void setContributionService(ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    private int id;
    private Thread thread;
    private String text;
    @DateTimeFormat(style = "LS")
    private Date creationTime;
    @DateTimeFormat(style = "LS")
    private Date lastSaveTime;
    private User author;

    /**
     * Initialises no property.
     */
    public Contribution() {
    }

    /**
     * Initialises contribution's text and author. Other properties are not touched.
     */
    public Contribution(String text, User author) {
        this.text = text;
        this.author = author;
    }

    /**
     * Initialises contribution's thread, text and author. Other properties are not touched.
     */
    public Contribution(Thread thread, String text, User author) {
        this.thread = thread;
        this.text = text;
        this.author = author;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Thread getThread() {
        return this.thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreationTime() {
        return this.creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastSaveTime() {
        return this.lastSaveTime;
    }

    public void setLastSaveTime(Date lastSaveTime) {
        this.lastSaveTime = lastSaveTime;
    }

    public User getAuthor() {
        return this.author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return new StringBuilder("[id=").append(this.id).append(", thread=").append(this.thread).append(", text=")
                .append(this.text).append(", creationTime=").append(this.creationTime).append(", lastSaveTime=")
                .append(this.lastSaveTime).append(", author=").append(this.author).append("]").toString();
    }
}
