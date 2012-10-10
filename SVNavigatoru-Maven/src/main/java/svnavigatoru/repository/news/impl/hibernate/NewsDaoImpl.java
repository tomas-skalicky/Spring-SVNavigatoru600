package svnavigatoru.repository.news.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import svnavigatoru.domain.News;
import svnavigatoru.repository.NewsDao;
import svnavigatoru.service.util.OrderType;

public class NewsDaoImpl extends HibernateDaoSupport implements NewsDao {

	public News findById(int newsId) {
		return this.getHibernateTemplate().load(News.class, newsId);
	}

	@SuppressWarnings("unchecked")
	public List<News> findOrdered(String attribute, OrderType order) {
		String query = String.format("FROM News n ORDER BY %s %s", attribute, order.getDatabaseCode());
		return (List<News>) this.getHibernateTemplate().find(query);
	}

	public void update(News news) {
		Date now = new Date();
		news.setLastSaveTime(now);
		this.getHibernateTemplate().update(news);
	}

	public int save(News news) {
		Date now = new Date();
		news.setCreationTime(now);
		news.setLastSaveTime(now);
		return (Integer) this.getHibernateTemplate().save(news);
	}

	public void delete(News news) {
		this.getHibernateTemplate().delete(news);
	}
}
