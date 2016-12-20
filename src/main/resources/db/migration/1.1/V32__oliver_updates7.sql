ALTER TABLE reading_room_object_parts ADD COLUMN `folio_imported` VARCHAR(150);
ALTER TABLE reading_room_object_parts ADD COLUMN `part_location_count` INT(6);
ALTER TABLE reading_room_object_parts ADD COLUMN `part_location_extent` VARCHAR(50);

ALTER TABLE reading_room_object_parts DROP COLUMN `part_location`;

ALTER TABLE reading_room_object_parts CHANGE COLUMN item_dimensions support_dimensions VARCHAR(100);

ALTER TABLE reading_room_object_parts ADD COLUMN support_width INT(3);
ALTER TABLE reading_room_object_parts ADD COLUMN support_height INT(3);
ALTER TABLE reading_room_object_parts ADD COLUMN support_depth INT(3);
ALTER TABLE reading_room_object_parts ADD COLUMN writing_space_width INT(3);
ALTER TABLE reading_room_object_parts ADD COLUMN writing_space_height INT(3);
ALTER TABLE reading_room_object_parts ADD COLUMN signatures TINYINT(1);

ALTER TABLE reading_room_content DROP COLUMN seq;

create table `reading_room_part_contributors` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,	
	`part_id` int(11) NOT NULL,	
	`contributor_id` int(11) NOT NULL,
	`contributor_type` varchar(25),
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;