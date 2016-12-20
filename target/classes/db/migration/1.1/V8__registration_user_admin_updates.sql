DELETE FROM user_roles WHERE role_id IN(SELECT id FROM roles WHERE name IN('ROLE_USER', 'ROLE_SCHOOL_USER'));

DELETE FROM roles WHERE name IN('ROLE_USER', 'ROLE_SCHOOL_USER');

UPDATE roles SET description = 'System Admin' WHERE name = 'ROLE_ADMIN';

ALTER TABLE users ADD COLUMN address VARCHAR(255);
ALTER TABLE users ADD COLUMN phone VARCHAR(20);
ALTER TABLE users ADD COLUMN research_interests TEXT;
ALTER TABLE users ADD COLUMN region_lang_interests TEXT;
ALTER TABLE users ADD COLUMN credentials_expired BOOLEAN DEFAULT 1;
ALTER TABLE users MODIFY password VARCHAR(255) NULL;