package svnavigatoru.web.users.administration;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;

import svnavigatoru.domain.users.User;
import svnavigatoru.repository.users.UserDao;
import svnavigatoru.service.users.UserDataValidator;

/**
 * Parent of all controllers which create and edit {@link User}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class NewEditUserController extends UserController {

	public static final String COMMAND = "userCommand";

	protected Validator validator;

	/**
	 * Constructor.
	 */
	public NewEditUserController(UserDao userDao, UserDataValidator validator, MessageSource messageSource) {
		super(userDao, messageSource);
		this.validator = validator;
	}
}
