package com.svnavigatoru600.domain.forum;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.forum.ContributionDao;

public class Contribution implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6558138282365185827L;

    @SuppressWarnings("unused")
    private ContributionDao contributionDao;

    public void setContributionDao(ContributionDao contributionDao) {
        this.contributionDao = contributionDao;
    }

    private int id;
    private Thread thread;
    private String text;
    @DateTimeFormat(style = "LS")
    private Date creationTime;
    @DateTimeFormat(style = "LS")
    private Date lastSaveTime;
    private User author;

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
}
