ALTER TABLE reading_room_imports drop COLUMN success;
ALTER TABLE reading_room_imports add COLUMN status VARCHAR(20);
ALTER TABLE reading_room_imports add COLUMN error_message VARCHAR(255);