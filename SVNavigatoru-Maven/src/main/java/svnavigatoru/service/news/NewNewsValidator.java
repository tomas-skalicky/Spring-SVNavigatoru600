package svnavigatoru.service.news;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import svnavigatoru.domain.News;

@Service
public class NewNewsValidator extends NewsValidator {

	public boolean supports(Class<?> clazz) {
		return NewNews.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		NewNews command = (NewNews) target;
		News news = command.getNews();
		this.checkNewTitle(news.getTitle(), errors);
		this.checkNewText(news.getText(), errors);
	}
}
