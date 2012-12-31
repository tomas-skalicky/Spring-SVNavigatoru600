package com.svnavigatoru600.repository.records.impl.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.repository.records.impl.DocumentRecordField;
import com.svnavigatoru600.repository.records.impl.SessionRecordField;
import com.svnavigatoru600.service.util.OrderType;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class SessionRecordDaoImpl extends HibernateDaoSupport implements SessionRecordDao {

    @Override
    public SessionRecord findById(int recordId) {
        return this.getHibernateTemplate().load(SessionRecord.class, recordId);
    }

    @Override
    public SessionRecord findById(int recordId, boolean loadFile) {
        // TBD
        return null;
    }

    @Override
    public SessionRecord findByFileName(String fileName) {
        // Stands for 'where' clause.
        DetachedCriteria criteria = DetachedCriteria.forClass(SessionRecord.class);
        criteria.add(Restrictions.eq(DocumentRecordField.fileName.name(), fileName));

        @SuppressWarnings("unchecked")
        List<SessionRecord> records = (List<SessionRecord>) this.getHibernateTemplate().findByCriteria(
                criteria);
        if (records.size() > 1) {
            throw new DataIntegrityViolationException("Filename should be unique.");
        } else if (records.isEmpty()) {
            throw new DataRetrievalFailureException("No record associated with the given filename exists.");
        }
        return records.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SessionRecord> findAllOrdered(OrderType order) {
        String query = String.format("FROM %s r ORDER BY r.%s %s", PersistedClass.SessionRecord.name(),
                SessionRecordField.sessionDate.name(), order.getDatabaseCode());
        return (List<SessionRecord>) this.getHibernateTemplate().find(query);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<SessionRecord> findAllOrdered(SessionRecordType type, OrderType order) {
        String query = String.format("FROM %s r WHERE r.type = ? ORDER BY r.%s %s",
                PersistedClass.SessionRecord.name(), SessionRecordField.sessionDate.name(),
                order.getDatabaseCode());
        return (List<SessionRecord>) this.getHibernateTemplate().find(query, type.name());
    }

    @Override
    public void update(SessionRecord record) {
        this.getHibernateTemplate().update(record);
    }

    @Override
    public int save(SessionRecord record) {
        return (Integer) this.getHibernateTemplate().save(record);
    }

    @Override
    public void delete(AbstractDocumentRecord record) {
        this.getHibernateTemplate().delete(record);
    }
}
