package com.svnavigatoru600.web.news;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.service.news.NewsService;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.UserUtils;
import com.svnavigatoru600.viewmodel.news.AbstractNewEditNews;
import com.svnavigatoru600.web.url.news.NewsUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class NewNewsResponse extends AbstractNewEditNewsResponse {

    /**
     * All variables intended for visualisation of the new news in the list of news.
     */
    private String localizedMonth = null;
    private String localizedDay = null;
    private boolean loggedUserCanEditNews = false;
    private String localizedDeleteQuestion = null;
    private String editUrlBeginning = null;

    public NewNewsResponse(AbstractNewEditNews command) {
        super(command);
    }

    public void setSuccess(MessageSource messageSource, HttpServletRequest request) {
        super.setSuccess();
        initVariables(messageSource, request);
    }

    void initVariables(MessageSource messageSource, HttpServletRequest request) {
        initDateVariables(request);
        initUserVariables();
        initMessageVariables(messageSource, request);
        initEditUrlVariables(request);
    }

    private void initDateVariables(HttpServletRequest request) {
        Locale locale = Localization.getLocale(request);

        News news = getCommand().getNews();
        Date creationTime = news.getCreationTime();
        this.localizedMonth = DateUtils.format(creationTime, DateUtils.LONG_MONTH_FORMATS.get(locale), locale);
        this.localizedDay = DateUtils.format(creationTime, DateUtils.SHORT_DAY_FORMATS.get(locale), locale);
    }

    private void initUserVariables() {
        this.loggedUserCanEditNews = UserUtils.getLoggedUser().canEditNews();
    }

    private void initMessageVariables(MessageSource messageSource, HttpServletRequest request) {
        News news = getCommand().getNews();
        this.localizedDeleteQuestion = NewsService.getLocalizedDeleteQuestion(news, messageSource, request);
    }

    private void initEditUrlVariables(HttpServletRequest request) {
        this.editUrlBeginning = new StringBuffer(request.getContextPath()).append(NewsUrlParts.EXISTING_URL).toString();
    }

    public String getLocalizedMonth() {
        return this.localizedMonth;
    }

    public String getLocalizedDay() {
        return this.localizedDay;
    }

    public boolean isLoggedUserCanEditNews() {
        return this.loggedUserCanEditNews;
    }

    public String getLocalizedDeleteQuestion() {
        return this.localizedDeleteQuestion;
    }

    public String getEditUrlBeginning() {
        return this.editUrlBeginning;
    }
}
