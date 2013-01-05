package com.svnavigatoru600.repository.records.impl.hibernate;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.svnavigatoru600.domain.records.AbstractDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.repository.records.OtherDocumentRecordTypeRelationDao;
import com.svnavigatoru600.repository.records.impl.OtherDocumentRecordField;
import com.svnavigatoru600.repository.records.impl.OtherDocumentRecordTypeRelationField;
import com.svnavigatoru600.service.util.OrderType;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class OtherDocumentRecordDaoImpl extends HibernateDaoSupport implements OtherDocumentRecordDao {

    private OtherDocumentRecordTypeRelationDao typeDao;

    @Inject
    public void setOtherDocumentRecordTypeRelationDao(OtherDocumentRecordTypeRelationDao typeDao) {
        this.typeDao = typeDao;
    }

    @Override
    public OtherDocumentRecord findById(int recordId) {
        return this.getHibernateTemplate().load(OtherDocumentRecord.class, recordId);
    }

    @Override
    public OtherDocumentRecord findById(int recordId, boolean loadFile) {
        // TBD
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OtherDocumentRecord> findAllOrdered(OrderType order) {
        String query = String.format("FROM %s r ORDER BY r.%s %s", PersistedClass.OtherDocumentRecord.name(),
                OtherDocumentRecordField.creationTime.name(), order.getDatabaseCode());
        return (List<OtherDocumentRecord>) this.getHibernateTemplate().find(query);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OtherDocumentRecord> findAllOrdered(OtherDocumentRecordType type, OrderType order) {
        String query = String.format(
                "SELECT r FROM %s r INNER JOIN r.types t WHERE t.%s = ? ORDER BY r.%s %s",
                PersistedClass.OtherDocumentRecord.name(),
                OtherDocumentRecordTypeRelationField.type.getFieldChain(),
                OtherDocumentRecordField.creationTime.name(), order.getDatabaseCode());
        return (List<OtherDocumentRecord>) this.getHibernateTemplate().find(query, type.name());
    }

    @Override
    public void update(OtherDocumentRecord record) {
        Date now = new Date();
        record.setLastSaveTime(now);
        this.getHibernateTemplate().update(record);

        // Updates types.
        this.typeDao.delete(record.getId());
        this.typeDao.save(record.getTypes());
    }

    @Override
    public int save(OtherDocumentRecord record) {
        Date now = new Date();
        record.setCreationTime(now);
        record.setLastSaveTime(now);
        return (Integer) this.getHibernateTemplate().save(record);

        // Not necessary to save types explicitly. The command above has already
        // done it.
    }

    @Override
    public void delete(AbstractDocumentRecord record) {
        this.getHibernateTemplate().delete(record);
    }
}
