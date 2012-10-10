package svnavigatoru.web.news;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;

import svnavigatoru.domain.News;
import svnavigatoru.repository.NewsDao;
import svnavigatoru.service.news.NewsValidator;

/**
 * Parent of controllers which create and edit the {@link News}.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class NewEditNewsController extends NewsController {

	/**
	 * Command used in /main-content/news/new-edit-news.jsp.
	 */
	public static final String COMMAND = "newEditNewsCommand";
	protected Validator validator;

	public NewEditNewsController(NewsDao newsDao, NewsValidator validator, MessageSource messageSource) {
		super(newsDao, messageSource);
		this.validator = validator;
	}
}
