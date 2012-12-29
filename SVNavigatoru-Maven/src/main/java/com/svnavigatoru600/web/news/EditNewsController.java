package com.svnavigatoru600.web.news;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.svnavigatoru600.repository.NewsDao;
import com.svnavigatoru600.service.news.EditNews;
import com.svnavigatoru600.service.news.validator.EditNewsValidator;

@Controller
public class EditNewsController extends NewEditNewsController {

    private static final String REQUEST_MAPPING_BASE_URL = EditNewsController.BASE_URL
            + "existujici/{newsId}/";
    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "edit.changes-not-saved-due-to-database-error";

    /**
     * Constructor.
     */
    @Autowired
    public EditNewsController(NewsDao newsDao, EditNewsValidator validator, MessageSource messageSource) {
        super(newsDao, validator, messageSource);
    }

    /**
     * Initialises the form.
     */
    @RequestMapping(value = EditNewsController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    public @ResponseBody
    NewsResponse initForm(@PathVariable int newsId, HttpServletRequest request) {

        News news = this.newsDao.findById(newsId);
        return new GoToEditFormResponse(news, this.messageSource, request);
    }

    @RequestMapping(value = EditNewsController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.POST)
    @Transactional
    public @ResponseBody
    NewsResponse processSubmittedForm(@ModelAttribute(EditNewsController.COMMAND) EditNews command,
            BindingResult result, SessionStatus status, @PathVariable int newsId, HttpServletRequest request) {

        EditNewsResponse response = new EditNewsResponse(command);

        this.validator.validate(command, result);
        if (result.hasErrors()) {
            response.setFail(result, this.messageSource, request);
            return response;
        }

        // Updates the original data.
        News originalNews = this.newsDao.findById(newsId);
        News newNews = command.getNews();
        originalNews.setTitle(newNews.getTitle());
        originalNews.setText(newNews.getText());
        // Sets up because of the comparison of the creationTime and lastSaveTime in AJAX.
        command.setNews(originalNews);

        try {
            // Updates the news in the repository.
            this.newsDao.update(originalNews);

            // Clears the command object from the session.
            status.setComplete();

            response.setSuccess();
            return response;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(originalNews, e);
            result.reject(EditNewsController.DATABASE_ERROR_MESSAGE_CODE);
            response.setFail(result, this.messageSource, request);
            return response;
        }
    }
}
