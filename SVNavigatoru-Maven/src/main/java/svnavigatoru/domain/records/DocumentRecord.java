package svnavigatoru.domain.records;

import java.io.Serializable;
import java.sql.Blob;

public abstract class DocumentRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5778831731988822276L;

	/**
	 * Artificial ID.
	 */
	private int id;
	/**
	 * The filename of the textual file which contains the full records. There
	 * is one directory which contains all files; hence the filename contains no
	 * "/".
	 */
	private String fileName;
	private Blob file;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Blob getFile() {
		return this.file;
	}

	public void setFile(Blob file) {
		this.file = file;
	}
}
