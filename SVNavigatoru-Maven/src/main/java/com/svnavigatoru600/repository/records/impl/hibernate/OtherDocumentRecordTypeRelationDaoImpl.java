package com.svnavigatoru600.repository.records.impl.hibernate;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation;
import com.svnavigatoru600.repository.records.OtherDocumentRecordTypeRelationDao;

public class OtherDocumentRecordTypeRelationDaoImpl extends HibernateDaoSupport implements
        OtherDocumentRecordTypeRelationDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<OtherDocumentRecordTypeRelation> find(int recordId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(OtherDocumentRecordTypeRelation.class);
        criteria.add(Restrictions.eq("id.recordId", recordId));

        return (List<OtherDocumentRecordTypeRelation>) this.getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void save(Collection<OtherDocumentRecordTypeRelation> types) {
        for (OtherDocumentRecordTypeRelation type : types) {
            this.getHibernateTemplate().save(type);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(int recordId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(OtherDocumentRecordTypeRelation.class);
        criteria.add(Restrictions.eq("id.recordId", recordId));

        this.getHibernateTemplate().deleteAll(
                (List<OtherDocumentRecordTypeRelation>) this.getHibernateTemplate().findByCriteria(criteria));
    }
}
