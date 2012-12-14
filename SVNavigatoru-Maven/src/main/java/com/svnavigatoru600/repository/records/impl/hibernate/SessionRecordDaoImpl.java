package com.svnavigatoru600.repository.records.impl.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.svnavigatoru600.domain.records.DocumentRecord;
import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.service.util.OrderType;

public class SessionRecordDaoImpl extends HibernateDaoSupport implements SessionRecordDao {

    public SessionRecord findById(int recordId) {
        return this.getHibernateTemplate().load(SessionRecord.class, recordId);
    }

    public SessionRecord findById(int recordId, boolean loadFile) {
        // TBD
        return null;
    }

    public SessionRecord findByFileName(String fileName) {
        // Stands for 'where' clause.
        DetachedCriteria criteria = DetachedCriteria.forClass(SessionRecord.class);
        criteria.add(Restrictions.eq("fileName", fileName));

        @SuppressWarnings("unchecked")
        List<SessionRecord> records = (List<SessionRecord>) this.getHibernateTemplate().findByCriteria(
                criteria);
        if (records.size() > 1) {
            throw new DataIntegrityViolationException("Filename should be unique.");
        } else if (records.size() == 0) {
            throw new DataRetrievalFailureException("No record associated with the given filename exists.");
        }
        return records.get(0);
    }

    @SuppressWarnings("unchecked")
    public List<SessionRecord> findOrdered(OrderType order) {
        String query = String.format("FROM SessionRecord r ORDER BY r.sessionDate %s",
                order.getDatabaseCode());
        return (List<SessionRecord>) this.getHibernateTemplate().find(query);
    }

    @SuppressWarnings("unchecked")
    public List<SessionRecord> findOrdered(SessionRecordType type, OrderType order) {
        String query = String.format("FROM SessionRecord r WHERE r.type = ? ORDER BY r.sessionDate %s",
                order.getDatabaseCode());
        return (List<SessionRecord>) this.getHibernateTemplate().find(query, type.name());
    }

    public void update(SessionRecord record) {
        this.getHibernateTemplate().update(record);
    }

    public int save(SessionRecord record) {
        return (Integer) this.getHibernateTemplate().save(record);
    }

    public void delete(DocumentRecord record) {
        this.getHibernateTemplate().delete(record);
    }
}
