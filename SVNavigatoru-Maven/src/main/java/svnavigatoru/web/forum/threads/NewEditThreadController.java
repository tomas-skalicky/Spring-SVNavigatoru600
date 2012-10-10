package svnavigatoru.web.forum.threads;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;

import svnavigatoru.domain.forum.Thread;
import svnavigatoru.repository.forum.ThreadDao;
import svnavigatoru.service.forum.threads.ThreadValidator;

/**
 * Parent of controllers which create and edit the {@link Thread}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class NewEditThreadController extends ThreadController {

	/**
	 * Command used in /main-content/forum/threads/new-edit-thread.jsp.
	 */
	public static final String COMMAND = "newEditThreadCommand";
	protected Validator validator;

	public NewEditThreadController(ThreadDao threadDao, ThreadValidator validator, MessageSource messageSource) {
		super(threadDao, messageSource);
		this.validator = validator;
	}
}
