package com.svnavigatoru600.web.news;

import com.svnavigatoru600.viewmodel.news.ShowAllNews;

public class ListNewsResponse extends NewsResponse {

    /**
     * Holds data of the command.
     */
    private ShowAllNews command = null;

    public ListNewsResponse(ShowAllNews command) {
        this.successful = true;
        this.command = command;
    }

    public ShowAllNews getCommand() {
        return command;
    }
}
