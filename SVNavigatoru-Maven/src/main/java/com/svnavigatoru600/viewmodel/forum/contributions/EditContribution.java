package com.svnavigatoru600.viewmodel.forum.contributions;

import org.springframework.stereotype.Service;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class EditContribution extends AbstractNewEditContribution {

    private boolean dataSaved = false;

    public boolean isDataSaved() {
        return this.dataSaved;
    }

    public void setDataSaved(boolean dataSaved) {
        this.dataSaved = dataSaved;
    }
}
