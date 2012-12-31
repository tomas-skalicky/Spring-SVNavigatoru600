package com.svnavigatoru600.viewmodel.records.session;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractNewEditSessionRecord {

    private SessionRecord record = null;
    private MultipartFile newFile = null;
    /**
     * Holds a value of the <code>newType</code> radiobutton. The value equals a localization of the selected
     * {@link SessionRecordType}. See the <code>NewEditRecordController.populateSessionRecordTypeList</code>
     * method.
     */
    private String newType;

    public SessionRecord getRecord() {
        return this.record;
    }

    public void setRecord(SessionRecord record) {
        this.record = record;
    }

    public MultipartFile getNewFile() {
        return this.newFile;
    }

    public void setNewFile(MultipartFile newFile) {
        this.newFile = newFile;
    }

    public String getNewType() {
        return this.newType;
    }

    public void setNewType(String newType) {
        this.newType = newType;
    }
}
