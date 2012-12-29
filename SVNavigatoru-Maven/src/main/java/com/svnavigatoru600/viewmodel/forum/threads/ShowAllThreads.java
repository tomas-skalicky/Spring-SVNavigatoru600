package com.svnavigatoru600.viewmodel.forum.threads;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.forum.Thread;

@Service
public class ShowAllThreads {

    private List<Thread> threads = null;
    private Map<Thread, Contribution> lastSavedContributions = null;
    private Map<Thread, String> localizedDeleteQuestions = null;
    private boolean threadCreated = false;
    private boolean threadDeleted = false;

    public List<Thread> getThreads() {
        return this.threads;
    }

    public void setThreads(List<Thread> threads) {
        this.threads = threads;
    }

    public Map<Thread, Contribution> getLastSavedContributions() {
        return this.lastSavedContributions;
    }

    public void setLastSavedContributions(Map<Thread, Contribution> lastSavedContributions) {
        this.lastSavedContributions = lastSavedContributions;
    }

    public Map<Thread, String> getLocalizedDeleteQuestions() {
        return this.localizedDeleteQuestions;
    }

    public void setLocalizedDeleteQuestions(Map<Thread, String> localizedDeleteQuestions) {
        this.localizedDeleteQuestions = localizedDeleteQuestions;
    }

    public boolean isThreadCreated() {
        return this.threadCreated;
    }

    public void setThreadCreated(boolean threadCreated) {
        this.threadCreated = threadCreated;
    }

    public boolean isThreadDeleted() {
        return this.threadDeleted;
    }

    public void setThreadDeleted(boolean threadDeleted) {
        this.threadDeleted = threadDeleted;
    }
}
