package com.svnavigatoru600.service.news;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.NewsDao;
import com.svnavigatoru600.repository.news.impl.FindAllOrderedArguments;
import com.svnavigatoru600.repository.news.impl.NewsField;
import com.svnavigatoru600.service.SubjectOfNotificationService;
import com.svnavigatoru600.service.users.UserService;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.OrderType;

/**
 * Provides convenient methods to work with {@link News} objects.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class NewsService implements SubjectOfNotificationService {

    private static final String DELETE_NEWS_QUESTION_CODE = "news.do-you-really-want-to-delete-news";
    /**
     * The object which provides a persistence.
     */
    private final NewsDao newsDao;
    /**
     * Does the work which concerns mainly notification of {@link User users}.
     */
    private UserService userService;
    /**
     * Assembles notification emails and sends them to authorized {@link User users}.
     */
    private NewsNotificationEmailService emailService;

    /**
     * Constructor.
     */
    @Inject
    public NewsService(NewsDao newsDao) {
        this.newsDao = newsDao;
    }

    @Inject
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Inject
    public void setEmailService(NewsNotificationEmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Returns a {@link News} stored in the repository which has the given ID.
     */
    public News findById(int newsId) {
        return this.newsDao.findById(newsId);
    }

    /**
     * Returns all {@link News} stored in the repository arranged according to their
     * {@link News#getCreationTime() creation time} descending.
     */
    public List<News> findAllOrdered() {
        return this.newsDao.findAllOrdered(new FindAllOrderedArguments(NewsField.creationTime,
                OrderType.DESCENDING));
    }

    /**
     * Updates the given {@link News} in the repository. The old version of this news should be already stored
     * there.
     */
    public void update(News news) {
        this.newsDao.update(news);
    }

    /**
     * Updates properties of the given <code>newsToUpdate</code> and persists this {@link News} into the
     * repository. The old version of this news should be already stored there.
     * 
     * @param newsToUpdate
     *            Persisted {@link News}
     * @param newNews
     *            {@link News} which contains new values of properties of <code>newsToUpdate</code>. These
     *            values are copied to the persisted news.
     */
    public void update(News newsToUpdate, News newNews) {
        newsToUpdate.setTitle(newNews.getTitle());
        newsToUpdate.setText(newNews.getText());
        this.update(newsToUpdate);
    }

    /**
     * Updates properties of the given <code>newsToUpdate</code> and persists this {@link News} into the
     * repository. The old version of this news should be already stored there.
     * <p>
     * Finally, notifies all users which have corresponding rights by email about changes in the news.
     * 
     * @param newsToUpdate
     *            Persisted {@link News}
     * @param newNews
     *            {@link News} which contains new values of properties of <code>newsToUpdate</code>. These
     *            values are copied to the persisted news.
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void updateAndNotifyUsers(News newsToUpdate, News newNews, boolean sendNotification,
            HttpServletRequest request, MessageSource messageSource) {
        this.update(newsToUpdate, newNews);

        if (sendNotification) {
            this.notifyUsersOfUpdate(newsToUpdate, request, messageSource);
        }
    }

    @Override
    public List<User> gainUsersToNotify() {
        return this.userService.findAllWithEmailByAuthorityAndSubscription(AuthorityType.ROLE_MEMBER_OF_SV,
                this.emailService.getNotificationType());
    }

    @Override
    public void notifyUsersOfUpdate(Object updatedNews, HttpServletRequest request,
            MessageSource messageSource) {
        this.emailService.sendEmailOnUpdate(updatedNews, this.gainUsersToNotify(), request, messageSource);
    }

    /**
     * Updates corresponding {@link java.util.Date Date} fields of the given {@link News} and stores the news
     * to the repository.
     * 
     * @return The new ID of the given {@link News} generated by the repository
     */
    public int save(News news) {
        return this.newsDao.save(news);
    }

    /**
     * Updates corresponding {@link java.util.Date Date} fields of the given new {@link News} (if there are
     * any appropriate) and stores the news to the repository.
     * <p>
     * Finally, notifies all users which have corresponding rights by email about a creation of the news.
     * 
     * @param sendNotification
     *            If <code>true</code>, the notification is sent; otherwise not.
     */
    public void saveAndNotifyUsers(News newNews, boolean sendNotification, HttpServletRequest request,
            MessageSource messageSource) {
        this.save(newNews);

        if (sendNotification) {
            this.notifyUsersOfCreation(newNews, request, messageSource);
        }
    }

    @Override
    public void notifyUsersOfCreation(Object newNews, HttpServletRequest request, MessageSource messageSource) {
        this.emailService.sendEmailOnCreation(newNews, this.gainUsersToNotify(), request, messageSource);
    }

    /**
     * Deletes the given {@link News} from the repository.
     */
    public void delete(News news) {
        this.newsDao.delete(news);
    }

    /**
     * Deletes the specified {@link News} from the repository.
     * 
     * @param newsId
     *            The ID of the news
     */
    public void delete(int newsId) {
        News news = this.findById(newsId);
        this.newsDao.delete(news);
    }

    /**
     * Gets a {@link Map} which for each input {@link News} contains a corresponding localized delete question
     * which is asked before deletion of that news.
     */
    public static Map<News, String> getLocalizedDeleteQuestions(List<News> newss, HttpServletRequest request,
            MessageSource messageSource) {
        Map<News, String> questions = new HashMap<News, String>();
        for (News news : newss) {
            questions.put(news, getLocalizedDeleteQuestion(news, messageSource, request));
        }
        return questions;
    }

    /**
     * For the given {@link News}, gets a corresponding localized delete question which is asked before
     * deletion of that news.
     */
    public static String getLocalizedDeleteQuestion(News news, MessageSource messageSource,
            HttpServletRequest request) {
        Object[] messageParams = new Object[] { news.getTitle() };
        return Localization.findLocaleMessage(messageSource, request, DELETE_NEWS_QUESTION_CODE,
                messageParams);
    }
}
