package svnavigatoru.web.news;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import svnavigatoru.domain.News;
import svnavigatoru.repository.NewsDao;

@Controller
public class DeleteNewsController extends NewsController {

	private static final String REQUEST_MAPPING_BASE_URL = DeleteNewsController.BASE_URL
			+ "existujici/{newsId}/smazat/";
	/**
	 * Code of the database error message used when the {@link DataAccessException} is thrown.
	 */
	public static final String DATABASE_ERROR_MESSAGE_CODE = "news.deletion-failed-due-to-database-error";

	/**
	 * Constructor.
	 */
	@Autowired
	public DeleteNewsController(NewsDao newsDao, MessageSource messageSource) {
		super(newsDao, messageSource);
	}

	@RequestMapping(value = DeleteNewsController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
	public @ResponseBody
	NewsResponse delete(@PathVariable int newsId, HttpServletRequest request, ModelMap model) {

		DeleteNewsResponse response = new DeleteNewsResponse();

		try {
			// Deletes the news from the repository.
			News news = this.newsDao.findById(newsId);
			this.newsDao.delete(news);

			response.setSuccess();

		} catch (DataAccessException e) {
			// We encountered a database problem.
			this.logger.error(e);
			response.setFail(DeleteNewsController.DATABASE_ERROR_MESSAGE_CODE, this.messageSource, request);
		}
		return response;
	}
}
