ALTER TABLE reading_room_objects ADD COLUMN medium VARCHAR(255);
ALTER TABLE reading_room_objects ADD COLUMN summary TEXT;
ALTER TABLE reading_room_objects ADD COLUMN decoration_illustration VARCHAR(255);
ALTER TABLE reading_room_objects ADD COLUMN country_uncertain TINYINT(1) DEFAULT 0;
ALTER TABLE reading_room_objects ADD COLUMN city_uncertain TINYINT(1) DEFAULT 0;

ALTER TABLE reading_room_object_parts ADD COLUMN country_of_origin_uncertain TINYINT(1) DEFAULT 0;
ALTER TABLE reading_room_object_parts ADD COLUMN city_of_origin_uncertain TINYINT(1) DEFAULT 0;

ALTER TABLE reading_room_content CHANGE COLUMN incipit incipit TEXT;
ALTER TABLE reading_room_content CHANGE COLUMN explicit explicit TEXT;

 