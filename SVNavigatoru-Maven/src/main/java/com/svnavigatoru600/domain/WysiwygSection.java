package com.svnavigatoru600.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.svnavigatoru600.repository.WysiwygSectionDao;

public class WysiwygSection implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2762455439041490854L;

    private WysiwygSectionDao sectionDao;

    public void setWysiwygSectionDao(WysiwygSectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public void update() {
        this.sectionDao.update(this);
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
}
