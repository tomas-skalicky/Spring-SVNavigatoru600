package com.svnavigatoru600.repository;

import java.util.Date;
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
public class NewsDaoTest extends AbstractRepositoryTest {

    /**
     * Title of the edited test news.
     */
    private static final String EDITED_NEWS_TITLE = "news title 2";
    /**
     * Text of the edited test news.
     */
    private static final String EDITED_NEWS_TEXT = "news text 2";

    @Test
    public void testSaveFindById() throws Exception {
        NewsDao newsDao = TEST_UTILS.getNewsDao();

        // INSERT
        int newsId = TEST_UTILS.createDefaultTestNews();

        // SELECT ONE
        News news = newsDao.findById(newsId);
        Assert.assertTrue(news.getId() >= 1);
        Assert.assertEquals(newsId, news.getId());
        Assert.assertEquals(RepositoryTestUtils.NEWS_DEFAULT_TITLE, news.getTitle());
        Assert.assertEquals(RepositoryTestUtils.NEWS_DEFAULT_TEXT, news.getText());
        Assert.assertTrue(new Date().after(news.getCreationTime()));
        Assert.assertTrue(new Date().after(news.getLastSaveTime()));
        Assert.assertEquals(news.getCreationTime(), news.getLastSaveTime());
    }

    @Test
    public void testFindAllOrdered() throws Exception {
        NewsDao newsDao = TEST_UTILS.getNewsDao();

        // TWO INSERTS
        int firstNewsId = TEST_UTILS.createDefaultTestNews();
        int secondNewsId = TEST_UTILS.createDefaultTestNews();

        // SELECT ALL
        List<News> foundNews = newsDao.findAllOrdered(new FindAllOrderedArguments(NewsField.creationTime,
                OrderType.ASCENDING));
        int expectedFoundNewsCount = 2;
        Assert.assertEquals(expectedFoundNewsCount, foundNews.size());
        Assert.assertEquals(firstNewsId, foundNews.get(0).getId());
        Assert.assertEquals(secondNewsId, foundNews.get(1).getId());
    }

    @Test
    public void testUpdate() throws Exception {
        NewsDao newsDao = TEST_UTILS.getNewsDao();

        // INSERT & SELECT ONE
        int newsId = TEST_UTILS.createDefaultTestNews();
        News news = newsDao.findById(newsId);

        // UPDATE
        news.setTitle(EDITED_NEWS_TITLE);
        news.setText(EDITED_NEWS_TEXT);
        newsDao.update(news);

        // SELECT ONE
        news = newsDao.findById(news.getId());
        Assert.assertEquals(newsId, news.getId());
        Assert.assertEquals(EDITED_NEWS_TITLE, news.getTitle());
        Assert.assertEquals(EDITED_NEWS_TEXT, news.getText());
        Assert.assertTrue(news.getLastSaveTime().after(news.getCreationTime()));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void testDelete() throws Exception {
        NewsDao newsDao = TEST_UTILS.getNewsDao();

        // INSERT & SELECT ONE
        int newsId = TEST_UTILS.createDefaultTestNews();
        News news = newsDao.findById(newsId);

        // DELETE
        newsDao.delete(news);

        // SELECT ONE
        // Throws an exception since the news cannot be found.
        newsDao.findById(news.getId());
    }
}
