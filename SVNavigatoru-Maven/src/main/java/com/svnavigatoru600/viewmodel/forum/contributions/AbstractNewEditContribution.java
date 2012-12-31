package com.svnavigatoru600.viewmodel.forum.contributions;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.Contribution;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractNewEditContribution {

    private Contribution contribution = null;
    private int threadId = -1;

    public Contribution getContribution() {
        return this.contribution;
    }

    public void setContribution(Contribution contribution) {
        this.contribution = contribution;
    }

    public int getThreadId() {
        return this.threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }
}
