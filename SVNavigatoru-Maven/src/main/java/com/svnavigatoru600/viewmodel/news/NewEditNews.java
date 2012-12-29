package com.svnavigatoru600.viewmodel.news;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.News;

@Service
public abstract class NewEditNews {

    private News news = null;

    public News getNews() {
        return this.news;
    }

    public void setNews(News news) {
        this.news = news;
    }
}
