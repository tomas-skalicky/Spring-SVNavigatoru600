package svnavigatoru.repository.records.impl.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import svnavigatoru.domain.records.DocumentRecord;
import svnavigatoru.domain.records.OtherDocumentRecord;
import svnavigatoru.domain.records.OtherDocumentRecordType;
import svnavigatoru.repository.records.OtherDocumentRecordDao;
import svnavigatoru.repository.records.OtherDocumentRecordTypeRelationDao;
import svnavigatoru.service.util.OrderType;

public class OtherDocumentRecordDaoImpl extends HibernateDaoSupport implements OtherDocumentRecordDao {

	private OtherDocumentRecordTypeRelationDao typeDao;

	@Autowired
	public void setOtherDocumentRecordTypeRelationDao(OtherDocumentRecordTypeRelationDao typeDao) {
		this.typeDao = typeDao;
	}

	public OtherDocumentRecord findById(int recordId) {
		return this.getHibernateTemplate().load(OtherDocumentRecord.class, recordId);
	}

	public OtherDocumentRecord findById(int recordId, boolean loadFile) {
		// TBD
		return null;
	}

	public OtherDocumentRecord findByFileName(String fileName) {
		// Stands for 'where' clause.
		DetachedCriteria criteria = DetachedCriteria.forClass(OtherDocumentRecord.class);
		criteria.add(Restrictions.eq("fileName", fileName));

		@SuppressWarnings("unchecked")
		List<OtherDocumentRecord> records = (List<OtherDocumentRecord>) this.getHibernateTemplate().findByCriteria(
				criteria);
		if (records.size() > 1) {
			throw new DataIntegrityViolationException("Filename should be unique.");
		} else if (records.size() == 0) {
			throw new DataRetrievalFailureException("No record associated with the given filename exists.");
		}
		return records.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<OtherDocumentRecord> findOrdered(OrderType order) {
		String query = String.format("FROM OtherDocumentRecord r ORDER BY r.creationTime %s", order.getDatabaseCode());
		return (List<OtherDocumentRecord>) this.getHibernateTemplate().find(query);
	}

	@SuppressWarnings("unchecked")
	public List<OtherDocumentRecord> findOrdered(OtherDocumentRecordType type, OrderType order) {
		String query = String.format("SELECT r FROM OtherDocumentRecord r INNER JOIN r.types t"
				+ " WHERE t.id.type = ? ORDER BY r.creationTime %s", order.getDatabaseCode());
		return (List<OtherDocumentRecord>) this.getHibernateTemplate().find(query, type.name());
	}

	public void update(OtherDocumentRecord record) {
		record.setLastSaveTime(new Date());
		this.getHibernateTemplate().update(record);

		// Updates types.
		this.typeDao.delete(record.getId());
		this.typeDao.save(record.getTypes());
	}

	public int save(OtherDocumentRecord record) {
		Date now = new Date();
		record.setCreationTime(now);
		record.setLastSaveTime(now);
		return (Integer) this.getHibernateTemplate().save(record);

		// Not necessary to save types explicitly. The command above has already
		// done it.
	}

	public void delete(DocumentRecord record) {
		this.getHibernateTemplate().delete(record);
	}
}
