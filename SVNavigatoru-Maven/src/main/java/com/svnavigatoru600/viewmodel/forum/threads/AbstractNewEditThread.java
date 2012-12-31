package com.svnavigatoru600.viewmodel.forum.threads;

import org.springframework.stereotype.Service;

import com.svnavigatoru600.domain.forum.Thread;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractNewEditThread {

    private Thread thread = null;

    public Thread getThread() {
        return this.thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }
}
