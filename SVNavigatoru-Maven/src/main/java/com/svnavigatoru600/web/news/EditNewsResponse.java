package com.svnavigatoru600.web.news;

import com.svnavigatoru600.viewmodel.news.AbstractNewEditNews;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class EditNewsResponse extends AbstractNewEditNewsResponse {

    public EditNewsResponse(AbstractNewEditNews command) {
        super(command);
    }
}
