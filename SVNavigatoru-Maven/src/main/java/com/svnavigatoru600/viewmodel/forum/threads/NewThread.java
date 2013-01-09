package com.svnavigatoru600.viewmodel.forum.threads;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.viewmodel.SendNotification;
import com.svnavigatoru600.viewmodel.SendNotificationViewModel;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class NewThread extends AbstractNewEditThread implements SendNotificationViewModel {

    private Contribution contribution = null;
    private SendNotification sendNotification = null;

    public Contribution getContribution() {
        return this.contribution;
    }

    public void setContribution(Contribution contribution) {
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
        return new StringBuilder(super.toString()).append(" [contribution=").append(this.contribution)
                .append(", sendNotification=").append(this.sendNotification).append("]").toString();
    }
}
