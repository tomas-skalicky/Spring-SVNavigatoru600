package com.svnavigatoru600.service.forum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.ForumThread;
import com.svnavigatoru600.domain.users.AuthorityTypeEnum;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.service.SubjectOfNotificationService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.service.util.Localization;

/**
 * Provides convenient methods to work with {@link com.svnavigatoru600.domain.forum.ForumThread Thread} objects.
 * <p>
 * For instance: There are methods which determine whether the currently logged-in user has rights to manipulate with a
 * particular {@link com.svnavigatoru600.domain.forum.ForumThread Thread}.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class ThreadService implements SubjectOfNotificationService {

    /**
     * The object which provides a persistence.
     */
    private final ThreadDao threadDao;
    /**
     * Does the work which concerns mainly notification of {@link User users}.
     */
    private UserService userService;
    /**
     * Assembles notification emails and sends them to authorized {@link User users}.
     */
    private ThreadNotificationEmailService emailService;

    @Inject
    public ThreadService(final ThreadDao threadDao) {
        this.threadDao = threadDao;
    }

    @Inject
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }

    @Inject
    public void setEmailService(final ThreadNotificationEmailService emailService) {
        this.emailService = emailService;
    }

    @PreAuthorize("hasPermission(#threadId, '" + ForumThread.CLASS_FULL_NAME + "', 'edit')")
    public void canEdit(final int threadId) {
    }

    @PreAuthorize("hasPermission(#threadId, '" + ForumThread.CLASS_FULL_NAME + "', 'delete')")
    public void canDelete(final int threadId) {
    }

    /**
     * Returns a {@link ForumThread} stored in the repository which has the given ID.
     */
    public ForumThread findById(final int threadId) {
        return threadDao.findById(threadId);
    }

    /**
     * Returns all {@link ForumThread Threads} stored in the repository.
     */
    public List<ForumThread> loadAll() {
        return threadDao.loadAll();
    }

    /**
     * Updates the given {@link ForumThread} in the repository. The old version of this thread should be already stored
     * there.
     */
    public void update(final ForumThread thread) {
        threadDao.update(thread);
    }

    /**
     * Updates properties of the given <code>threadToUpdate</code> and persists this {@link ForumThread} into the
     * repository. The old version of this thread should be already stored there.
     *
     * @param threadToUpdate
     *            Persisted {@link ForumThread}
     * @param newThread
     *            {@link ForumThread} which contains new values of properties of <code>threadToUpdate</code>. These
     *            values are copied to the persisted thread.
     */
    public void update(final ForumThread threadToUpdate, final ForumThread newThread) {
        threadToUpdate.setName(newThread.getName());
        this.update(threadToUpdate);
    }

    @Override
    public List<User> gainUsersToNotify() {
        return userService.findAllWithEmailByAuthorityAndSubscription(AuthorityTypeEnum.ROLE_MEMBER_OF_SV,
                emailService.getNotificationType());
    }

    @Override
    public void notifyUsersOfUpdate(final Object updatedObject, final HttpServletRequest request,
            final MessageSource messageSource) {
        throw new IllegalAccessError("This method is not supported");
    }

    /**
     * Stores the given {@link ForumThread} to the repository.
     *
     * @return New ID of the given {@link ForumThread} generated by the repository
     */
    public int save(final ForumThread thread) {
        return threadDao.save(thread);
    }

    /**
     * Updates corresponding {@link java.util.Date Date} fields of the given new {@link ForumThread} (if there are any
     * appropriate) and stores the thread to the repository.
     * <p>
     * Finally, notifies all users which have corresponding rights by email about a creation of the thread.
     *
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void saveAndNotifyUsers(final ForumThread newThread, final boolean sendNotification,
            final HttpServletRequest request, final MessageSource messageSource) {
        save(newThread);

        if (sendNotification) {
            notifyUsersOfCreation(newThread, request, messageSource);
        }
    }

    @Override
    public void notifyUsersOfCreation(final Object newThread, final HttpServletRequest request,
            final MessageSource messageSource) {
        emailService.sendEmailOnCreation(newThread, gainUsersToNotify(), request, messageSource);
    }

    /**
     * Deletes the given {@link ForumThread} from the repository.
     */
    public void delete(final ForumThread thread) {
        threadDao.delete(thread);
    }

    /**
     * Gets a {@link Map} which for each input {@link ForumThread} contains a corresponding localized delete question
     * which is asked before deletion of that thread.
     */
    public static Map<ForumThread, String> getLocalizedDeleteQuestions(final List<ForumThread> threads,
            final HttpServletRequest request, final MessageSource messageSource) {
        final String messageCode = "forum.threads.do-you-really-want-to-delete-thread";
        final Map<ForumThread, String> questions = new HashMap<>();

        for (final ForumThread thread : threads) {
            final Object[] messageParams = new Object[] { thread.getName() };
            questions.put(thread, Localization.findLocaleMessage(messageSource, request, messageCode, messageParams));
        }
        return questions;
    }

}
