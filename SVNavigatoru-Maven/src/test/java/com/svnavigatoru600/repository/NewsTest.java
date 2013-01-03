package com.svnavigatoru600.repository;

import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
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
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(PersistenceTests.class)
public class NewsTest extends AbstractMapperTest {

    /**
     * The default title of the test news.
     */
    private static final String NEWS_TITLE = "Test title";
    /**
     * The default text of the test news.
     */
    private static final String NEWS_TEXT = "Test text";
    /**
     * The text of the edited test news.
     */
    private static final String EDITED_NEWS_TEXT = "Test text 2";

    /**
     * If the given {@link SqlSession} is determined, the changes - carried out in the database - are
     * committed.
     */
    private void commitChanges(SqlSession sqlSession) {
        if (sqlSession != null) {
            sqlSession.commit();
        }
    }

    /**
     * Creates and saves a test instance of {@link News}.
     * 
     * @param newsDao
     * @param sqlSession
     * @param title
     *            The title of the {@link News}
     * @param text
     *            The text of the {@link News}
     * @return The ID of the created instance of {@link News}
     */
    private int createTestNews(String title, String text, NewsDao newsDao, SqlSession sqlSession) {
        News news = new News();
        news.setTitle(title);
        news.setText(text);

        int newId = newsDao.save(news);
        this.commitChanges(sqlSession);
        return newId;
    }

    /**
     * Creates and saves a default test instance of {@link News}.
     * 
     * @return The ID of the created instance of {@link News}
     */
    private int createDefaultTestNews(NewsDao newsDao, SqlSession sqlSession) {
        return this.createTestNews(NEWS_TITLE, NEWS_TEXT, newsDao, sqlSession);
    }

    /**
     * Deletes the given {@link News news}.
     */
    private void deleteNews(News news, NewsDao newsDao, SqlSession sqlSession) {
        newsDao.delete(news);
        this.commitChanges(sqlSession);
    }

    /**
     * Tests methods of the {@link NewsDao} interface.
     */
    @Test
    public void testWholeNewsDaoInterface() throws Exception {
        // SqlSession sqlSession = APPLICATION_CONTEXT.getBean(SqlSessionFactory.class).openSession();
        SqlSession sqlSession = null;
        Connection sqlConnection = APPLICATION_CONTEXT.getBean(DataSource.class).getConnection();
        sqlConnection.setAutoCommit(true);

        NewsDao newsDao = APPLICATION_CONTEXT.getBean(NewsDao.class);

        try {
            // INSERT
            int newsId = this.createDefaultTestNews(newsDao, sqlSession);

            // SELECT ONE
            News news = newsDao.findById(newsId);
            Assert.assertNotNull(news);
            Assert.assertTrue(news.getId() >= 1);
            Assert.assertEquals(newsId, news.getId());
            Assert.assertEquals(NEWS_TITLE, news.getTitle());
            Assert.assertEquals(NEWS_TEXT, news.getText());

            // UPDATE
            news.setText(EDITED_NEWS_TEXT);
            newsDao.update(news);
            this.commitChanges(sqlSession);

            // SELECT ONE
            news = newsDao.findById(news.getId());
            Assert.assertNotNull(news);
            Assert.assertTrue(news.getId() >= 1);
            Assert.assertEquals(newsId, news.getId());
            Assert.assertEquals(EDITED_NEWS_TEXT, news.getText());
            Assert.assertTrue(news.getLastSaveTime().after(news.getCreationTime()));

            // SELECT ALL
            int secondNewsId = this.createTestNews(NEWS_TITLE, NEWS_TEXT, newsDao, sqlSession);
            List<News> foundNews = newsDao.findAllOrdered(new FindAllOrderedArguments(NewsField.creationTime,
                    OrderType.ASCENDING));
            news = foundNews.get(foundNews.size() - 2);
            Assert.assertEquals(newsId, news.getId());
            News secondNews = foundNews.get(foundNews.size() - 1);
            Assert.assertEquals(secondNewsId, secondNews.getId());

            // DELETE
            this.deleteNews(news, newsDao, sqlSession);

            // SELECT ONE
            try {
                news = newsDao.findById(news.getId());
                Assert.fail("The news has been found");
            } catch (EmptyResultDataAccessException ex) {
                // OK since the news cannot have been found.
                ;
            }

            // DELETE
            this.deleteNews(secondNews, newsDao, sqlSession);

        } finally {
            // sqlSession.close();
            sqlConnection.close();
        }
    }
}
