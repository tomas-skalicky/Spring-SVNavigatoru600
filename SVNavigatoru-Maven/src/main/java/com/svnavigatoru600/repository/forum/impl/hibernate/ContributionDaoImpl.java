package com.svnavigatoru600.repository.forum.impl.hibernate;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.impl.ContributionField;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.service.util.OrderType;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("contributionDao")
public class ContributionDaoImpl extends HibernateDaoSupport implements ContributionDao {

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public ContributionDaoImpl(SessionFactory sessionFactory) {
        super();
        setSessionFactory(sessionFactory);
    }

    @Override
    public Contribution findById(int contributionId) {
        return getHibernateTemplate().load(Contribution.class, contributionId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Contribution> findAll(int threadId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Contribution.class);
        criteria.add(Restrictions.eq(ContributionField.threadId.getFieldChain(), threadId));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Contribution> findAllOrdered(ContributionField sortField, OrderType sortDirection,
            int maxResultSize) {
        String query = String.format("FROM %s c ORDER BY c.%s %s", PersistedClass.Contribution.name(),
                sortField.name(), sortDirection.getDatabaseCode());
        List<Contribution> contributionsFromDb = getHibernateTemplate().find(query);
        return contributionsFromDb.subList(0, Math.min(contributionsFromDb.size(), maxResultSize));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Contribution> findAllOrdered(int threadId, ContributionField sortField,
            OrderType sortDirection) {
        String query = String.format("FROM %s c WHERE c.%s = ? ORDER BY c.%s %s",
                PersistedClass.Contribution.name(), ContributionField.threadId.getFieldChain(),
                sortField.name(), sortDirection.getDatabaseCode());
        return getHibernateTemplate().find(query, threadId);
    }

    @Override
    public void update(Contribution contribution) {
        Date now = new Date();
        contribution.setLastSaveTime(now);
        getHibernateTemplate().update(contribution);
    }

    @Override
    public int save(Contribution contribution) {
        Date now = new Date();
        contribution.setCreationTime(now);
        contribution.setLastSaveTime(now);
        return (Integer) getHibernateTemplate().save(contribution);
    }

    @Override
    public void delete(Contribution contribution) {
        getHibernateTemplate().delete(contribution);
    }
}
