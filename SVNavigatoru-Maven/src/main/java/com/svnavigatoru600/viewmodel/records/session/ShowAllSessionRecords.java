package com.svnavigatoru600.viewmodel.records.session;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.records.SessionRecord;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class ShowAllSessionRecords {

    private List<SessionRecord> records;
    private boolean allRecordTypes = false;
    private Map<SessionRecord, String> localizedTypeTitles = null;
    private Map<SessionRecord, String> localizedSessionDates = null;
    private Map<SessionRecord, String> localizedDeleteQuestions = null;
    private boolean recordCreated = false;
    private boolean recordDeleted = false;

    public List<SessionRecord> getRecords() {
        return this.records;
    }

    public void setRecords(List<SessionRecord> records) {
        this.records = records;
    }

    public boolean isAllRecordTypes() {
        return this.allRecordTypes;
    }

    public void setAllRecordTypes(boolean allRecordTypes) {
        this.allRecordTypes = allRecordTypes;
    }

    public Map<SessionRecord, String> getLocalizedTypeTitles() {
        return this.localizedTypeTitles;
    }

    public void setLocalizedTypeTitles(Map<SessionRecord, String> localizedTypeTitles) {
        this.localizedTypeTitles = localizedTypeTitles;
    }

    public Map<SessionRecord, String> getLocalizedSessionDates() {
        return this.localizedSessionDates;
    }

    public void setLocalizedSessionDates(Map<SessionRecord, String> localizedSessionDates) {
        this.localizedSessionDates = localizedSessionDates;
    }

    public Map<SessionRecord, String> getLocalizedDeleteQuestions() {
        return this.localizedDeleteQuestions;
    }

    public void setLocalizedDeleteQuestions(Map<SessionRecord, String> localizedDeleteQuestions) {
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
