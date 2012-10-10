package svnavigatoru.repository.forum.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import svnavigatoru.domain.forum.Contribution;
import svnavigatoru.repository.forum.ContributionDao;
import svnavigatoru.service.util.OrderType;

public class ContributionDaoImpl extends HibernateDaoSupport implements ContributionDao {

	public Contribution findById(int contributionId) {
		return this.getHibernateTemplate().load(Contribution.class, contributionId);
	}

	@SuppressWarnings("unchecked")
	public List<Contribution> find(int threadId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Contribution.class);
		criteria.add(Restrictions.eq("thread.id", threadId));

		return (List<Contribution>) this.getHibernateTemplate().findByCriteria(criteria);
	}

	/**
	 * @param count
	 *            Not used yet.
	 */
	@SuppressWarnings("unchecked")
	public List<Contribution> findOrdered(String attribute, OrderType order, int count) {
		String query = String.format("FROM Contribution c ORDER BY c.%s %s", attribute, order.getDatabaseCode());
		return (List<Contribution>) this.getHibernateTemplate().find(query);
	}

	@SuppressWarnings("unchecked")
	public List<Contribution> findOrdered(int threadId, String attribute, OrderType order) {
		String query = String.format("FROM Contribution c WHERE c.thread.id = ? ORDER BY c.%s %s", attribute,
				order.getDatabaseCode());
		return (List<Contribution>) this.getHibernateTemplate().find(query, threadId);
	}

	public void update(Contribution contribution) {
		Date now = new Date();
		contribution.setLastSaveTime(now);
		this.getHibernateTemplate().update(contribution);
	}

	public int save(Contribution contribution) {
		Date now = new Date();
		contribution.setCreationTime(now);
		contribution.setLastSaveTime(now);
		return (Integer) this.getHibernateTemplate().save(contribution);
	}

	public void delete(Contribution contribution) {
		this.getHibernateTemplate().delete(contribution);
	}
}
