package com.svnavigatoru600.web.news;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.UserUtils;
import com.svnavigatoru600.viewmodel.news.AbstractNewEditNews;

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
        this.initVariables(messageSource, request);
    }

    void initVariables(MessageSource messageSource, HttpServletRequest request) {
        this.initDateVariables(request);
        this.initUserVariables();
        this.initMessageVariables(messageSource, request);
        this.initEditUrlVariables(request);
    }

    private void initDateVariables(HttpServletRequest request) {
        Locale locale = Localization.getLocale(request);

        News news = this.command.getNews();
        Date creationTime = news.getCreationTime();
        this.localizedMonth = DateUtils
                .format(creationTime, DateUtils.LONG_MONTH_FORMATS.get(locale), locale);
        this.localizedDay = DateUtils.format(creationTime, DateUtils.SHORT_DAY_FORMATS.get(locale), locale);
    }

    private void initUserVariables() {
        this.loggedUserCanEditNews = UserUtils.getLoggedUser().canEditNews();
    }

    private void initMessageVariables(MessageSource messageSource, HttpServletRequest request) {
        News news = this.command.getNews();
        this.localizedDeleteQuestion = ListNewsController.getLocalizedDeleteQuestion(news, messageSource,
                request);
    }

    private void initEditUrlVariables(HttpServletRequest request) {
        StringBuffer urlBuilder = new StringBuffer(request.getContextPath());
        urlBuilder.append(AbstractNewsController.BASE_URL);
        urlBuilder.append("existujici/");
        this.editUrlBeginning = urlBuilder.toString();
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
