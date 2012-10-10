package svnavigatoru.service.forum.threads;

import org.springframework.stereotype.Service;

import svnavigatoru.domain.forum.Contribution;

@Service
public class NewThread extends NewEditThread {

	private Contribution contribution = null;

	public Contribution getContribution() {
		return this.contribution;
	}

	public void setContribution(Contribution contribution) {
		this.contribution = contribution;
	}
}
