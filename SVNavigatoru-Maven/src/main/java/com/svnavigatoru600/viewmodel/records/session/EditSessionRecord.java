package com.svnavigatoru600.viewmodel.records.session;

import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class EditSessionRecord extends AbstractNewEditSessionRecord {

    private boolean fileChanged = false;
    private boolean dataSaved = false;

    public boolean isFileChanged() {
        return this.fileChanged;
    }

    public void setFileChanged(boolean fileChanged) {
        this.fileChanged = fileChanged;
    }

    public boolean isDataSaved() {
        return this.dataSaved;
    }

    public void setDataSaved(boolean dataSaved) {
        this.dataSaved = dataSaved;
    }
}
