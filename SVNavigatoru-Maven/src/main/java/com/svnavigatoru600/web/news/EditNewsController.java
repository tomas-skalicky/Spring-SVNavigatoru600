package com.svnavigatoru600.web.news;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.service.news.NewsService;
import com.svnavigatoru600.url.news.NewsUrlParts;
import com.svnavigatoru600.viewmodel.EditControllerSendNotification;
import com.svnavigatoru600.viewmodel.SendNotification;
import com.svnavigatoru600.viewmodel.news.EditNews;
import com.svnavigatoru600.viewmodel.news.validator.EditNewsValidator;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditNewsController extends AbstractNewEditNewsController {

    private static final class Vars {
        private static final String NEWS_ID = "newsId";

        private Vars() {
        }
    }

    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "edit.changes-not-saved-due-to-database-error";

    /**
     * Constructor.
     */
    @Inject
    public EditNewsController(final NewsService newsService, final EditNewsValidator validator,
            final MessageSource messageSource) {
        super(newsService, validator, messageSource);
    }

    /**
     * Initialises the form.
     */
    @GetMapping(value = NewsUrlParts.EXISTING_URL + "{" + Vars.NEWS_ID
            + "}/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody AbstractNewsResponse initForm(@PathVariable(Vars.NEWS_ID) final int newsId,
            final HttpServletRequest request) {

        final News news = getNewsService().findById(newsId);
        final MessageSource messageSource = getMessageSource();
        final SendNotification sendNotification = new EditControllerSendNotification(request, messageSource);
        return new GoToEditFormResponse(news, sendNotification, messageSource, request);
    }

    @PostMapping(value = NewsUrlParts.EXISTING_URL + "{" + Vars.NEWS_ID
            + "}/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Transactional
    public @ResponseBody AbstractNewsResponse processSubmittedForm(@PathVariable(Vars.NEWS_ID) final int newsId,
            @RequestBody final EditNews command, final BindingResult result, final SessionStatus status,
            final HttpServletRequest request) {

        final EditNewsResponse response = new EditNewsResponse(command);

        getValidator().validate(command, result);
        final MessageSource messageSource = getMessageSource();
        if (result.hasErrors()) {
            response.setFail(result, messageSource, request);
            return response;
        }

        final News newNews = command.getNews();
        final NewsService newsService = getNewsService();
        News originalNews = null;
        try {
            originalNews = newsService.findById(newsId);
            // Sets up because of the comparison of the creationTime and lastSaveTime in AJAX.
            command.setNews(originalNews);
            newsService.updateAndNotifyUsers(originalNews, newNews, command.getSendNotification().isStatus(), request,
                    messageSource);

            // Clears the command object from the session.
            status.setComplete();

            response.setSuccess();
            return response;

        } catch (final DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(originalNews, e);
            result.reject(EditNewsController.DATABASE_ERROR_MESSAGE_CODE);
            response.setFail(result, messageSource, request);
            return response;
        }
    }
}
