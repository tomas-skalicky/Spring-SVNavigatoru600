package svnavigatoru.service.news;

import org.springframework.stereotype.Service;

import svnavigatoru.domain.News;

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
