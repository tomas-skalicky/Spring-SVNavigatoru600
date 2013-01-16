package com.svnavigatoru600.service.forum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.service.SubjectOfNotificationService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.service.util.Localization;

/**
 * Provides convenient methods to work with {@link com.svnavigatoru600.domain.forum.Thread Thread} objects.
 * <p>
 * For instance: There are methods which determine whether the currently logged-in user has rights to
 * manipulate with a particular {@link com.svnavigatoru600.domain.forum.Thread Thread}.
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

    /**
     * Constructor.
     */
    @Inject
    public ThreadService(ThreadDao threadDao) {
        this.threadDao = threadDao;
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Inject
    public void setEmailService(ThreadNotificationEmailService emailService) {
        this.emailService = emailService;
    }

    @PreAuthorize("hasPermission(#threadId, 'com.svnavigatoru600.domain.forum.Thread', 'edit')")
    public void canEdit(int threadId) {
    }

    @PreAuthorize("hasPermission(#threadId, 'com.svnavigatoru600.domain.forum.Thread', 'delete')")
    public void canDelete(int threadId) {
    }

    /**
     * Returns a {@link Thread} stored in the repository which has the given ID.
     */
    public Thread findById(int threadId) {
        return this.threadDao.findById(threadId);
    }

    /**
     * Returns all {@link Thread Threads} stored in the repository.
     */
    public List<Thread> loadAll() {
        return this.threadDao.loadAll();
    }

    /**
     * Updates the given {@link Thread} in the repository. The old version of this thread should be already
     * stored there.
     */
    public void update(Thread thread) {
        this.threadDao.update(thread);
    }

    /**
     * Updates properties of the given <code>threadToUpdate</code> and persists this {@link Thread} into the
     * repository. The old version of this thread should be already stored there.
     * 
     * @param threadToUpdate
     *            Persisted {@link Thread}
     * @param newThread
     *            {@link Thread} which contains new values of properties of <code>threadToUpdate</code>. These
     *            values are copied to the persisted thread.
     */
    public void update(Thread threadToUpdate, Thread newThread) {
        threadToUpdate.setName(newThread.getName());
        this.update(threadToUpdate);
    }

    @Override
    public List<User> gainUsersToNotify() {
        return this.userService.findAllWithEmailByAuthorityAndSubscription(AuthorityType.ROLE_MEMBER_OF_SV,
                this.emailService.getNotificationType());
    }

    @Override
    public void notifyUsersOfUpdate(Object updatedObject, HttpServletRequest request,
            MessageSource messageSource) {
        throw new IllegalAccessError("This method is not supported");
    }

    /**
     * Stores the given {@link Thread} to the repository.
     * 
     * @return New ID of the given {@link Thread} generated by the repository
     */
    public int save(Thread thread) {
        return this.threadDao.save(thread);
    }

    /**
     * Updates corresponding {@link java.util.Date Date} fields of the given new {@link Thread} (if there are
     * any appropriate) and stores the thread to the repository.
     * <p>
     * Finally, notifies all users which have corresponding rights by email about a creation of the thread.
     * 
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void saveAndNotifyUsers(Thread newThread, boolean sendNotification, HttpServletRequest request,
            MessageSource messageSource) {
        this.save(newThread);

        if (sendNotification) {
            this.notifyUsersOfCreation(newThread, request, messageSource);
        }
    }

    @Override
    public void notifyUsersOfCreation(Object newThread, HttpServletRequest request,
            MessageSource messageSource) {
        this.emailService.sendEmailOnCreation(newThread, this.gainUsersToNotify(), request, messageSource);
    }

    /**
     * Deletes the given {@link Thread} from the repository.
     */
    public void delete(Thread thread) {
        this.threadDao.delete(thread);
    }

    /**
     * Gets a {@link Map} which for each input {@link Thread} contains a corresponding localized delete
     * question which is asked before deletion of that thread.
     */
    public static Map<Thread, String> getLocalizedDeleteQuestions(List<Thread> threads,
            HttpServletRequest request, MessageSource messageSource) {
        String messageCode = "forum.threads.do-you-really-want-to-delete-thread";
        Map<Thread, String> questions = new HashMap<Thread, String>();

        for (Thread thread : threads) {
            Object[] messageParams = new Object[] { thread.getName() };
            questions.put(thread,
                    Localization.findLocaleMessage(messageSource, request, messageCode, messageParams));
        }
        return questions;
    }
}
