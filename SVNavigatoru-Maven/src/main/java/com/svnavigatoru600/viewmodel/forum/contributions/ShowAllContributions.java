package com.svnavigatoru600.viewmodel.forum.contributions;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.ForumContribution;
import com.svnavigatoru600.domain.forum.ForumThread;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class ShowAllContributions {

    private List<ForumContribution> contributions;
    private ForumThread thread;
    private Map<ForumContribution, String> localizedDeleteQuestions = null;
    private boolean contributionCreated = false;
    private boolean contributionDeleted = false;

    public List<ForumContribution> getContributions() {
        return this.contributions;
    }

    public void setContributions(List<ForumContribution> contributions) {
        this.contributions = contributions;
    }

    public ForumThread getThread() {
        return this.thread;
    }

    public void setThread(ForumThread thread) {
        this.thread = thread;
    }

    public Map<ForumContribution, String> getLocalizedDeleteQuestions() {
        return this.localizedDeleteQuestions;
    }

    public void setLocalizedDeleteQuestions(Map<ForumContribution, String> localizedDeleteQuestions) {
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
