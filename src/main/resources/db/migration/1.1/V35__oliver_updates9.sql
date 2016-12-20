ALTER TABLE reading_room_objects ADD COLUMN active TINYINT(1) DEFAULT 0;

ALTER TABLE reading_room_content CHANGE COLUMN devised_title provisional_title VARCHAR(255);
ALTER TABLE reading_room_content CHANGE COLUMN devised_title_ns title_ns VARCHAR(255);

ALTER TABLE reading_room_object_parts CHANGE COLUMN support_dimensions support_dimensions_imported VARCHAR(100);
ALTER TABLE reading_room_object_parts ADD COLUMN support_imported VARCHAR(255);
ALTER TABLE reading_room_object_parts ADD COLUMN century_imported VARCHAR(255);
ALTER TABLE reading_room_object_parts ADD COLUMN ymd_date_imported VARCHAR(255);
ALTER TABLE reading_room_object_parts ADD COLUMN writing_system_imported VARCHAR(255);

ALTER TABLE reading_room_content DROP COLUMN uniform_title;
ALTER TABLE reading_room_content DROP COLUMN uniform_title_ns;
ALTER TABLE reading_room_content DROP COLUMN uniform_title_uri;
ALTER TABLE reading_room_content DROP COLUMN incipit_obsolete;
ALTER TABLE reading_room_content DROP COLUMN explicit_obsolete;

ALTER TABLE reading_room_content ADD COLUMN uniform_title_id INT(11);

create table `reading_room_uniform_titles` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` varchar(255),
	`name_ns` varchar(255),
	`authority_uri_lc` varchar(255),
	`authority_uri_viaf` text,	
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


create table `reading_room_content_uniform_titles` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,	
	`content_id` int(11) NOT NULL,	
	`title_id` int(11) NOT NULL,
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `reading_room_alternate_titles` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,	
	`content_id` int(11) NOT NULL,	
	`title` varchar(255) NOT NULL,
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;