package com.svnavigatoru600.viewmodel.forum.contributions;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.forum.Thread;

@Service
public class ShowAllContributions {

    private List<Contribution> contributions;
    private Thread thread;
    private Map<Contribution, String> localizedDeleteQuestions = null;
    private boolean contributionCreated = false;
    private boolean contributionDeleted = false;

    public List<Contribution> getContributions() {
        return this.contributions;
    }

    public void setContributions(List<Contribution> contributions) {
        this.contributions = contributions;
    }

    public Thread getThread() {
        return this.thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Map<Contribution, String> getLocalizedDeleteQuestions() {
        return this.localizedDeleteQuestions;
    }

    public void setLocalizedDeleteQuestions(Map<Contribution, String> localizedDeleteQuestions) {
        this.localizedDeleteQuestions = localizedDeleteQuestions;
    }

    public boolean isContributionCreated() {
        return this.contributionCreated;
    }

    public void setContributionCreated(boolean contributionCreated) {
        this.contributionCreated = contributionCreated;
    }

    public boolean isContributionDeleted() {
        return this.contributionDeleted;
    }

    public void setContributionDeleted(boolean contributionDeleted) {
        this.contributionDeleted = contributionDeleted;
    }
}
