package com.svnavigatoru600.viewmodel.forum.contributions;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.viewmodel.SendNotification;
import com.svnavigatoru600.viewmodel.SendNotificationViewModel;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractNewEditContribution implements SendNotificationViewModel {

    private Contribution contribution = null;
    private int threadId = -1;
    private SendNotification sendNotification = null;

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

    @Override
    public SendNotification getSendNotification() {
        return this.sendNotification;
    }

    @Override
    public void setSendNotification(SendNotification sendNotification) {
        this.sendNotification = sendNotification;
    }

    @Override
    public String toString() {
        return new StringBuilder("[contribution=").append(this.contribution).append(", threadId=")
                .append(this.threadId).append(", sendNotification=").append(this.sendNotification)
                .append("]").toString();
    }
}
