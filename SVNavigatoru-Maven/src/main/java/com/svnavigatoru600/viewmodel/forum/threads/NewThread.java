package com.svnavigatoru600.viewmodel.forum.threads;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.Contribution;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class NewThread extends AbstractNewEditThread {

    private Contribution contribution = null;

    public Contribution getContribution() {
        return this.contribution;
    }

    public void setContribution(Contribution contribution) {
        this.contribution = contribution;
    }
}
