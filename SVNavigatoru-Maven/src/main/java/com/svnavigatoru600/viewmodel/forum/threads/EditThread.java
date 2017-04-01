package com.svnavigatoru600.viewmodel.forum.threads;

import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class EditThread extends AbstractNewEditThread {

    private boolean dataSaved = false;

    public boolean isDataSaved() {
        return this.dataSaved;
    }

    public void setDataSaved(boolean dataSaved) {
        this.dataSaved = dataSaved;
    }

    @Override
    public String toString() {
        return new StringBuilder(super.toString()).append(" [dataSaved=").append(this.dataSaved).append("]").toString();
    }
}
