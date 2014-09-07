package com.svnavigatoru600.web.news;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.service.news.NewsService;
import com.svnavigatoru600.url.news.NewsUrlParts;
import com.svnavigatoru600.viewmodel.NewControllerSendNotification;
import com.svnavigatoru600.viewmodel.SendNotification;
import com.svnavigatoru600.viewmodel.news.NewNews;
import com.svnavigatoru600.viewmodel.news.validator.NewNewsValidator;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class NewNewsController extends AbstractNewEditNewsController {

    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "news.adding-failed-due-to-database-error";

    /**
     * Constructor.
     */
    @Inject
    public NewNewsController(NewsService newsService, NewNewsValidator validator, MessageSource messageSource) {
        super(newsService, validator, messageSource);
    }

    /**
     * Initialises the form.
     */
    @RequestMapping(value = NewsUrlParts.NEW_URL, method = RequestMethod.GET)
    public @ResponseBody
    AbstractNewsResponse initForm(HttpServletRequest request, ModelMap model) {

        NewNews command = new NewNews();

        News news = new News();
        command.setNews(news);
        MessageSource messageSource = getMessageSource();
        SendNotification sendNotification = new NewControllerSendNotification(request, messageSource);
        command.setSendNotification(sendNotification);

        model.addAttribute(AbstractNewEditNewsController.COMMAND, command);
        return new GoToNewFormResponse(news, sendNotification, messageSource, request);
    }

    /**
     * If values in the form are OK, the new news is stored to the repository. Otherwise, returns back to the
     * form.
     * 
     * @return Response in the JSON format
     */
    @RequestMapping(value = NewsUrlParts.NEW_URL, method = RequestMethod.POST)
    @Transactional
    public @ResponseBody
    AbstractNewsResponse processSubmittedForm(@ModelAttribute(NewNewsController.COMMAND) NewNews command,
            BindingResult result, SessionStatus status, HttpServletRequest request) {

        NewNewsResponse response = new NewNewsResponse(command);

        getValidator().validate(command, result);
        MessageSource messageSource = getMessageSource();
        if (result.hasErrors()) {
            response.setFail(result, messageSource, request);
            return response;
        }

        // Updates the data of the new news.
        News newNews = command.getNews();

        try {
            // Saves the news to the repository.
            getNewsService().saveAndNotifyUsers(newNews, command.getSendNotification().isStatus(), request,
                    messageSource);

            // Clears the command object from the session.
            status.setComplete();

            response.setSuccess(messageSource, request);
            return response;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(newNews, e);
            result.reject(NewNewsController.DATABASE_ERROR_MESSAGE_CODE);
            response.setFail(result, messageSource, request);
            return response;
        }
    }
}
