package svnavigatoru.service.records.session;

import org.springframework.stereotype.Service;

@Service
public class EditSessionRecord extends NewEditSessionRecord {

	private boolean fileChanged = false;
	private boolean dataSaved = false;

	public boolean isFileChanged() {
		return this.fileChanged;
	}

	public void setFileChanged(boolean fileChanged) {
		this.fileChanged = fileChanged;
	}

	public boolean isDataSaved() {
		return this.dataSaved;
	}

	public void setDataSaved(boolean dataSaved) {
		this.dataSaved = dataSaved;
	}
}
