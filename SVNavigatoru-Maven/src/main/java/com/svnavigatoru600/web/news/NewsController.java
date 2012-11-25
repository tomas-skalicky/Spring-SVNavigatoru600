package com.svnavigatoru600.web.news;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.repository.NewsDao;
import com.svnavigatoru600.web.PrivateSectionMetaController;


/**
 * Parent of all controllers which handle all operations upon the {@link News}.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class NewsController extends PrivateSectionMetaController {

	protected static final String BASE_URL = "/novinky/";
	protected NewsDao newsDao;
	protected MessageSource messageSource;

	public NewsController(NewsDao newsDao, MessageSource messageSource) {
		this.newsDao = newsDao;
		this.messageSource = messageSource;
	}
}