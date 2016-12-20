ALTER TABLE reading_room_object_parts CHANGE COLUMN signatures signatures TINYINT(1) DEFAULT 0;

ALTER TABLE reading_room_content ADD COLUMN `item_location_count` INT(6);
ALTER TABLE reading_room_content ADD COLUMN `item_location_extent` VARCHAR(50);