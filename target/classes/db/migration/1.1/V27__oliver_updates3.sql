ALTER TABLE reading_room_objects ADD COLUMN binding_notes TEXT;

create table reading_room_object_contributors (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`reading_room_object_id` INT(11),
	`contributor_id` INT(11),
	`type` VARCHAR(50),
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
