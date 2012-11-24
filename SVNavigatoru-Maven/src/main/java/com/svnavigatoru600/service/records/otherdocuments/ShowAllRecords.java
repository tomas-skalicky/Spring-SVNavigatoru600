package com.svnavigatoru600.service.records.otherdocuments;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;


@Service
public class ShowAllRecords {

	private List<OtherDocumentRecord> records;
	private Map<OtherDocumentRecord, String> localizedDeleteQuestions = null;
	private boolean recordCreated = false;
	private boolean recordDeleted = false;

	public List<OtherDocumentRecord> getRecords() {
		return this.records;
	}

	public void setRecords(List<OtherDocumentRecord> records) {
		this.records = records;
	}

	public Map<OtherDocumentRecord, String> getLocalizedDeleteQuestions() {
		return this.localizedDeleteQuestions;
	}

	public void setLocalizedDeleteQuestions(Map<OtherDocumentRecord, String> localizedDeleteQuestions) {
		this.localizedDeleteQuestions = localizedDeleteQuestions;
	}

	public boolean isRecordCreated() {
		return this.recordCreated;
	}

	public void setRecordCreated(boolean recordCreated) {
		this.recordCreated = recordCreated;
	}

	public boolean isRecordDeleted() {
		return this.recordDeleted;
	}

	public void setRecordDeleted(boolean recordDeleted) {
		this.recordDeleted = recordDeleted;
	}
}
