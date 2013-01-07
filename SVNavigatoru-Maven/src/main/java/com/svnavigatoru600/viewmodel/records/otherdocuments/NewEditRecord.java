package com.svnavigatoru600.viewmodel.records.otherdocuments;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordType;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class NewEditRecord {

    private OtherDocumentRecord record = null;
    private MultipartFile newFile = null;
    private boolean[] newTypes = new boolean[OtherDocumentRecordType.values().length];
    private Map<Long, String> typeCheckboxId = null;
    private Map<Long, String> localizedTypeCheckboxTitles = null;

    public OtherDocumentRecord getRecord() {
        return this.record;
    }

    public void setRecord(OtherDocumentRecord record) {
        this.record = record;
    }

    public MultipartFile getNewFile() {
        return this.newFile;
    }

    public void setNewFile(MultipartFile newFile) {
        this.newFile = newFile;
    }

    public boolean[] getNewTypes() {
        return this.newTypes;
    }

    public void setNewTypes(boolean[] newTypes) {
        this.newTypes = newTypes;
    }

    public Map<Long, String> getTypeCheckboxId() {
        return this.typeCheckboxId;
    }

    public void setTypeCheckboxId(Map<Long, String> typeCheckboxId) {
        this.typeCheckboxId = typeCheckboxId;
    }

    public Map<Long, String> getLocalizedTypeCheckboxTitles() {
        return this.localizedTypeCheckboxTitles;
    }

    public void setLocalizedTypeCheckboxTitles(Map<Long, String> localizedTypeCheckboxTitles) {
        this.localizedTypeCheckboxTitles = localizedTypeCheckboxTitles;
    }
}
