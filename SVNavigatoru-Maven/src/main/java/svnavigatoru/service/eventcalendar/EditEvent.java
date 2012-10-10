package svnavigatoru.service.eventcalendar;

import org.springframework.stereotype.Service;

@Service
public class EditEvent extends NewEditEvent {

	private boolean dataSaved = false;

	public boolean isDataSaved() {
		return this.dataSaved;
	}

	public void setDataSaved(boolean dataSaved) {
		this.dataSaved = dataSaved;
	}
}
