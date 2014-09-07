package com.svnavigatoru600.repository.users.impl.hibernate;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.repository.users.AuthorityDao;
import com.svnavigatoru600.repository.users.impl.AuthorityField;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("authorityDao")
public class AuthorityDaoImpl extends HibernateDaoSupport implements AuthorityDao {

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public AuthorityDaoImpl(SessionFactory sessionFactory) {
        super();
        setSessionFactory(sessionFactory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Authority> findAll(String username) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Authority.class);
        criteria.add(Restrictions.eq(AuthorityField.username.getFieldChain(), username));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void save(Authority authority) {
        getHibernateTemplate().save(authority);
    }

    @Override
    public void save(Collection<GrantedAuthority> authorities) {
        for (GrantedAuthority authority : authorities) {
            this.save((Authority) authority);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(String username) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Authority.class);
        criteria.add(Restrictions.eq(AuthorityField.username.getFieldChain(), username));

        getHibernateTemplate().deleteAll(getHibernateTemplate().findByCriteria(criteria));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(String username, AuthorityType authorityType) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Authority.class);
        criteria.add(Restrictions.eq(AuthorityField.username.getFieldChain(), username));
        criteria.add(Restrictions.eq(AuthorityField.authority.getFieldChain(), authorityType.name()));

        getHibernateTemplate().deleteAll(getHibernateTemplate().findByCriteria(criteria));
    }
}
