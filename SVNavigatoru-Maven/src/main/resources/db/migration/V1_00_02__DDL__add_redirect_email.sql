USE skalicky_sv_navigatoru;

ALTER TABLE users ADD COLUMN redirect_email VARCHAR(100) UNIQUE;
ALTER TABLE users ADD CONSTRAINT redirect_email_validator CHECK (email LIKE '_%@_%._%');
