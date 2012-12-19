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
	is_test_user BOOLEAN NOT NULL DEFAULT 0
);

CREATE TABLE authorities (
	username VARCHAR(50) NOT NULL,
	authority VARCHAR(50) NOT NULL,
	PRIMARY KEY (username, authority),
	CONSTRAINT FK_authorities_users
		FOREIGN KEY (username)
		REFERENCES users(username)
		ON DELETE CASCADE
);


	
/**
 * Sections which are edited via WYSIWYG editor.
 */
CREATE TABLE wysiwyg_sections (
	name VARCHAR(50) PRIMARY KEY,
	last_save_time DATETIME NOT NULL,
	source_code LONGTEXT
);



/**
 * Documents
 */
CREATE TABLE document_records (
	id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	file_name VARCHAR(100) NOT NULL,
	/* BLOB cannot be used since it allows to store at most 16KB. MEDIUMBLOB is up to 64MB. */
	file MEDIUMBLOB
);

CREATE TABLE session_records (
	id INTEGER PRIMARY KEY,
	type VARCHAR(50) NOT NULL,
	session_date DATETIME NOT NULL,
	discussed_topics TEXT NOT NULL,
	CONSTRAINT FK_session_records_document_records
		FOREIGN KEY (id)
		REFERENCES document_records(id)
		ON DELETE CASCADE
);

CREATE TABLE other_document_records (
	id INTEGER PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	description TEXT,
	creation_time DATETIME NOT NULL,
	last_save_time DATETIME NOT NULL,
	CONSTRAINT FK_other_document_records_document_records
		FOREIGN KEY (id)
		REFERENCES document_records(id)
		ON DELETE CASCADE
);

CREATE TABLE other_document_record_type_relations (
	record_id INTEGER NOT NULL,
	type VARCHAR(50) NOT NULL,
	PRIMARY KEY (record_id, type),
	CONSTRAINT FK_relations_other_document_records
		FOREIGN KEY (record_id)
		REFERENCES other_document_records(id)
		ON DELETE CASCADE
);



/**
 * Forum
 */
CREATE TABLE threads (
	id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	creation_time DATETIME not null,
	author_username VARCHAR(50),
	CONSTRAINT FK_threads_users
		FOREIGN KEY (author_username)
		REFERENCES users(username)
		ON DELETE SET NULL
);

CREATE TABLE contributions (
	id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	thread_id INTEGER NOT NULL,
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
);



/**
 * Events
 */
CREATE TABLE calendar_events (
	id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	date DATETIME NOT NULL,
	description TEXT,
	priority VARCHAR(10) NOT NULL
);



/**
 * News
 */
CREATE TABLE news (
	id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1, INCREMENT BY 1) PRIMARY KEY,
	title VARCHAR(200) NOT NULL,
	text TEXT,
	creation_time DATETIME NOT NULL,
	last_save_time DATETIME NOT NULL
);