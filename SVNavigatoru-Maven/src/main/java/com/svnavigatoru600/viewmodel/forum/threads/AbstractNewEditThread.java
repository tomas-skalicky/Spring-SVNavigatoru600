package com.svnavigatoru600.viewmodel.forum.threads;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.ForumThread;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractNewEditThread {

    private ForumThread thread = null;

    public ForumThread getThread() {
        return this.thread;
    }

    public void setThread(ForumThread thread) {
        this.thread = thread;
    }

    @Override
    public String toString() {
        return new StringBuilder("[thread=").append(this.thread).append("]").toString();
    }
}
