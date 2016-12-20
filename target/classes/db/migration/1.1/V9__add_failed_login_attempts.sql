ALTER TABLE users ADD COLUMN failed_logins INT default 0;
ALTER TABLE users ADD COLUMN last_failed_login DATETIME;
ALTER TABLE users ADD COLUMN account_locked BOOLEAN default 0;