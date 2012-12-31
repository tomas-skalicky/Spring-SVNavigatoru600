package com.svnavigatoru600.repository.forum.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.impl.ContributionField;
import com.svnavigatoru600.repository.forum.impl.FindAllContributionsOrderedWithLimitArguments;
import com.svnavigatoru600.repository.forum.impl.FindAllContributionsOrderedWithThreadIdArguments;
import com.svnavigatoru600.repository.impl.PersistedClass;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class ContributionDaoImpl extends HibernateDaoSupport implements ContributionDao {

    @Override
    public Contribution findById(int contributionId) {
        return this.getHibernateTemplate().load(Contribution.class, contributionId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Contribution> find(int threadId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Contribution.class);
        criteria.add(Restrictions.eq(ContributionField.threadId.getFieldChain(), threadId));

        return (List<Contribution>) this.getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Contribution> findAllOrdered(FindAllContributionsOrderedWithLimitArguments arguments) {
        String query = String.format("FROM %s c ORDER BY c.%s %s", PersistedClass.Contribution.name(),
                arguments.getSortField().name(), arguments.getSortDirection().getDatabaseCode());
        return (List<Contribution>) this.getHibernateTemplate().find(query);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Contribution> findAllOrdered(FindAllContributionsOrderedWithThreadIdArguments arguments) {
        String query = String.format("FROM %s c WHERE c.%s = ? ORDER BY c.%s %s", PersistedClass.Contribution
                .name(), ContributionField.threadId.getFieldChain(), arguments.getSortField().name(),
                arguments.getSortDirection().getDatabaseCode());
        return (List<Contribution>) this.getHibernateTemplate().find(query, arguments.getThreadId());
    }

    @Override
    public void update(Contribution contribution) {
        Date now = new Date();
        contribution.setLastSaveTime(now);
        this.getHibernateTemplate().update(contribution);
    }

    @Override
    public int save(Contribution contribution) {
        Date now = new Date();
        contribution.setCreationTime(now);
        contribution.setLastSaveTime(now);
        return (Integer) this.getHibernateTemplate().save(contribution);
    }

    @Override
    public void delete(Contribution contribution) {
        this.getHibernateTemplate().delete(contribution);
    }
}
