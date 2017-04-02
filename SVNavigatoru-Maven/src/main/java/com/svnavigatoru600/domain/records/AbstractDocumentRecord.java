package com.svnavigatoru600.domain.records;

import java.io.Serializable;
import java.sql.Blob;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractDocumentRecord implements Serializable {

    private static final long serialVersionUID = 1L;

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
    protected AbstractDocumentRecord(final String fileName, final Blob file) {
        this.fileName = fileName;
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public Blob getFile() {
        return file;
    }

    public void setFile(final Blob file) {
        this.file = file;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("AbstractDocumentRecord [id=");
        builder.append(id);
        builder.append(", fileName=");
        builder.append(fileName);
        builder.append(", file=");
        builder.append(file);
        builder.append("]");
        return builder.toString();
    }
}
