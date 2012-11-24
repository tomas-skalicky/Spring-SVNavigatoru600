package com.svnavigatoru600.service.forum.contributions;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.web.PrivateSectionMetaController;


/**
 * Used in the {@link PrivateSectionMetaController}.
 * 
 * @author Tomas Skalicky
 */
public class ContributionWrapper {

	private Contribution contribution = null;
	private String defaultDateTimeFormattedLastSaveTime = null;

	public ContributionWrapper(Contribution contribution, HttpServletRequest request) {
		this.contribution = contribution;
		Locale locale = Localization.getLocale(request);
		this.defaultDateTimeFormattedLastSaveTime = DateUtils.format(contribution.getLastSaveTime(),
				DateUtils.DEFAULT_DATE_TIME_FORMATS.get(locale), locale);
	}

	public Contribution getContribution() {
		return this.contribution;
	}

	public void setContribution(Contribution contribution) {
		this.contribution = contribution;
	}

	public String getDefaultDateTimeFormattedLastSaveTime() {
		return this.defaultDateTimeFormattedLastSaveTime;
	}

	public void setDefaultDateTimeFormattedLastSaveTime(String defaultDateTimeFormattedLastSaveTime) {
		this.defaultDateTimeFormattedLastSaveTime = defaultDateTimeFormattedLastSaveTime;
	}
}
