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
 * Provides convenient methods to work with {@link com.svnavigatoru600.domain.forum.Contribution Contribution}
 * objects.
 * <p>
 * For instance: There are methods which determine whether the currently logged-in user has rights to
 * manipulate with a particular {@link com.svnavigatoru600.domain.forum.Contribution Contribution}.
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

    /**
     * Constructor.
     */
    @Inject
    public ContributionService(ContributionDao contributionDao) {
        this.contributionDao = contributionDao;
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Inject
    public void setEmailService(ContributionNotificationEmailService emailService) {
        this.emailService = emailService;
    }

    @PreAuthorize("hasPermission(#contributionId, 'com.svnavigatoru600.domain.forum.Contribution', 'edit')")
    public void canEdit(int contributionId) {
    }

    @PreAuthorize("hasPermission(#contributionId, 'com.svnavigatoru600.domain.forum.Contribution', 'delete')")
    public void canDelete(int contributionId) {
    }

    /**
     * Returns a {@link Contribution} stored in the repository which has the given ID.
     */
    public Contribution findById(int contributionId) {
        return this.contributionDao.findById(contributionId);
    }

    /**
     * Returns all {@link Contribution Contributions} stored in the repository which belong to the specified
     * {@link com.svnavigatoru600.domain.forum.Thread thread}.
     * 
     * @param threadId
     *            ID of the thread
     */
    public List<Contribution> findAll(int threadId) {
        return this.contributionDao.findAll(threadId);
    }

    /**
     * Returns all {@link Contribution Contributions} stored in the repository arranged according to their
     * {@link Contribution#getLastSaveTime() last save times} descending.
     * <p>
     * NOT IMPLEMENTED YET: It returns only the first <code>maxResultSize</code> {@link Contribution
     * Contributions}.
     */
    public List<Contribution> findLimitedNumberOrdered(int maxResultSize) {
        return this.contributionDao.findAllOrdered(ContributionField.lastSaveTime, OrderType.DESCENDING,
                maxResultSize);
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
    public List<Contribution> findAllOrdered(int threadId) {
        return this.contributionDao.findAllOrdered(threadId, ContributionField.creationTime,
                OrderType.ASCENDING);
    }

    /**
     * Updates the given {@link Contribution} in the repository. The old version of this contribution should
     * be already stored there.
     */
    public void update(Contribution contribution) {
        this.contributionDao.update(contribution);
    }

    /**
     * Updates properties of the given <code>contributionToUpdate</code> and persists this
     * {@link Contribution} into the repository. The old version of this contribution should be already stored
     * there.
     * 
     * @param contributionToUpdate
     *            Persisted {@link Contribution}
     * @param newContribution
     *            {@link Contribution} which contains new values of properties of
     *            <code>contributionToUpdate</code>. These values are copied to the persisted contribution.
     */
    public void update(Contribution contributionToUpdate, Contribution newContribution) {
        contributionToUpdate.setText(newContribution.getText());
        this.update(contributionToUpdate);
    }

    /**
     * Updates properties of the given <code>contributionToUpdate</code> and persists this
     * {@link Contribution} into the repository. The old version of this contribution should be already stored
     * there.
     * <p>
     * Finally, notifies all users which have corresponding rights by email about changes in the contribution.
     * 
     * @param contributionToUpdate
     *            Persisted {@link Contribution}
     * @param newContribution
     *            {@link Contribution} which contains new values of properties of
     *            <code>contributionToUpdate</code>. These values are copied to the persisted contribution.
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void updateAndNotifyUsers(Contribution contributionToUpdate, Contribution newContribution,
            boolean sendNotification, HttpServletRequest request, MessageSource messageSource) {
        this.update(contributionToUpdate, newContribution);

        if (sendNotification) {
            this.notifyUsersOfUpdate(contributionToUpdate, request, messageSource);
        }
    }

    @Override
    public List<User> gainUsersToNotify() {
        return this.userService.findAllWithEmailByAuthorityAndSubscription(AuthorityType.ROLE_MEMBER_OF_SV,
                this.emailService.getNotificationType());
    }

    @Override
    public void notifyUsersOfUpdate(Object updatedContribution, HttpServletRequest request,
            MessageSource messageSource) {
        this.emailService.sendEmailOnUpdate(updatedContribution, this.gainUsersToNotify(), request,
                messageSource);
    }

    /**
     * Stores the given {@link Contribution} to the repository.
     * 
     * @return New ID of the given {@link Contribution} generated by the repository
     */
    public int save(Contribution contribution) {
        return this.contributionDao.save(contribution);
    }

    /**
     * Updates corresponding {@link java.util.Date Date} fields of the given new {@link Contribution} (if
     * there are any appropriate) and stores the contribution to the repository.
     * <p>
     * Finally, notifies all users which have corresponding rights by email about a creation of the
     * contribution.
     * 
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void saveAndNotifyUsers(Contribution newContribution, boolean sendNotification,
            HttpServletRequest request, MessageSource messageSource) {
        this.save(newContribution);

        if (sendNotification) {
            this.notifyUsersOfCreation(newContribution, request, messageSource);
        }
    }

    @Override
    public void notifyUsersOfCreation(Object newContribution, HttpServletRequest request,
            MessageSource messageSource) {
        this.emailService.sendEmailOnCreation(newContribution, this.gainUsersToNotify(), request,
                messageSource);
    }

    /**
     * Deletes the given {@link Contribution} from the repository.
     */
    public void delete(Contribution contribution) {
        this.contributionDao.delete(contribution);
    }

    /**
     * Deletes the specified {@link Contribution} from the repository.
     * 
     * @param contributionId
     *            ID of the contribution
     */
    public void delete(int contributionId) {
        Contribution contribution = this.findById(contributionId);
        this.contributionDao.delete(contribution);
    }

    /**
     * Gets a {@link Map} which for each input {@link Contribution} contains a corresponding localized delete
     * question which is asked before deletion of that contribution.
     */
    public static Map<Contribution, String> getLocalizedDeleteQuestions(List<Contribution> contributions,
            HttpServletRequest request, MessageSource messageSource) {
        String messageCode = "forum.contributions.do-you-really-want-to-delete-contribution";
        String question = Localization.findLocaleMessage(messageSource, request, messageCode);
        Map<Contribution, String> questions = new HashMap<Contribution, String>();

        for (Contribution contribution : contributions) {
            questions.put(contribution, question);
        }
        return questions;
    }
}
