alter table users drop index username;
alter table users ADD UNIQUE unique_index(username, removed);