package com.svnavigatoru600.web.users;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.users.NotificationSubscriberVisitor;
import com.svnavigatoru600.domain.users.NotificationTypeEnum;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.UserUtils;
import com.svnavigatoru600.url.users.UserAccountUrlParts;
import com.svnavigatoru600.viewmodel.users.UpdateUserData;
import com.svnavigatoru600.viewmodel.users.validator.UpdateUserDataValidator;
import com.svnavigatoru600.web.AbstractMetaController;
import com.svnavigatoru600.web.AbstractPrivateSectionMetaController;

/**
 * The controller bound to the <i>user-account.jsp</i> and <i>user-administration.jsp</i> form. For more details about
 * the concepts used here, see the <i>ForgottenPasswordController</i> controller.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditMyAccountController extends AbstractPrivateSectionMetaController {

    private static final String COMMAND = "updateUserDataCommand";
    private static final String PAGE_VIEW = "editMyUserAccount";

    private final UserService userService;
    private final Validator validator;
    private final MessageSource messageSource;

    @Inject
    public EditMyAccountController(final UserService userService, final UpdateUserDataValidator validator,
            final MessageSource messageSource) {
        LogFactory.getLog(this.getClass()).debug("The UserAccountController object created.");
        this.userService = userService;
        this.validator = validator;
        this.messageSource = messageSource;
    }

    /**
     * Initializes the form.
     */
    @GetMapping(value = UserAccountUrlParts.BASE_URL)
    @PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
    public String initForm(final HttpServletRequest request, final ModelMap model) {

        final UpdateUserData command = new UpdateUserData();

        final User user = UserUtils.getLoggedUser();
        command.setUser(user);

        // Sets up all auxiliary (but necessary) maps.
        command.setLocalizedNotificationCheckboxTitles(
                UserService.getLocalizedNotificationTitles(request, messageSource));

        model.addAttribute(EditMyAccountController.COMMAND, command);
        return EditMyAccountController.PAGE_VIEW;
    }

    /**
     * Initializes the form after the modified data were successfully saved to a repository.
     */
    @GetMapping(value = UserAccountUrlParts.SAVED_URL)
    @PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
    public String initFormAfterSave(final HttpServletRequest request, final ModelMap model) {
        final String view = initForm(request, model);
        ((UpdateUserData) model.get(EditMyAccountController.COMMAND)).setDataSaved(true);
        return view;
    }

    /**
     * If values in the form are OK, update date of the current {@link User}. Otherwise, returns back to the form.
     *
     * @return The name of the view which is to be shown.
     */
    @PostMapping(value = UserAccountUrlParts.BASE_URL)
    @PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
    @Transactional
    public String processSubmittedForm(@ModelAttribute(EditMyAccountController.COMMAND) final UpdateUserData command,
            final BindingResult result, final SessionStatus status, final HttpServletRequest request, final ModelMap model) {

        // Sets up all auxiliary (but necessary) maps.
        // It is necessary to do it again since they are not push as a part of the command back. They have
        // been set to null.
        command.setLocalizedNotificationCheckboxTitles(
                UserService.getLocalizedNotificationTitles(request, messageSource));

        validator.validate(command, result);
        if (result.hasErrors()) {
            return EditMyAccountController.PAGE_VIEW;
        }

        User originalUser = null;
        try {
            // We cannot use the User object from the session, i.e. Users.getLoggedUser() since
            // if the new user's data (e.g. email) were not valid, e.g. because of the occupied email,
            // the old valid user's logging data would be available anymore. They were removed by the new
            // invalid data.
            originalUser = userService.findByUsername(UserUtils.getLoggedUser().getUsername());
            userService.update(originalUser, command.getUser(), command.getNewPassword());

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, UserAccountUrlParts.SAVED_URL);
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (final DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(originalUser, e);
            result.rejectValue("user.email", "email.occupied-by-somebody");
            return EditMyAccountController.PAGE_VIEW;
        }
    }

    /**
     * Unsubscribes notifications of the given <code>notificationType</code> and redirects processing in order to avoid
     * re-submitting.
     *
     * @param username
     *            Username (= login) of the user who is to be unsubscribed from receiving notifications of the
     *            <code>notificationType</code>.
     * @param notificationTypeOrdinal
     *            Ordinal of notifications' type which is to be deactivated by the user.
     */
    @GetMapping(value = UserAccountUrlParts.BASE_URL + "{username}/" + UserAccountUrlParts.UNSUBSCRIBE_EXTENSION
            + "{notificationType}/")
    @PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
    public String unsubscribeNotificationsAndRedirect(@PathVariable("username") final String username,
            @PathVariable("notificationType") final int notificationTypeOrdinal, final HttpServletRequest request, final ModelMap model) {
        final User loggedUser = UserUtils.getLoggedUser();
        if (!loggedUser.getUsername().equals(username)) {
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE,
                    UserAccountUrlParts.UNSUBSCRIBE_FAILED_FOREIGN_ACCOUNT_URL + username + "/");
            return AbstractMetaController.REDIRECTION_PAGE;
        }

        final NotificationTypeEnum notificationType = NotificationTypeEnum.values()[notificationTypeOrdinal];
        try {
            userService.unsubscribeFromNotifications(username, notificationType);

            // It is necessary to update the instance which is in the session.
            notificationType.accept(new NotificationSubscriberVisitor(loggedUser, false));

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, UserAccountUrlParts.UNSUBSCRIBE_URL
                    + notificationTypeOrdinal + "/" + UserAccountUrlParts.UNSUBSCRIBED_EXTENSION);
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (final DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass())
                    .error(String.format("DB error: username=%s, notificationType=%d", username, notificationType), e);
            return EditMyAccountController.PAGE_VIEW;
        }
    }

    /**
     * Initializes the form after the given <code>notificationType</code> have been successfully unsubscribed and these
     * changes have been persisted.
     */
    @GetMapping(value = UserAccountUrlParts.UNSUBSCRIBE_URL + "{notificationType}/"
            + UserAccountUrlParts.UNSUBSCRIBED_EXTENSION)
    @PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
    public String initFormAfterUnsubscription(@PathVariable("notificationType") final int notificationType,
            final HttpServletRequest request, final ModelMap model) {
        final String view = initForm(request, model);

        final UpdateUserData command = (UpdateUserData) model.get(EditMyAccountController.COMMAND);
        command.setNotificationsUnsubscribed(true);

        final String localizationCode = NotificationTypeEnum.values()[notificationType].getTitleLocalizationCode();
        final String localizedTitle = Localization.findLocaleMessage(messageSource, request, localizationCode);
        command.setLocalizedUnsubscribedNotificationTitle(localizedTitle);
        return view;
    }

    /**
     * Initializes the form after a fail of unsubscription due to a sign-in in a different account than in that which
     * stands in a notification email. Shows an error message.
     * <p>
     * It looks like that the currently logged user has either an access to more than one account or has an access to an
     * email inbox of foreign account.
     */
    @GetMapping(value = UserAccountUrlParts.UNSUBSCRIBE_FAILED_FOREIGN_ACCOUNT_URL + "{username}/")
    @PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
    public String initFormAfterUnsubscriptionFail(@PathVariable("username") final String username, final HttpServletRequest request,
            final ModelMap model) {
        final String view = initForm(request, model);

        final UpdateUserData command = (UpdateUserData) model.get(EditMyAccountController.COMMAND);
        command.setForeignAccountDuringUnsubscription(true);
        command.setForeignAccountUsername(username);
        return view;
    }
}
