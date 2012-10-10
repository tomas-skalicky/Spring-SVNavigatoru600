package svnavigatoru.service.forum.threads;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class EditThread extends NewEditThread {

	private boolean dataSaved = false;

	public boolean isDataSaved() {
		return this.dataSaved;
	}

	public void setDataSaved(boolean dataSaved) {
		this.dataSaved = dataSaved;
	}

	@PreAuthorize("hasPermission(#threadId, 'svnavigatoru.domain.forum.Thread', 'edit')")
	public void canEdit(int threadId) {
	}
}
