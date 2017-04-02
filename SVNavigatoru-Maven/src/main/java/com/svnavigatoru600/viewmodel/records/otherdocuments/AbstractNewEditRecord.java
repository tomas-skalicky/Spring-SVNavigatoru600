package com.svnavigatoru600.viewmodel.records.otherdocuments;

import java.util.Arrays;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeEnum;
import com.svnavigatoru600.viewmodel.SendNotification;
import com.svnavigatoru600.viewmodel.SendNotificationViewModel;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractNewEditRecord implements SendNotificationViewModel {

    private OtherDocumentRecord record = null;
    private MultipartFile newFile = null;
    private boolean[] newTypes = new boolean[OtherDocumentRecordTypeEnum.values().length];
    private Map<Long, String> typeCheckboxId = null;
    private Map<Long, String> localizedTypeCheckboxTitles = null;
    private SendNotification sendNotification = null;

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

    @Override
    public SendNotification getSendNotification() {
        return this.sendNotification;
    }

    @Override
    public void setSendNotification(SendNotification sendNotification) {
        this.sendNotification = sendNotification;
    }

    @Override
    public String toString() {
        return new StringBuilder("[record=").append(this.record).append(", newTypes=")
                .append(Arrays.toString(this.newTypes)).append(", typeCheckboxId=").append(this.typeCheckboxId)
                .append(", localizedTypeCheckboxTitles=").append(this.localizedTypeCheckboxTitles)
                .append(", sendNotification=").append(this.sendNotification).append("]").toString();
    }
}
