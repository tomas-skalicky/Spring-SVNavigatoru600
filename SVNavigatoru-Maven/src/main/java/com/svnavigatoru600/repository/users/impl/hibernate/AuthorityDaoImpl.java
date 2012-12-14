package com.svnavigatoru600.repository.users.impl.hibernate;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.security.core.GrantedAuthority;

import com.svnavigatoru600.domain.users.Authority;
import com.svnavigatoru600.repository.users.AuthorityDao;

public class AuthorityDaoImpl extends HibernateDaoSupport implements AuthorityDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<Authority> find(String username) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Authority.class);
        criteria.add(Restrictions.eq("id.username", username));

        return (List<Authority>) this.getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void save(Collection<GrantedAuthority> authorities) {
        for (GrantedAuthority authority : authorities) {
            this.getHibernateTemplate().save(authority);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(String username) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Authority.class);
        criteria.add(Restrictions.eq("id.username", username));

        this.getHibernateTemplate().deleteAll(
                (List<Authority>) this.getHibernateTemplate().findByCriteria(criteria));
    }
}
