package com.svnavigatoru600.repository.forum.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.repository.forum.ThreadDao;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class ThreadDaoImpl extends HibernateDaoSupport implements ThreadDao {

    @Override
    public Thread findById(int threadId) {
        return getHibernateTemplate().load(Thread.class, threadId);
    }

    @Override
    public List<Thread> loadAll() {
        return (List<Thread>) getHibernateTemplate().loadAll(Thread.class);
    }

    @Override
    public void update(Thread thread) {
        getHibernateTemplate().update(thread);
    }

    @Override
    public int save(Thread thread) {
        Date now = new Date();
        thread.setCreationTime(now);

        for (Contribution contribution : thread.getContributions()) {
            contribution.setCreationTime(now);
            contribution.setLastSaveTime(now);
        }

        return (Integer) getHibernateTemplate().save(thread);
    }

    @Override
    public void delete(Thread thread) {
        getHibernateTemplate().delete(thread);
    }
}
