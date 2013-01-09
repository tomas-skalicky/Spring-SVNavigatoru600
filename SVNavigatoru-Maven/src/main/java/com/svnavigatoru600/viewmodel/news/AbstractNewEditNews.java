package com.svnavigatoru600.viewmodel.news;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.viewmodel.SendNotification;
import com.svnavigatoru600.viewmodel.SendNotificationViewModel;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractNewEditNews implements SendNotificationViewModel {

    private News news = null;
    private SendNotification sendNotification = null;

    public News getNews() {
        return this.news;
    }

    public void setNews(News news) {
        this.news = news;
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
        return new StringBuilder("[news=").append(this.news).append(", sendNotification=")
                .append(this.sendNotification).append("]").toString();
    }
}
