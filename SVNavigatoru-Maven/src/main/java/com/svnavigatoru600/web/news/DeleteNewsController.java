package com.svnavigatoru600.web.news;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.svnavigatoru600.service.news.NewsService;
import com.svnavigatoru600.web.url.CommonUrlParts;
import com.svnavigatoru600.web.url.news.NewsUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class DeleteNewsController extends AbstractNewsController {

    /**
     * Code of the database error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "news.deletion-failed-due-to-database-error";

    @Inject
    public DeleteNewsController(final NewsService newsService, final MessageSource messageSource) {
        super(newsService, messageSource);
    }

    @GetMapping(value = NewsUrlParts.EXISTING_URL + "{newsId}/" + CommonUrlParts.DELETE_EXTENSION)
    @Transactional
    public @ResponseBody AbstractNewsResponse delete(@PathVariable final int newsId, final HttpServletRequest request,
            final ModelMap model) {

        final DeleteNewsResponse response = new DeleteNewsResponse();

        try {
            getNewsService().delete(newsId);

            response.setSuccess();

        } catch (final DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(e);
            response.setFail(DeleteNewsController.DATABASE_ERROR_MESSAGE_CODE, getMessageSource(), request);
        }
        return response;
    }
}
