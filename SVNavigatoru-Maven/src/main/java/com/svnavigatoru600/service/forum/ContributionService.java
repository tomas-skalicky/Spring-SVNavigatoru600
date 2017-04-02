package com.svnavigatoru600.service.forum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.ForumContribution;
import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.impl.ContributionField;
import com.svnavigatoru600.service.SubjectOfNotificationService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.OrderType;

/**
 * Provides convenient methods to work with {@link com.svnavigatoru600.domain.forum.ForumContribution Contribution} objects.
 * <p>
 * For instance: There are methods which determine whether the currently logged-in user has rights to manipulate with a
 * particular {@link com.svnavigatoru600.domain.forum.ForumContribution Contribution}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class ContributionService implements SubjectOfNotificationService {

    /**
     * The object which provides a persistence.
     */
    private final ContributionDao contributionDao;
    /**
     * Does the work which concerns mainly notification of {@link User users}.
     */
    private UserService userService;
    /**
     * Assembles notification emails and sends them to authorized {@link User users}.
     */
    private ContributionNotificationEmailService emailService;

    @Inject
    public ContributionService(final ContributionDao contributionDao) {
        this.contributionDao = contributionDao;
    }

    @Inject
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    @Inject
    public void setEmailService(final ContributionNotificationEmailService emailService) {
        this.emailService = emailService;
    }

    @PreAuthorize("hasPermission(#contributionId, 'com.svnavigatoru600.domain.forum.Contribution', 'edit')")
    public void canEdit(final int contributionId) {
    }

    @PreAuthorize("hasPermission(#contributionId, 'com.svnavigatoru600.domain.forum.Contribution', 'delete')")
    public void canDelete(final int contributionId) {
    }

    /**
     * Returns a {@link ForumContribution} stored in the repository which has the given ID.
     */
    public ForumContribution findById(final int contributionId) {
        return contributionDao.findById(contributionId);
    }

    /**
     * Returns all {@link ForumContribution Contributions} stored in the repository which belong to the specified
     * {@link com.svnavigatoru600.domain.forum.ForumThread thread}.
     * 
     * @param threadId
     *            ID of the thread
     */
    public List<ForumContribution> findAll(final int threadId) {
        return contributionDao.findAll(threadId);
    }

    /**
     * Returns all {@link ForumContribution Contributions} stored in the repository arranged according to their
     * {@link ForumContribution#getLastSaveTime() last save times} descending.
     * <p>
     * NOT IMPLEMENTED YET: It returns only the first <code>maxResultSize</code> {@link ForumContribution Contributions}.
     */
    public List<ForumContribution> findLimitedNumberOrdered(final int maxResultSize) {
        return contributionDao.findAllOrdered(ContributionField.lastSaveTime, OrderType.DESCENDING, maxResultSize);
    }

    /**
     * Returns all {@link ForumContribution Contributions} stored in the repository arranged according to their
     * {@link ForumContribution#getCreationTime() creation times} ascending.
     * <p>
     * Moreover, the resulting {@link ForumContribution Contributions} are only those which belong to the specified
     * {@link com.svnavigatoru600.domain.forum.ForumThread Thread}.
     * 
     * @param threadId
     *            ID of the thread
     */
    public List<ForumContribution> findAllOrdered(final int threadId) {
        return contributionDao.findAllOrdered(threadId, ContributionField.creationTime, OrderType.ASCENDING);
    }

    /**
     * Updates the given {@link ForumContribution} in the repository. The old version of this contribution should be already
     * stored there.
     */
    public void update(final ForumContribution contribution) {
        contributionDao.update(contribution);
    }

    /**
     * Updates properties of the given <code>contributionToUpdate</code> and persists this {@link ForumContribution} into the
     * repository. The old version of this contribution should be already stored there.
     * 
     * @param contributionToUpdate
     *            Persisted {@link ForumContribution}
     * @param newContribution
     *            {@link ForumContribution} which contains new values of properties of <code>contributionToUpdate</code>.
     *            These values are copied to the persisted contribution.
     */
    public void update(final ForumContribution contributionToUpdate, final ForumContribution newContribution) {
        contributionToUpdate.setText(newContribution.getText());
        this.update(contributionToUpdate);
    }

    /**
     * Updates properties of the given <code>contributionToUpdate</code> and persists this {@link ForumContribution} into the
     * repository. The old version of this contribution should be already stored there.
     * <p>
     * Finally, notifies all users which have corresponding rights by email about changes in the contribution.
     * 
     * @param contributionToUpdate
     *            Persisted {@link ForumContribution}
     * @param newContribution
     *            {@link ForumContribution} which contains new values of properties of <code>contributionToUpdate</code>.
     *            These values are copied to the persisted contribution.
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void updateAndNotifyUsers(final ForumContribution contributionToUpdate, final ForumContribution newContribution,
            final boolean sendNotification, final HttpServletRequest request, final MessageSource messageSource) {
        this.update(contributionToUpdate, newContribution);

        if (sendNotification) {
            notifyUsersOfUpdate(contributionToUpdate, request, messageSource);
        }
    }

    @Override
    public List<User> gainUsersToNotify() {
        return userService.findAllWithEmailByAuthorityAndSubscription(AuthorityType.ROLE_MEMBER_OF_SV,
                emailService.getNotificationType());
    }

    @Override
    public void notifyUsersOfUpdate(final Object updatedContribution, final HttpServletRequest request,
            final MessageSource messageSource) {
        emailService.sendEmailOnUpdate(updatedContribution, gainUsersToNotify(), request, messageSource);
    }

    /**
     * Stores the given {@link ForumContribution} to the repository.
     * 
     * @return New ID of the given {@link ForumContribution} generated by the repository
     */
    public int save(final ForumContribution contribution) {
        contribution.setId(contributionDao.save(contribution));
        return contribution.getId();
    }

    /**
     * Updates corresponding {@link java.util.Date Date} fields of the given new {@link ForumContribution} (if there are any
     * appropriate) and stores the contribution to the repository.
     * <p>
     * Finally, notifies all users which have corresponding rights by email about a creation of the contribution.
     * 
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void saveAndNotifyUsers(final ForumContribution newContribution, final boolean sendNotification, final HttpServletRequest request,
            final MessageSource messageSource) {
        save(newContribution);

        if (sendNotification) {
            notifyUsersOfCreation(newContribution, request, messageSource);
        }
    }

    @Override
    public void notifyUsersOfCreation(final Object newContribution, final HttpServletRequest request, final MessageSource messageSource) {
        emailService.sendEmailOnCreation(newContribution, gainUsersToNotify(), request, messageSource);
    }

    /**
     * Deletes the given {@link ForumContribution} from the repository.
     */
    public void delete(final ForumContribution contribution) {
        contributionDao.delete(contribution);
    }

    /**
     * Deletes the specified {@link ForumContribution} from the repository.
     * 
     * @param contributionId
     *            ID of the contribution
     */
    public void delete(final int contributionId) {
        final ForumContribution contribution = findById(contributionId);
        contributionDao.delete(contribution);
    }

    /**
     * Gets a {@link Map} which for each input {@link ForumContribution} contains a corresponding localized delete question
     * which is asked before deletion of that contribution.
     */
    public static Map<ForumContribution, String> getLocalizedDeleteQuestions(final List<ForumContribution> contributions,
            final HttpServletRequest request, final MessageSource messageSource) {
        final String messageCode = "forum.contributions.do-you-really-want-to-delete-contribution";
        final String question = Localization.findLocaleMessage(messageSource, request, messageCode);
        final Map<ForumContribution, String> questions = new HashMap<ForumContribution, String>();

        for (final ForumContribution contribution : contributions) {
            questions.put(contribution, question);
        }
        return questions;
    }
}
