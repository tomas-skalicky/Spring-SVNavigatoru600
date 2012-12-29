package com.svnavigatoru600.viewmodel.forum.contributions;

import org.springframework.stereotype.Service;

@Service
public class EditContribution extends NewEditContribution {

    private boolean dataSaved = false;

    public boolean isDataSaved() {
        return this.dataSaved;
    }

    public void setDataSaved(boolean dataSaved) {
        this.dataSaved = dataSaved;
    }
}
