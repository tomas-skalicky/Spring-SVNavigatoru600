package com.svnavigatoru600.repository.news.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.repository.NewsDao;
import com.svnavigatoru600.service.util.OrderType;

public class NewsDaoImpl extends HibernateDaoSupport implements NewsDao {

    @Override
    public News findById(int newsId) {
        return this.getHibernateTemplate().load(News.class, newsId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<News> findOrdered(String attribute, OrderType order) {
        String query = String.format("FROM News n ORDER BY %s %s", attribute, order.getDatabaseCode());
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
        return (Integer) this.getHibernateTemplate().save(news);
    }

    @Override
    public void delete(News news) {
        this.getHibernateTemplate().delete(news);
    }
}
