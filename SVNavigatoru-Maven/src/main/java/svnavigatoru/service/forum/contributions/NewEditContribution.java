package svnavigatoru.service.forum.contributions;

import org.springframework.stereotype.Service;

import svnavigatoru.domain.forum.Contribution;

@Service
public abstract class NewEditContribution {

	private Contribution contribution = null;
	private int threadId = -1;

	public Contribution getContribution() {
		return this.contribution;
	}

	public void setContribution(Contribution contribution) {
		this.contribution = contribution;
	}

	public int getThreadId() {
		return this.threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}
}
