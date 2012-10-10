package svnavigatoru.service.records.otherdocuments;

import org.springframework.stereotype.Service;

@Service
public class EditRecord extends NewEditRecord {

	private boolean fileChanged = false;
	private boolean dataSaved = false;

	public boolean isFileChanged() {
		return fileChanged;
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
