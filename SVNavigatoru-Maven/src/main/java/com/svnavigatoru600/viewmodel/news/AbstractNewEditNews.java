package com.svnavigatoru600.viewmodel.news;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.News;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractNewEditNews {

    private News news = null;

    public News getNews() {
        return this.news;
    }

    public void setNews(News news) {
        this.news = news;
    }
}
