USE skalicky_sv_navigatoru;

/**
 * Users and Authorities (= Roles)
 * Do not change username, password, enabled and authority since they are the parts
 * of the Spring Security's default database schema.
 *
 * The email is UNIQUE; hence it is necessary to use "null" as the default value, not
 * the empty string.
 * 
 * The default value of is_test_user is "false".
 */
CREATE TABLE users (
	username VARCHAR(50) PRIMARY KEY,
	password VARCHAR(50) NOT NULL,
	enabled BOOLEAN NOT NULL,
	first_name VARCHAR(20) NOT NULL,
	last_name VARCHAR(20) NOT NULL,
	email VARCHAR(100) UNIQUE,
	phone VARCHAR(20),
	is_test_user BOOLEAN NOT NULL DEFAULT 0,
	CONSTRAINT email_validator CHECK (email LIKE '_%@_%._%')
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE authorities (
	username VARCHAR(50) NOT NULL,
	authority VARCHAR(50) NOT NULL,
	PRIMARY KEY (username, authority),
	CONSTRAINT FK_authorities_users
		FOREIGN KEY (username)
		REFERENCES users(username)
		ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


	
/**
 * Sections which are edited via WYSIWYG editor.
 */
CREATE TABLE wysiwyg_sections (
	name VARCHAR(50) PRIMARY KEY,
	last_save_time DATETIME NOT NULL,
	source_code LONGTEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/**
 * Documents
 */
CREATE TABLE document_records (
	id INT(11) PRIMARY KEY AUTO_INCREMENT,
	file_name VARCHAR(100) NOT NULL,
	/* BLOB cannot be used since it allows to store at most 16KB. MEDIUMBLOB is up to 64MB. */
	file MEDIUMBLOB
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE session_records (
	id INT(11) PRIMARY KEY,
	type VARCHAR(50) NOT NULL,
	session_date DATETIME NOT NULL,
	discussed_topics TEXT NOT NULL,
	CONSTRAINT FK_session_records_document_records
		FOREIGN KEY (id)
		REFERENCES document_records(id)
		ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE other_document_records (
	id INT(11) PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	description TEXT,
	creation_time DATETIME NOT NULL,
	last_save_time DATETIME NOT NULL,
	CONSTRAINT FK_other_document_records_document_records
		FOREIGN KEY (id)
		REFERENCES document_records(id)
		ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE other_document_record_type_relations (
	record_id INT(11) NOT NULL,
	type VARCHAR(50) NOT NULL,
	PRIMARY KEY (record_id, type),
	CONSTRAINT FK_relations_other_document_records
		FOREIGN KEY (record_id)
		REFERENCES other_document_records(id)
		ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/**
 * Forum
 */
CREATE TABLE threads (
	id INT(11) PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	creation_time DATETIME not null,
	author_username VARCHAR(50),
	CONSTRAINT FK_threads_users
		FOREIGN KEY (author_username)
		REFERENCES users(username)
		ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE contributions (
	id INT(11) PRIMARY KEY AUTO_INCREMENT,
	thread_id INT(11) NOT NULL,
	text TEXT,
	creation_time DATETIME NOT NULL,
	last_save_time DATETIME NOT NULL,
	author_username VARCHAR(50),
	CONSTRAINT FK_contributions_threads
		FOREIGN KEY (thread_id)
		REFERENCES threads(id)
		ON DELETE CASCADE,
	CONSTRAINT FK_contributions_users
		FOREIGN KEY (author_username)
		REFERENCES users(username)
		ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/**
 * Events
 */
CREATE TABLE calendar_events (
	id INT(11) PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	date DATETIME NOT NULL,
	description TEXT,
	priority VARCHAR(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



/**
 * News
 */
CREATE TABLE news (
	id INT(11) PRIMARY KEY AUTO_INCREMENT,
	title VARCHAR(200) NOT NULL,
	text TEXT,
	creation_time DATETIME NOT NULL,
	last_save_time DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
