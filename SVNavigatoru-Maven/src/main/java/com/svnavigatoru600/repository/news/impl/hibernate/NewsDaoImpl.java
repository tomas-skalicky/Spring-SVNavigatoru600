package com.svnavigatoru600.repository.news.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.repository.NewsDao;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.news.impl.FindAllOrderedArguments;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class NewsDaoImpl extends HibernateDaoSupport implements NewsDao {

    @Override
    public News findById(int newsId) {
        return this.getHibernateTemplate().load(News.class, newsId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<News> findAllOrdered(FindAllOrderedArguments arguments) {
        String query = String.format("FROM %s n ORDER BY %s %s", PersistedClass.News.name(), arguments
                .getSortField().name(), arguments.getSortDirection().getDatabaseCode());
        return (List<News>) this.getHibernateTemplate().find(query);
    }

    @Override
    public void update(News news) {
        Date now = new Date();
        news.setLastSaveTime(now);

        this.getHibernateTemplate().update(news);
    }

    @Override
    public int save(News news) {
        Date now = new Date();
        news.setCreationTime(now);
        news.setLastSaveTime(now);

        int newId = (Integer) this.getHibernateTemplate().save(news);
        news.setId(newId);
        return newId;
    }

    @Override
    public void delete(News news) {
        this.getHibernateTemplate().delete(news);
    }
}
