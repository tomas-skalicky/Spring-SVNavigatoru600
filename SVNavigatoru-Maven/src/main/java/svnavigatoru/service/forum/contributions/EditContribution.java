package svnavigatoru.service.forum.contributions;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class EditContribution extends NewEditContribution {

	private boolean dataSaved = false;

	public boolean isDataSaved() {
		return this.dataSaved;
	}

	public void setDataSaved(boolean dataSaved) {
		this.dataSaved = dataSaved;
	}

	@PreAuthorize("hasPermission(#contributionId, 'svnavigatoru.domain.forum.Contribution', 'edit')")
	public void canEdit(int contributionId) {
	}
}
