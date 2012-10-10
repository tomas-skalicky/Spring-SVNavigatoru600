package svnavigatoru.service.forum.threads;

import org.springframework.stereotype.Service;

import svnavigatoru.domain.forum.Thread;

@Service
public abstract class NewEditThread {

	private Thread thread = null;

	public Thread getThread() {
		return this.thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}
}
