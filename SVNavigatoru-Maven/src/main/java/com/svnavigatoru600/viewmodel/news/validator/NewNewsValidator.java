package com.svnavigatoru600.viewmodel.news.validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.viewmodel.news.NewNews;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class NewNewsValidator extends AbstractNewsValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return NewNews.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewNews command = (NewNews) target;
        News news = command.getNews();
        checkNewTitle(news.getTitle(), errors);
        checkNewText(news.getText(), errors);
    }
}
