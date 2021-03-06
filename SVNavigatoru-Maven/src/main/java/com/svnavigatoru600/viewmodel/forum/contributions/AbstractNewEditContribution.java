package com.svnavigatoru600.viewmodel.forum.contributions;

import com.svnavigatoru600.domain.forum.ForumContribution;
import com.svnavigatoru600.viewmodel.SendNotification;
import com.svnavigatoru600.viewmodel.SendNotificationViewModel;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractNewEditContribution implements SendNotificationViewModel {

    private ForumContribution contribution = null;
    private SendNotification sendNotification = null;

    public ForumContribution getContribution() {
        return this.contribution;
    }

    public void setContribution(ForumContribution contribution) {
        this.contribution = contribution;
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
        return new StringBuilder("[contribution=").append(this.contribution).append(", sendNotification=")
                .append(this.sendNotification).append("]").toString();
    }
}
