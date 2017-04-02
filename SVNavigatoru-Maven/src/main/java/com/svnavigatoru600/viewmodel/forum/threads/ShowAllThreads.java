package com.svnavigatoru600.viewmodel.forum.threads;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.ForumContribution;
import com.svnavigatoru600.domain.forum.ForumThread;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class ShowAllThreads {

    private List<ForumThread> threads = null;
    private Map<ForumThread, ForumContribution> lastSavedContributions = null;
    private Map<ForumThread, String> localizedDeleteQuestions = null;
    private boolean threadCreated = false;
    private boolean threadDeleted = false;

    public List<ForumThread> getThreads() {
        return this.threads;
    }

    public void setThreads(List<ForumThread> threads) {
        this.threads = threads;
    }

    public Map<ForumThread, ForumContribution> getLastSavedContributions() {
        return this.lastSavedContributions;
    }

    public void setLastSavedContributions(Map<ForumThread, ForumContribution> lastSavedContributions) {
        this.lastSavedContributions = lastSavedContributions;
    }

    public Map<ForumThread, String> getLocalizedDeleteQuestions() {
        return this.localizedDeleteQuestions;
    }

    public void setLocalizedDeleteQuestions(Map<ForumThread, String> localizedDeleteQuestions) {
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
