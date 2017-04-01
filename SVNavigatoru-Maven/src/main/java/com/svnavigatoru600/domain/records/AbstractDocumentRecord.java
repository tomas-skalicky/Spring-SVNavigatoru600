package com.svnavigatoru600.domain.records;

import java.io.Serializable;
import java.sql.Blob;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractDocumentRecord implements Serializable {

    private static final long serialVersionUID = -5778831731988822276L;

    /**
     * Artificial ID.
     */
    private int id;
    /**
     * The filename of the textual file which contains the full records. There is one directory which contains all
     * files; hence the filename contains no "/".
     */
    private String fileName;
    private Blob file;

    /**
     * Initialises no property.
     */
    protected AbstractDocumentRecord() {
    }

    /**
     * Initialises record's filename and file. Other properties are not touched.
     */
    protected AbstractDocumentRecord(String fileName, Blob file) {
        this.fileName = fileName;
        this.file = file;
    }

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

    @Override
    public String toString() {
        return new StringBuilder("[id=").append(this.id).append(", fileName=").append(this.fileName).append("]")
                .toString();
    }
}
