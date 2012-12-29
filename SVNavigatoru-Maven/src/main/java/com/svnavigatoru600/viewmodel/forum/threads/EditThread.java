package com.svnavigatoru600.viewmodel.forum.threads;

import org.springframework.stereotype.Service;


@Service
public class EditThread extends NewEditThread {

    private boolean dataSaved = false;

    public boolean isDataSaved() {
        return this.dataSaved;
    }

    public void setDataSaved(boolean dataSaved) {
        this.dataSaved = dataSaved;
    }
}
