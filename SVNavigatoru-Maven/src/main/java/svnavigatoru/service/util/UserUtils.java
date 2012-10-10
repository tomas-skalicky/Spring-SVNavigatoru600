package svnavigatoru.service.util;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import svnavigatoru.domain.users.User;
import svnavigatoru.repository.users.UserDao;

/**
 * Provides a set of static functions related to users.
 * 
 * @author Tomas Skalicky
 */
public class UserUtils {

	/**
	 * Indicates whether the current user in the application is logged.
	 */
	public static boolean isLogged() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return (auth != null) && (auth.getPrincipal() instanceof User);
	}

	/**
	 * Gets the user who is currently logged in the application.
	 * 
	 * <b>Precondition:</b> The User.isLogged function must be <code>true</code>
	 * .
	 */
	public static User getLoggedUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/**
	 * Looks up in the repository and finds out whether an {@link User} with the
	 * given <code>username</code> exists, or not.
	 * 
	 * @return <code>true</code> the corresponding {@link User} exists.
	 */
	public static boolean isUsernameOccupied(String username, UserDao userDao) {
		try {
			userDao.findByUsername(username);
			return true;
		} catch (DataRetrievalFailureException e) {
			return false;
		}
	}

	/**
	 * Looks up in the repository and finds out whether an {@link User} with the
	 * given <code>emailAddress</code> exists, or not.
	 * 
	 * @return <code>true</code> the corresponding {@link User} exists.
	 */
	public static boolean isEmailOccupied(String emailAddress, UserDao userDao) {
		try {
			userDao.findByEmail(emailAddress);
			return true;
		} catch (DataRetrievalFailureException e) {
			return false;
		}
	}
}
