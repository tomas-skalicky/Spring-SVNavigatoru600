package com.svnavigatoru600.web.news;

import com.svnavigatoru600.viewmodel.news.ShowAllNews;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class ListNewsResponse extends AbstractNewsResponse {

    /**
     * Holds data of the command.
     */
    private ShowAllNews command = null;

    public ListNewsResponse(ShowAllNews command) {
        this.successful = true;
        this.command = command;
    }

    public ShowAllNews getCommand() {
        return this.command;
    }
}
