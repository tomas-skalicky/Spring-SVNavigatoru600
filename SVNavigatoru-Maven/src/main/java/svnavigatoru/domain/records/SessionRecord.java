package svnavigatoru.domain.records;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import svnavigatoru.repository.records.SessionRecordDao;

public class SessionRecord extends DocumentRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7056147517890905426L;

	@SuppressWarnings("unused")
	private SessionRecordDao recordDao;

	public void setSessionRecordDao(SessionRecordDao recordDao) {
		this.recordDao = recordDao;
	}

	/**
	 * Type of this {@link SessionRecord}.
	 */
	private SessionRecordType type;
	/**
	 * {@link Date} when an appropriate session took place. The date is
	 * determined by the user.
	 */
	@DateTimeFormat(style = "L-")
	private Date sessionDate;
	/**
	 * The important topics which have been discussed during an appropriate
	 * session.
	 */
	private String discussedTopics;

	/**
	 * Different - not "getType" - name of the getter method is necessary.
	 * Otherwise, the methods' signatures would be identical.
	 */
	public SessionRecordType getTypedType() {
		return this.type;
	}

	/**
	 * This getter is necessary because of Hibernate.
	 */
	public String getType() {
		return this.type.name();
	}

	public void setType(SessionRecordType type) {
		this.type = type;
	}

	/**
	 * This setter is necessary because of Hibernate.
	 */
	public void setType(String type) {
		this.type = SessionRecordType.valueOf(type);
	}

	public Date getSessionDate() {
		return this.sessionDate;
	}

	public void setSessionDate(Date sessionDate) {
		this.sessionDate = sessionDate;
	}

	public String getDiscussedTopics() {
		return this.discussedTopics;
	}

	public void setDiscussedTopics(String discussedTopics) {
		this.discussedTopics = discussedTopics;
	}
}
