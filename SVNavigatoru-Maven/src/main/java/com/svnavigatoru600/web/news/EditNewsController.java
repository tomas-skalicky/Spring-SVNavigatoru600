package com.svnavigatoru600.web.news;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.service.news.NewsService;
import com.svnavigatoru600.service.news.validator.EditNewsValidator;
import com.svnavigatoru600.viewmodel.news.EditNews;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditNewsController extends AbstractNewEditNewsController {

    private static final String REQUEST_MAPPING_BASE_URL = EditNewsController.BASE_URL
            + "existujici/{newsId}/";
    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "edit.changes-not-saved-due-to-database-error";

    /**
     * Constructor.
     */
    @Inject
    public EditNewsController(NewsService newsService, EditNewsValidator validator,
            MessageSource messageSource) {
        super(newsService, validator, messageSource);
    }

    /**
     * Initialises the form.
     */
    @RequestMapping(value = EditNewsController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    public @ResponseBody
    AbstractNewsResponse initForm(@PathVariable int newsId, HttpServletRequest request) {

        News news = this.getNewsService().findById(newsId);
        return new GoToEditFormResponse(news, this.getMessageSource(), request);
    }

    @RequestMapping(value = EditNewsController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.POST)
    @Transactional
    public @ResponseBody
    AbstractNewsResponse processSubmittedForm(@ModelAttribute(EditNewsController.COMMAND) EditNews command,
            BindingResult result, SessionStatus status, @PathVariable int newsId, HttpServletRequest request) {

        EditNewsResponse response = new EditNewsResponse(command);

        this.getValidator().validate(command, result);
        final MessageSource messageSource = this.getMessageSource();
        if (result.hasErrors()) {
            response.setFail(result, messageSource, request);
            return response;
        }

        final NewsService newsService = this.getNewsService();
        News originalNews = null;
        try {
            originalNews = newsService.findById(newsId);
            // Sets up because of the comparison of the creationTime and lastSaveTime in AJAX.
            command.setNews(originalNews);
            newsService.update(originalNews, command.getNews());

            // Clears the command object from the session.
            status.setComplete();

            response.setSuccess();
            return response;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(originalNews, e);
            result.reject(EditNewsController.DATABASE_ERROR_MESSAGE_CODE);
            response.setFail(result, messageSource, request);
            return response;
        }
    }
}
