package com.svnavigatoru600.service.forum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.impl.ContributionField;
import com.svnavigatoru600.service.SubjectOfNotificationService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.OrderType;

/**
 * Provides convenient methods to work with {@link com.svnavigatoru600.domain.forum.Contribution Contribution} objects.
 * <p>
 * For instance: There are methods which determine whether the currently logged-in user has rights to manipulate with a
 * particular {@link com.svnavigatoru600.domain.forum.Contribution Contribution}.
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
     * Returns a {@link Contribution} stored in the repository which has the given ID.
     */
    public Contribution findById(final int contributionId) {
        return contributionDao.findById(contributionId);
    }

    /**
     * Returns all {@link Contribution Contributions} stored in the repository which belong to the specified
     * {@link com.svnavigatoru600.domain.forum.Thread thread}.
     * 
     * @param threadId
     *            ID of the thread
     */
    public List<Contribution> findAll(final int threadId) {
        return contributionDao.findAll(threadId);
    }

    /**
     * Returns all {@link Contribution Contributions} stored in the repository arranged according to their
     * {@link Contribution#getLastSaveTime() last save times} descending.
     * <p>
     * NOT IMPLEMENTED YET: It returns only the first <code>maxResultSize</code> {@link Contribution Contributions}.
     */
    public List<Contribution> findLimitedNumberOrdered(final int maxResultSize) {
        return contributionDao.findAllOrdered(ContributionField.lastSaveTime, OrderType.DESCENDING, maxResultSize);
    }

    /**
     * Returns all {@link Contribution Contributions} stored in the repository arranged according to their
     * {@link Contribution#getCreationTime() creation times} ascending.
     * <p>
     * Moreover, the resulting {@link Contribution Contributions} are only those which belong to the specified
     * {@link com.svnavigatoru600.domain.forum.Thread Thread}.
     * 
     * @param threadId
     *            ID of the thread
     */
    public List<Contribution> findAllOrdered(final int threadId) {
        return contributionDao.findAllOrdered(threadId, ContributionField.creationTime, OrderType.ASCENDING);
    }

    /**
     * Updates the given {@link Contribution} in the repository. The old version of this contribution should be already
     * stored there.
     */
    public void update(final Contribution contribution) {
        contributionDao.update(contribution);
    }

    /**
     * Updates properties of the given <code>contributionToUpdate</code> and persists this {@link Contribution} into the
     * repository. The old version of this contribution should be already stored there.
     * 
     * @param contributionToUpdate
     *            Persisted {@link Contribution}
     * @param newContribution
     *            {@link Contribution} which contains new values of properties of <code>contributionToUpdate</code>.
     *            These values are copied to the persisted contribution.
     */
    public void update(final Contribution contributionToUpdate, final Contribution newContribution) {
        contributionToUpdate.setText(newContribution.getText());
        this.update(contributionToUpdate);
    }

    /**
     * Updates properties of the given <code>contributionToUpdate</code> and persists this {@link Contribution} into the
     * repository. The old version of this contribution should be already stored there.
     * <p>
     * Finally, notifies all users which have corresponding rights by email about changes in the contribution.
     * 
     * @param contributionToUpdate
     *            Persisted {@link Contribution}
     * @param newContribution
     *            {@link Contribution} which contains new values of properties of <code>contributionToUpdate</code>.
     *            These values are copied to the persisted contribution.
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void updateAndNotifyUsers(final Contribution contributionToUpdate, final Contribution newContribution,
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
     * Stores the given {@link Contribution} to the repository.
     * 
     * @return New ID of the given {@link Contribution} generated by the repository
     */
    public int save(final Contribution contribution) {
        contribution.setId(contributionDao.save(contribution));
        return contribution.getId();
    }

    /**
     * Updates corresponding {@link java.util.Date Date} fields of the given new {@link Contribution} (if there are any
     * appropriate) and stores the contribution to the repository.
     * <p>
     * Finally, notifies all users which have corresponding rights by email about a creation of the contribution.
     * 
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void saveAndNotifyUsers(final Contribution newContribution, final boolean sendNotification, final HttpServletRequest request,
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
     * Deletes the given {@link Contribution} from the repository.
     */
    public void delete(final Contribution contribution) {
        contributionDao.delete(contribution);
    }

    /**
     * Deletes the specified {@link Contribution} from the repository.
     * 
     * @param contributionId
     *            ID of the contribution
     */
    public void delete(final int contributionId) {
        final Contribution contribution = findById(contributionId);
        contributionDao.delete(contribution);
    }

    /**
     * Gets a {@link Map} which for each input {@link Contribution} contains a corresponding localized delete question
     * which is asked before deletion of that contribution.
     */
    public static Map<Contribution, String> getLocalizedDeleteQuestions(final List<Contribution> contributions,
            final HttpServletRequest request, final MessageSource messageSource) {
        final String messageCode = "forum.contributions.do-you-really-want-to-delete-contribution";
        final String question = Localization.findLocaleMessage(messageSource, request, messageCode);
        final Map<Contribution, String> questions = new HashMap<Contribution, String>();

        for (final Contribution contribution : contributions) {
            questions.put(contribution, question);
        }
        return questions;
    }
}
