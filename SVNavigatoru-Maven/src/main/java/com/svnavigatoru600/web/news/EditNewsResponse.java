package com.svnavigatoru600.web.news;

import com.svnavigatoru600.service.news.NewEditNews;

public class EditNewsResponse extends NewEditNewsResponse {

    public EditNewsResponse(NewEditNews command) {
        super(command);
    }
}
