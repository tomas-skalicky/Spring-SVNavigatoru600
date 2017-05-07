package com.svnavigatoru600.common.aspects;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @author Tomas Skalicky
 * @since 07.05.2017
 */
public class StringListAppender extends AppenderSkeleton {

    private final List<String> renderedMessages = new ArrayList<>();

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

    @Override
    protected void append(final LoggingEvent event) {
        renderedMessages.add(event.getRenderedMessage());
    }

    public List<String> getMessages() {
        return renderedMessages;
    }

}
