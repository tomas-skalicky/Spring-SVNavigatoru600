package svnavigatoru.service.records.otherdocuments;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import svnavigatoru.domain.records.OtherDocumentRecord;
import svnavigatoru.domain.records.OtherDocumentRecordType;

@Service
public class NewEditRecord {

	private OtherDocumentRecord record = null;
	private MultipartFile newFile = null;
	private boolean[] newTypes = new boolean[OtherDocumentRecordType.values().length];
	private Map<Integer, String> typeCheckboxId = null;
	private Map<Integer, String> localizedTypeCheckboxTitles = null;

	public OtherDocumentRecord getRecord() {
		return this.record;
	}

	public void setRecord(OtherDocumentRecord record) {
		this.record = record;
	}

	public MultipartFile getNewFile() {
		return this.newFile;
	}

	public void setNewFile(MultipartFile newFile) {
		this.newFile = newFile;
	}

	public boolean[] getNewTypes() {
		return this.newTypes;
	}

	public void setNewTypes(boolean[] newTypes) {
		this.newTypes = newTypes;
	}

	public Map<Integer, String> getTypeCheckboxId() {
		return this.typeCheckboxId;
	}

	public void setTypeCheckboxId(Map<Integer, String> typeCheckboxId) {
		this.typeCheckboxId = typeCheckboxId;
	}

	public Map<Integer, String> getLocalizedTypeCheckboxTitles() {
		return this.localizedTypeCheckboxTitles;
	}

	public void setLocalizedTypeCheckboxTitles(Map<Integer, String> localizedTypeCheckboxTitles) {
		this.localizedTypeCheckboxTitles = localizedTypeCheckboxTitles;
	}
}
