package com.svnavigatoru600.repository.forum.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.repository.forum.ThreadDao;

public class ThreadDaoImpl extends HibernateDaoSupport implements ThreadDao {

    public Thread findById(int threadId) {
        return this.getHibernateTemplate().load(Thread.class, threadId);
    }

    public List<Thread> loadAll() {
        return (List<Thread>) this.getHibernateTemplate().loadAll(Thread.class);
    }

    public void update(Thread thread) {
        this.getHibernateTemplate().update(thread);
    }

    public int save(Thread thread) {
        Date now = new Date();
        thread.setCreationTime(now);

        for (Contribution contribution : thread.getContributions()) {
            contribution.setCreationTime(now);
            contribution.setLastSaveTime(now);
        }

        return (Integer) this.getHibernateTemplate().save(thread);
    }

    public void delete(Thread thread) {
        this.getHibernateTemplate().delete(thread);
    }
}
