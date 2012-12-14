package com.svnavigatoru600.service.news;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.domain.News;

@Service
public class EditNewsValidator extends NewsValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EditNews.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EditNews command = (EditNews) target;
        News news = command.getNews();
        this.checkNewTitle(news.getTitle(), errors);
        this.checkNewText(news.getText(), errors);
    }
}
