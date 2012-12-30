package com.svnavigatoru600.repository.records.impl.direct;

/**
 * For more information, see {@link com.svnavigatoru600.repository.users.impl.direct.UserRowMapper
 * UserRowMapper}. This class cannot implement the interface {@link org.springframework.jdbc.core.RowMapper
 * RowMapper} since the class {@link com.svnavigatoru600.domain.records.DocumentRecord DocumentRecord} is not
 * instantiable - it is abstract.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class DocumentRecordRowMapper {

    /**
     * The default value of the indication whether to load <b>Blob</b> file, or not.
     */
    private static final boolean DEFAULT_LOAD_FILE = true;

    /**
     * Determines whether this mapper will consider the <b>Blob</b> file.
     */
    private boolean loadFile;

    /**
     * Constructor.
     */
    public DocumentRecordRowMapper() {
        this(DEFAULT_LOAD_FILE);
    }

    /**
     * Constructor.
     */
    public DocumentRecordRowMapper(boolean loadFile) {
        this.loadFile = loadFile;
    }

    public boolean isLoadFile() {
        return this.loadFile;
    }
}
