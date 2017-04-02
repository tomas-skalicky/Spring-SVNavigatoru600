package com.svnavigatoru600.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class WysiwygSection implements Serializable {

    private static final long serialVersionUID = 1L;

    private WysiwygSectionNameEnum name;
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
        return name.name();
    }

    public void setName(final WysiwygSectionNameEnum name) {
        this.name = name;
    }

    /**
     * This setter is necessary because of Hibernate.
     */
    public void setName(final String name) {
        this.name = WysiwygSectionNameEnum.valueOf(name);
    }

    public Date getLastSaveTime() {
        return lastSaveTime;
    }

    public void setLastSaveTime(final Date lastSaveTime) {
        this.lastSaveTime = lastSaveTime;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(final String sourceCode) {
        this.sourceCode = sourceCode;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("WysiwygSection [name=");
        builder.append(name);
        builder.append(", lastSaveTime=");
        builder.append(lastSaveTime);
        builder.append(", sourceCode=");
        builder.append(sourceCode);
        builder.append("]");
        return builder.toString();
    }
}
