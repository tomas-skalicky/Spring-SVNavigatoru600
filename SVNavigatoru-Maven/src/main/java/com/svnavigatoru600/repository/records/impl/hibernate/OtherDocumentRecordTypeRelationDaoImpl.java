package com.svnavigatoru600.repository.records.impl.hibernate;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation;
import com.svnavigatoru600.repository.records.OtherDocumentRecordTypeRelationDao;
import com.svnavigatoru600.repository.records.impl.OtherDocumentRecordTypeRelationField;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("otherDocumentRecordTypeRelationDao")
public class OtherDocumentRecordTypeRelationDaoImpl extends HibernateDaoSupport implements
        OtherDocumentRecordTypeRelationDao {

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public OtherDocumentRecordTypeRelationDaoImpl(SessionFactory sessionFactory) {
        super();
        setSessionFactory(sessionFactory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OtherDocumentRecordTypeRelation> findAll(int recordId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(OtherDocumentRecordTypeRelation.class);
        criteria.add(Restrictions.eq(OtherDocumentRecordTypeRelationField.recordId.getFieldChain(), recordId));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    @Override
    public void save(Collection<OtherDocumentRecordTypeRelation> typeRelations) {
        for (OtherDocumentRecordTypeRelation typeRelation : typeRelations) {
            getHibernateTemplate().save(typeRelation);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(int recordId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(OtherDocumentRecordTypeRelation.class);
        criteria.add(Restrictions.eq(OtherDocumentRecordTypeRelationField.recordId.getFieldChain(), recordId));

        getHibernateTemplate().deleteAll(getHibernateTemplate().findByCriteria(criteria));
    }
}
