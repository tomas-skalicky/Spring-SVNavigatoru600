package com.svnavigatoru600.repository.records.impl.direct;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.svnavigatoru600.domain.records.DocumentRecord;
import com.svnavigatoru600.repository.users.impl.direct.UserRowMapper;


/**
 * For more information, see {@link UserRowMapper}. This class cannot implement
 * the interface {@link RowMapper} since the class {@link DocumentRecord} is not
 * instantiable - it is abstract.
 * 
 * @author Tomas Skalicky
 */
public abstract class DocumentRecordRowMapper {

	/**
	 * Determines whether this mapper will consider the <b>Blob</b> file.
	 */
	protected boolean loadFile;

	private static final Map<String, String> PROPERTY_COLUMN_MAP;

	/**
	 * Static constructor.
	 */
	static {
		PROPERTY_COLUMN_MAP = new HashMap<String, String>();
		PROPERTY_COLUMN_MAP.put("id", "id");
		PROPERTY_COLUMN_MAP.put("fileName", "file_name");
		PROPERTY_COLUMN_MAP.put("file", "file");
	}

	/**
	 * Constructor.
	 */
	public DocumentRecordRowMapper() {
		this(true);
	}

	/**
	 * Constructor.
	 */
	public DocumentRecordRowMapper(boolean loadFile) {
		this.loadFile = loadFile;
	}

	public static String getColumn(String propertyName) {
		return DocumentRecordRowMapper.PROPERTY_COLUMN_MAP.get(propertyName);
	}
}
