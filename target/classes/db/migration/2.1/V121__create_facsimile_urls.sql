create table reading_room_facsimile_urls (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`reading_room_object_id` INT(11),
	`url` VARCHAR(255),
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into reading_room_facsimile_urls (id, reading_room_object_id, url) (select null, id, external_facsimile_url from reading_room_objects where external_facsimile_url is not null);

alter table reading_room_objects drop column external_facsimile_url;