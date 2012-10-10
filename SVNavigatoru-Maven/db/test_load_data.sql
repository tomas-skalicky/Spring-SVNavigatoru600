USE skalicky_sv_navigatoru;

-- tom, tomspassword
INSERT INTO users (username, password, enabled, first_name, last_name, email, phone)
	VALUES ('tom', 'e1d06df3f843c11e857fa844a5dd00f45808bd1b', true, 'Tomáš', 'Skalický', 'tomsky@seznam.cz', '608 254 465');
INSERT INTO authorities (username, authority) VALUES ('tom', 'ROLE_REGISTERED_USER');
INSERT INTO authorities (username, authority) VALUES ('tom', 'ROLE_MEMBER_OF_SV');
INSERT INTO authorities (username, authority) VALUES ('tom', 'ROLE_USER_ADMINISTRATOR');

-- success, s
INSERT INTO users (username, password, enabled, first_name, last_name, email, phone)
	VALUES ('success', 'a0f1490a20d0211c997b44bc357e1972deab8ae3', true, 'Mr.', 'Success', null, null);
INSERT INTO authorities (username, authority) VALUES ('success', 'ROLE_REGISTERED_USER');
INSERT INTO authorities (username, authority) VALUES ('success', 'ROLE_MEMBER_OF_SV');
INSERT INTO authorities (username, authority) VALUES ('success', 'ROLE_MEMBER_OF_BOARD');

-- tomas, t
INSERT INTO users (username, password, enabled, first_name, last_name, email, phone)
	VALUES ('tomas', '8efd86fb78a56a5145ed7739dcb00c78581c5375', true, 'Tomáš', 'Skalický', 'skalicky.tomas@gmail.com', null);
INSERT INTO authorities (username, authority) VALUES ('tomas', 'ROLE_REGISTERED_USER');
INSERT INTO authorities (username, authority) VALUES ('tomas', 'ROLE_MEMBER_OF_SV');
INSERT INTO authorities (username, authority) VALUES ('tomas', 'ROLE_MEMBER_OF_BOARD');
INSERT INTO authorities (username, authority) VALUES ('tomas', 'ROLE_USER_ADMINISTRATOR');

-- sections
INSERT INTO wysiwyg_sections (name, last_save_time, source_code) VALUES ('BOARD', '1900-01-01 00:00:00', '<p><span style="background-color: #ffff00;">Členov&eacute; <a href="http://www.liboc.euweb.cz"><span style="background-color: #ffff00;">v&yacute;boru</span></a><img title="Je na prachy" src="/SVNavigatoru/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-money-mouth.gif" alt="Je na prachy" border="0" /></span></p>');
INSERT INTO wysiwyg_sections (name, last_save_time, source_code) VALUES ('REMOSTAV_CONTACT', '1900-01-01 00:00:00', '<p><span style="background-color: #ffff00;">Remostav - <a href="http://www.liboc.euweb.cz"><span style="background-color: #ffff00;">kontakty</span></a><img title="Je na prachy" src="/SVNavigatoru/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-money-mouth.gif" alt="Je na prachy" border="0" /></span></p>');
INSERT INTO wysiwyg_sections (name, last_save_time, source_code) VALUES ('USEFUL_LINKS', '1900-01-01 00:00:00', '<p><span style="background-color: #ffff00;">Užitečné <a href="http://www.liboc.euweb.cz"><span style="background-color: #ffff00;">odkazy</span></a><img title="Je na prachy" src="/SVNavigatoru/tinymce/jscripts/tiny_mce/plugins/emotions/img/smiley-money-mouth.gif" alt="Je na prachy" border="0" /></span></p>');
