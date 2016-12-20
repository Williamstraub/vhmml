ALTER TABLE reading_room_objects CHANGE COLUMN shelfmark_inventory_no shelf_mark VARCHAR(255);
ALTER TABLE reading_room_objects CHANGE COLUMN external_link_facsimile external_facsimile_url TEXT;
ALTER TABLE reading_room_objects CHANGE COLUMN download download_option VARCHAR(100);
ALTER TABLE reading_room_objects CHANGE COLUMN online viewable_online TINYINT(1) DEFAULT 0;

ALTER TABLE reading_room_objects DROP COLUMN binding_dimensions;
ALTER TABLE reading_room_objects ADD COLUMN binding_width INT(3);
ALTER TABLE reading_room_objects ADD COLUMN binding_height INT(3);
ALTER TABLE reading_room_objects ADD COLUMN binding_depth INT(3);
ALTER TABLE reading_room_objects ADD COLUMN condition_notes TEXT;
ALTER TABLE reading_room_objects ADD COLUMN last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;