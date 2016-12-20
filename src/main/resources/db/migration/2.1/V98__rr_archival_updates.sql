alter table reading_room_objects drop column archival_collection_fond;
alter table reading_room_objects drop column archival_series;
alter table reading_room_objects drop column archival_sub_series;
alter table reading_room_objects drop column archival_language;
alter table reading_room_objects drop column archival_scope_content;
alter table reading_room_objects drop column archival_rec_type;
alter table reading_room_objects drop column archival_custodial_history;
alter table reading_room_objects drop column archival_container_binding;
alter table reading_room_objects drop column archival_binding_notes;
alter table reading_room_objects drop column archival_dimensions;
alter table reading_room_objects drop column archival_support;
alter table reading_room_objects drop column archival_date_range_object;

alter table reading_room_objects add column archival_data_id int(11);

alter table reading_room_object_parts drop column arc_folder;
alter table reading_room_object_parts drop column arc_item;
alter table reading_room_object_parts drop column arc_content;
alter table reading_room_object_parts drop column arc_content_ns;
alter table reading_room_object_parts drop column arc_incipit;
alter table reading_room_object_parts drop column arc_incipit_ns;
alter table reading_room_object_parts drop column arc_item_total_folios;
alter table reading_room_object_parts drop column arc_item_span_of_folios;
alter table reading_room_object_parts drop column arc_country_of_origin;
alter table reading_room_object_parts drop column arc_city_of_origin;
alter table reading_room_object_parts drop column arc_city_of_origin_uri;
alter table reading_room_object_parts drop column arc_item_language;
alter table reading_room_object_parts drop column arc_item_status_of_text;
alter table reading_room_object_parts drop column arc_item_notes;
alter table reading_room_object_parts drop column arc_scribe_notary;
alter table reading_room_object_parts drop column arc_music;
alter table reading_room_object_parts drop column arc_seal;
alter table reading_room_object_parts drop column arc_bibliography;

-- content for archival items don't belong to a part so part id has to be nullable
alter table reading_room_content change column reading_room_object_part_id reading_room_object_part_id int(11) null;

CREATE TABLE `reading_room_archival_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reading_room_object_id` int(11) NOT NULL,  
  `collection_fond` varchar(500) DEFAULT NULL,
  `series` text,
  `sub_series` varchar(500) DEFAULT NULL,
  `container_type` varchar(50) DEFAULT NULL,
  `container_count` int(4),
  `language` varchar(255) DEFAULT NULL,
  `creator` varchar(100) DEFAULT NULL,
  `scope_content` text,
  `historical_note` text,
  `rec_type` varchar(255) DEFAULT NULL,
  `custodial_history` text,  
  `dimensions` varchar(255) DEFAULT NULL,
  `support` varchar(255) DEFAULT NULL,
  `date_range` varchar(255) DEFAULT NULL, 
  PRIMARY KEY (`id`),
  KEY `reading_room_archival_data_idx1` (`reading_room_object_id`)
) ENGINE=InnoDB AUTO_INCREMENT=138078 DEFAULT CHARSET=utf8;

CREATE TABLE `reading_room_archival_content` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `archival_data_id` int(11) NOT NULL,  
  `folder` varchar(100) DEFAULT NULL,
  `item` varchar(100) DEFAULT NULL,
  `content` text,
  `content_ns` text,
  `incipit` text,
  `incipit_ns` text,
  `total_folios` varchar(255) DEFAULT NULL,
  `span_of_folios` varchar(255) DEFAULT NULL,
  `country_of_origin` varchar(255) DEFAULT NULL,
  `city_of_origin` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `status_of_text` varchar(255) DEFAULT NULL,
  `scribe_notary` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `archival_content_idx1` (`archival_data_id`),
  CONSTRAINT `archival_content_fk1` FOREIGN KEY (`id`) REFERENCES `reading_room_content` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=142436 DEFAULT CHARSET=utf8;

CREATE TABLE `reading_room_archival_containers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)  
) ENGINE=InnoDB AUTO_INCREMENT=142436 DEFAULT CHARSET=utf8;

CREATE TABLE `reading_room_archival_contributors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `archival_data_id` int(11) NOT NULL,
  `contributor_id` int(11) NOT NULL,
  `contributor_type` varchar(25) DEFAULT NULL,
  `name_ns` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `reading_room_part_archival_idx1` (`archival_data_id`),
  KEY `reading_room_part_archival_idx2` (`contributor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=223 DEFAULT CHARSET=utf8;

