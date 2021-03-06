package com.svnavigatoru600.viewmodel.forum.contributions;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.svnavigatoru600.domain.forum.ForumContribution;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.Localization;

/**
 * Used in the {@link com.svnavigatoru600.web.AbstractPrivateSectionMetaController
 * AbstractPrivateSectionMetaController}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class ContributionWrapper {

    private ForumContribution contribution = null;
    private String defaultDateTimeFormattedLastSaveTime = null;

    public ContributionWrapper(ForumContribution contribution, HttpServletRequest request) {
        this.contribution = contribution;
        Locale locale = Localization.getLocale(request);
        this.defaultDateTimeFormattedLastSaveTime = DateUtils.format(contribution.getLastSaveTime(),
                DateUtils.DEFAULT_DATE_TIME_FORMATS.get(locale), locale);
    }

    public ForumContribution getContribution() {
        return this.contribution;
    }

    public void setContribution(ForumContribution contribution) {
        this.contribution = contribution;
    }

    public String getDefaultDateTimeFormattedLastSaveTime() {
        return this.defaultDateTimeFormattedLastSaveTime;
    }

    public void setDefaultDateTimeFormattedLastSaveTime(String defaultDateTimeFormattedLastSaveTime) {
        this.defaultDateTimeFormattedLastSaveTime = defaultDateTimeFormattedLastSaveTime;
    }
}
