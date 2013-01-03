package com.svnavigatoru600.domain;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import org.springframework.format.annotation.DateTimeFormat;

import com.svnavigatoru600.service.NewsService;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class News implements Serializable {

    private static final long serialVersionUID = 6948132683901305572L;

    private NewsService newsService;

    @Inject
    public void setNewsService(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * Updates the persisted copy of this object.
     */
    public void update() {
        this.newsService.update(this);
    }

    private int id;
    private String title;
    private String text;
    @DateTimeFormat(style = "M-")
    private Date creationTime;
    private Date lastSaveTime;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
