package com.svnavigatoru600.viewmodel.news;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.viewmodel.SendNotification;
import com.svnavigatoru600.viewmodel.SendNotificationViewModel;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractNewEditNews implements SendNotificationViewModel {

    private News news = null;
    private SendNotification sendNotification = null;

    public News getNews() {
        return news;
    }

    public void setNews(final News news) {
        this.news = news;
    }

    @Override
    public SendNotification getSendNotification() {
        return sendNotification;
    }

    @Override
    public void setSendNotification(final SendNotification sendNotification) {
        this.sendNotification = sendNotification;
    }

    @Override
    public String toString() {
        return new StringBuilder("[news=").append(news).append(", sendNotification=")
                .append(sendNotification).append("]").toString();
    }
}
