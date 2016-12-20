ALTER TABLE reading_room_objects DROP COLUMN country_uncertain;
ALTER TABLE reading_room_objects DROP COLUMN city_uncertain;

ALTER TABLE reading_room_objects ADD COLUMN last_updated_by VARCHAR(255);

ALTER TABLE reading_room_object_parts MODIFY date_century VARCHAR(255);
ALTER TABLE reading_room_object_parts CHANGE COLUMN mss_part_folio part_location VARCHAR(100);

ALTER TABLE reading_room_content ADD COLUMN item_number INT(11);