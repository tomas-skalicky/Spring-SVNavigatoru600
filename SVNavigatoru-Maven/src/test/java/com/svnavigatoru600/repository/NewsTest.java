package com.svnavigatoru600.repository;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.dao.EmptyResultDataAccessException;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.repository.news.impl.FindAllOrderedArguments;
import com.svnavigatoru600.repository.news.impl.NewsField;
import com.svnavigatoru600.service.util.OrderType;
import com.svnavigatoru600.test.category.PersistenceTests;

/**
 * Tests methods of the {@link NewsDao} interface.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(PersistenceTests.class)
public class NewsTest extends AbstractRepositoryTest {

    /**
     * Default title of the test news.
     */
    private static final String NEWS_TITLE = "title 1";
    /**
     * Default text of the test news.
     */
    private static final String NEWS_TEXT = "text 1";
    /**
     * Text of the edited test news.
     */
    private static final String EDITED_NEWS_TEXT = "text 2";

    @Test
    public void testCreateRetrieve() throws Exception {
        NewsDao newsDao = APPLICATION_CONTEXT.getBean(NewsDao.class);

        // INSERT
        int newsId = this.createDefaultTestNews(newsDao);

        // SELECT ONE
        News news = newsDao.findById(newsId);
        Assert.assertNotNull(news);
        Assert.assertTrue(news.getId() >= 1);
        Assert.assertEquals(newsId, news.getId());
        Assert.assertEquals(NEWS_TITLE, news.getTitle());
        Assert.assertEquals(NEWS_TEXT, news.getText());
    }

    @Test
    public void testUpdate() throws Exception {
        NewsDao newsDao = APPLICATION_CONTEXT.getBean(NewsDao.class);

        // INSERT & SELECT ONE
        int newsId = this.createDefaultTestNews(newsDao);
        News news = newsDao.findById(newsId);

        // UPDATE
        news.setText(EDITED_NEWS_TEXT);
        newsDao.update(news);

        // SELECT ONE
        news = newsDao.findById(news.getId());
        Assert.assertNotNull(news);
        Assert.assertTrue(news.getId() >= 1);
        Assert.assertEquals(newsId, news.getId());
        Assert.assertEquals(EDITED_NEWS_TEXT, news.getText());
        Assert.assertTrue(news.getLastSaveTime().after(news.getCreationTime()));
    }

    @Test
    public void testDelete() throws Exception {
        NewsDao newsDao = APPLICATION_CONTEXT.getBean(NewsDao.class);

        // INSERT & SELECT ONE
        int newsId = this.createDefaultTestNews(newsDao);
        News news = newsDao.findById(newsId);

        // DELETE
        newsDao.delete(news);

        // SELECT ONE
        try {
            news = newsDao.findById(news.getId());
            Assert.fail("The news has been found");
        } catch (EmptyResultDataAccessException ex) {
            // OK since the news cannot have been found.
            ;
        }
    }

    @Test
    public void testSelectAll() throws Exception {
        NewsDao newsDao = APPLICATION_CONTEXT.getBean(NewsDao.class);

        // TWO INSERTS
        int firstNewsId = this.createDefaultTestNews(newsDao);
        int secondNewsId = this.createDefaultTestNews(newsDao);

        // SELECT ALL
        List<News> foundNews = newsDao.findAllOrdered(new FindAllOrderedArguments(NewsField.creationTime,
                OrderType.ASCENDING));
        Assert.assertEquals(firstNewsId, foundNews.get(foundNews.size() - 2).getId());
        Assert.assertEquals(secondNewsId, foundNews.get(foundNews.size() - 1).getId());
    }

    /**
     * Creates and saves a default test news.
     * 
     * @return ID of the newly created news
     */
    private int createDefaultTestNews(NewsDao newsDao) {
        return this.createTestNews(NEWS_TITLE, NEWS_TEXT, newsDao);
    }

    /**
     * Creates and saves a test news.
     * 
     * @param title
     *            Title of the news
     * @param text
     *            Text of the news
     * @return ID of the newly created news
     */
    private int createTestNews(String title, String text, NewsDao newsDao) {
        News news = new News();
        news.setTitle(title);
        news.setText(text);

        int newId = newsDao.save(news);
        return newId;
    }
}
