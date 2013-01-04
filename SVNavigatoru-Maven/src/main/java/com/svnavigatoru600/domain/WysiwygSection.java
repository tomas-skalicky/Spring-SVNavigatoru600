package com.svnavigatoru600.domain;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import org.springframework.format.annotation.DateTimeFormat;

import com.svnavigatoru600.service.WysiwygSectionService;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class WysiwygSection implements Serializable {

    private static final long serialVersionUID = -2762455439041490854L;

    private WysiwygSectionService sectionService;

    @Inject
    public void setWysiwygSectionService(WysiwygSectionService sectionService) {
        this.sectionService = sectionService;
    }

    /**
     * Updates the persisted copy of this object.
     */
    public void update() {
        this.sectionService.update(this);
    }

    private WysiwygSectionName name;
    /**
     * Date set by the application, not by the user.
     */
    @DateTimeFormat(style = "LS")
    private Date lastSaveTime;
    private String sourceCode;

    /**
     * This getter is necessary because of Hibernate.
     */
    public String getName() {
        return this.name.name();
    }

    public void setName(WysiwygSectionName name) {
        this.name = name;
    }

    /**
     * This setter is necessary because of Hibernate.
     */
    public void setName(String name) {
        this.name = WysiwygSectionName.valueOf(name);
    }

    public Date getLastSaveTime() {
        return this.lastSaveTime;
    }

    public void setLastSaveTime(Date lastSaveTime) {
        this.lastSaveTime = lastSaveTime;
    }

    public String getSourceCode() {
        return this.sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    @Override
    public String toString() {
        return new StringBuilder("[name=").append(this.name).append(", lastSaveTime=")
                .append(this.lastSaveTime).append(", sourceCode=").append(this.sourceCode).append("]")
                .toString();
    }
}
