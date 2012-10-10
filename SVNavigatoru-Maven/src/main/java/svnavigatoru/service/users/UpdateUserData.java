package svnavigatoru.service.users;

import org.springframework.stereotype.Service;

import svnavigatoru.domain.users.User;

/**
 * Holder of user's data when the user views and modifies them in the
 * <i>user-account.jsp</i> form.
 * 
 * @author Tomas Skalicky
 */
@Service
public class UpdateUserData {

	private User user = null;
	private String newPassword = "";
	private String newPasswordConfirmation = "";
	private boolean dataSaved = false;

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getNewPassword() {
		return this.newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getNewPasswordConfirmation() {
		return this.newPasswordConfirmation;
	}

	public void setNewPasswordConfirmation(String newPasswordConfirmation) {
		this.newPasswordConfirmation = newPasswordConfirmation;
	}

	public boolean isDataSaved() {
		return this.dataSaved;
	}

	public void setDataSaved(boolean dataSaved) {
		this.dataSaved = dataSaved;
	}
}
