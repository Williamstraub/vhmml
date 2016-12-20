drop table if exists reading_room_images;
drop table if exists reading_room_texts;
drop table if exists reading_room_object_parts;
drop table if exists reading_room_objects;
drop table if exists countries;

create table `reading_room_objects` (
	`id` int(11) NOT NULL DEFAULT 0,	
	`processed_by` varchar(255) DEFAULT NULL,
	`repository_id` int(11) DEFAULT NULL,
	`shelfmark_inventory_no` varchar(255) NOT NULL,
	`nick_name` varchar(255) DEFAULT NULL,
	`composite` tinyint(1) DEFAULT 0,
	`extent` varchar(255) NOT NULL,
	`physical_notes` TEXT,
	`binding` TEXT,
	`provenance` TEXT,
	`bibliography` TEXT,
	`notes` TEXT,
	`hmml_project_number` varchar(255) DEFAULT NULL,
	`inputter` varchar(255) DEFAULT NULL,
	`input_date` varchar(255) DEFAULT NULL,
	`reviser_date` varchar(255) DEFAULT NULL,
	`revisit` tinyint(1) DEFAULT 0,
	`reproduction_notes` varchar(500) DEFAULT NULL,
	`acknowledgments` varchar(255) DEFAULT NULL,
	`url3` TEXT,
	`url2` TEXT,
	`url` TEXT,
	`city_id` int(11) DEFAULT NULL,
	`country_id` int(11),
	`colophon` TEXT,
	`online_access_permission` varchar(255) DEFAULT NULL,
	`object_type` varchar(255) DEFAULT NULL,
	`facs_cap_date` varchar(255) DEFAULT NULL,
	`holding_institution` varchar(255) DEFAULT NULL,
	`city_authority_uri` varchar(1000) DEFAULT NULL,
	`access_restriction` varchar(255) DEFAULT NULL,
	`online` tinyint(1) DEFAULT 0,
	`country_authority_uri` varchar(1000) DEFAULT NULL,
	`country_iso3166` varchar(20) DEFAULT NULL,
	`collation` TEXT,
	`download` varchar(100) DEFAULT NULL,
	`uri` TEXT,
	`external_link_bib` TEXT,
	`external_link_facsimile` TEXT,
	`icon_name` varchar(255) DEFAULT NULL,				
	`cite_as` TEXT,
	`add_resources` TEXT,
	`associated_works` TEXT,
	`archival_collection_fond` varchar(500) DEFAULT NULL,
	`archival_series` TEXT,
	`archival_sub_series` varchar(500) DEFAULT NULL,
	`archival_language` varchar(255) DEFAULT NULL,
	`archival_scope_content` TEXT,
	`archival_rec_type` varchar(255) DEFAULT NULL,
	`archival_custodial_history` TEXT,
	`archival_container_binding` varchar(255) DEFAULT NULL,
	`archival_binding_notes` TEXT,
	`archival_dimensions` varchar(255) DEFAULT NULL,
	`archival_support` varchar(255) DEFAULT NULL,
	`archival_date_range_object` varchar(255) DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `reading_room_object_parts` (
	`id` int(11) NOT NULL DEFAULT 0,
	`reading_room_object_id` int(11) NOT NULL,	
	`part_number` varchar(4),
	`mss_part_folio` varchar(100),
	`item_dimensions` varchar(100),
	`country_of_origin_id` int(11),
	`cardinal_of_origin` varchar(100),
	`region_of_origin` varchar(100),
	`city_of_origin` varchar(100),
	`document` tinyint(1) DEFAULT 0,
	`dated_or_not` tinyint(1) DEFAULT 0,
	`date_century` varchar(255) NOT NULL,
	`date_precise` varchar(255),
	`layout` varchar(255),
	`script` varchar(255),
	`number_scribes` varchar(255),
	`scribe` varchar(255),
	`music` varchar(255),
	`support` varchar(255),
	`watermark` text,
	`representational_decoration` text,
	`other_decoration` text,
	`artist` varchar(255),
	`part_notes` text,
	`revisit` tinyint(1) DEFAULT 0,
	`begin_date` int,
	`end_date` int,
	`seq` int DEFAULT 0,
	`acknowledgments` varchar(255),
	`writing_system` varchar(255),
	`native_date_precise` varchar(255),
	`reviser` varchar(255),
	`catchwords` tinyint(1) DEFAULT 0,
	`foliation` varchar(255),
	`medium` varchar(255),
	`decoration` text,
	`colophon_part` text,
	`artist_uri` text,	
	`arc_folder` varchar(100),
	`arc_item` varchar(100),
	`arc_content` text,
	`arc_content_ns` text,
	`arc_incipit` text,
	`arc_incipit_ns` text,
	`arc_item_total_folios` varchar(255),
	`arc_item_span_of_folios` varchar(255),
	`arc_country_of_origin` varchar(255),
	`arc_city_of_origin` varchar(255),
	`arc_city_of_origin_uri` varchar(255),
	`arc_item_language` varchar(255),
	`arc_item_status_of_text` varchar(255),
	`arc_item_notes` text,
	`arc_scribe_notary` varchar(255),
	`arc_music` varchar(255),
	`arc_seal` varchar(255),
	`arc_bibliography` text,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `reading_room_content` (
	`id` int(11) NOT NULL DEFAULT 0,
	`reading_room_object_part_id` int(11) NOT NULL,	
	`folio_span` varchar(255),	
	`contributor` text,
	`title_in_ms` varchar(255),
	`devised_title` varchar(255),
	`devised_title_ns` varchar(255),
	`docket` varchar(255),
	`status_of_text` varchar(255),
	`rubric` varchar(255),
	`incipit_obsolete` varchar(255),
	`incipit` varchar(255),
	`explicit_obsolete` varchar(255),
	`explicit` varchar(255),
	`languages` varchar(255),
	`notes_to_text` text,
	`revisit` tinyint(1) DEFAULT 0,
	`seq` int DEFAULT 0,
	`acknowledgments` varchar(255),
	`music` varchar(255),
	`reviser` varchar(255),
	`colophon_text` text,
	`uniform_title` text,
	`uniform_title_ns` text,
	`uniform_title_uri` text,
	`text_bibliography` text,	
	`subject_keywords` varchar(255),
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `reading_room_images` (
	`id` int(11) NOT NULL DEFAULT 0,
	`reading_room_content_id` int(11) NOT NULL,	
	`img_id` int NOT NULL,
	`folio_number` varchar(25),
	`caption` text,
	`notes_to_photographer` varchar(50),
	`icon_class` varchar(100),
	`revisit` tinyint(1) DEFAULT 0,
	`seq` int DEFAULT 0,
	`url1` text,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `countries` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,	
	`name` varchar(255) NOT NULL,	
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `cities` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,	
	`name` varchar(255) NOT NULL,	
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `repositories` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,	
	`name` varchar(255) NOT NULL,	
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `reading_room_formats` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` varchar(255) NOT NULL,
PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `reading_room_object_formats` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,	
	`reading_room_object_id` int(11) NOT NULL,	
	`format_id` int(11) NOT NULL,	
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `reading_room_subjects` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` varchar(255) NOT NULL,
	`authority_url` text,
PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `reading_room_object_subjects` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,	
	`reading_room_object_id` int(11) NOT NULL,	
	`subject_id` int(11) NOT NULL,	
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `reading_room_genres` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` varchar(255) NOT NULL,
	`authority_url` text,
PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `reading_room_object_genres` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,	
	`reading_room_object_id` int(11) NOT NULL,	
	`genre_id` int(11) NOT NULL,	
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `reading_room_languages` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` varchar(255) NOT NULL,
PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `reading_room_content_languages` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,	
	`reading_room_content_id` int(11) NOT NULL,	
	`language_id` int(11) NOT NULL,	
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `reading_room_contributors` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`name` varchar(255),
	`display_name` varchar(255),
	`name_ns` varchar(255),
	`uri` text,	
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `reading_room_content_contributors` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,	
	`reading_room_content_id` int(11) NOT NULL,	
	`reading_room_contributor_id` int(11) NOT NULL,
	`contributor_type` varchar(25),
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create view reading_room_authors as
select 
	c.id, 
	c.name, 
	c.display_name, 
	c.name_ns, 
	c.uri,
	cc.reading_room_content_id
from 
	reading_room_contributors c left join
	reading_room_content_contributors cc on c.id = cc.reading_room_contributor_id
where 
	cc.contributor_type = 'AUTHOR';
	
create view reading_room_other_contributors	as
select 
	c.id, 
	c.name, 
	c.display_name, 
    c.name_ns, 
    c.uri,
    cc.reading_room_content_id
from 
	reading_room_contributors c left join
	reading_room_content_contributors cc on c.id = cc.reading_room_contributor_id
where 
	cc.contributor_type = 'OTHER';

create view reading_room_overview as
select 
	o.id,
	o.hmml_project_number,
	o.inputter,
	o.input_date,
	o.shelfmark_inventory_no,
	o.object_type,
	o.extent,
	o.provenance,
    o.binding,
    o.processed_by,
    o.access_restriction,
	group_concat(distinct f.name order by f.name separator '; ') as format, 
	co.name as country, 
	ci.name as city, 
	r.name as repository, 	
	group_concat(DISTINCT p.date_century ORDER BY p.date_century DESC SEPARATOR '; ') as centuries,	
	group_concat(DISTINCT p.support ORDER BY p.support SEPARATOR '; ') as support,	
    group_concat(DISTINCT p.item_dimensions SEPARATOR '; ') as dimensions,
    group_concat(DISTINCT l.name SEPARATOR '; ') as languages,
    group_concat(DISTINCT author_list.name SEPARATOR '; ') as authors,
    group_concat(DISTINCT author_list.name_ns SEPARATOR '; ') as authors_ns,
    group_concat(DISTINCT contributor_list.name SEPARATOR '; ') as contributors,
    group_concat(DISTINCT s.name SEPARATOR '; ') as subjects,
    group_concat(DISTINCT g.name SEPARATOR '; ') as genres
from reading_room_objects o left outer join
	countries co on o.country_id = co.id left outer join
	cities ci on o.city_id = ci.id left outer join
	repositories r on o.repository_id = r.id left outer join
	reading_room_object_parts p on o.id = p.reading_room_object_id left join
	reading_room_object_formats of on o.id = of.reading_room_object_id left outer join
	reading_room_formats f on of.format_id = f.id left outer join
    reading_room_content cnt on p.id = cnt.reading_room_object_part_id left join
    reading_room_content_languages cl on cnt.id = cl.reading_room_content_id left outer join
    reading_room_languages l on cl.language_id = l.id left outer join
	reading_room_authors author_list on cnt.id = author_list.reading_room_content_id left outer join
    reading_room_other_contributors contributor_list on cnt.id = contributor_list.reading_room_content_id left outer join
    reading_room_object_subjects os on o.id = os.reading_room_object_id left outer join
    reading_room_subjects s on os.subject_id = s.id left outer join
    reading_room_object_genres og on o.id = og.reading_room_object_id left outer join
    reading_room_genres g on og.genre_id = g.id
group by o.id,
	o.hmml_project_number,
	o.inputter,
	o.input_date,
	o.shelfmark_inventory_no,
	o.object_type,
	o.extent,
	o.provenance,
    o.binding,
    o.processed_by,
    o.access_restriction;

CREATE INDEX reading_room_objects_idx1 ON reading_room_objects(country_id);
CREATE INDEX reading_room_objects_idx2 ON reading_room_objects(hmml_project_number);
CREATE INDEX reading_room_parts_idx1 ON reading_room_object_parts(reading_room_object_id);
CREATE INDEX reading_room_context_idx1 ON reading_room_content(reading_room_object_part_id);
CREATE INDEX reading_room_images_idx1 ON reading_room_images(reading_room_content_id);
CREATE INDEX reading_room_object_formats_idx1 ON reading_room_object_formats(reading_room_object_id);
CREATE INDEX reading_room_object_formats_idx2 ON reading_room_object_formats(format_id);
CREATE INDEX reading_room_object_subjects_idx1 ON reading_room_object_subjects(reading_room_object_id);
CREATE INDEX reading_room_object_subjects_idx2 ON reading_room_object_subjects(subject_id);
CREATE INDEX reading_room_object_genres_idx1 ON reading_room_object_genres(reading_room_object_id);
CREATE INDEX reading_room_object_genres_idx2 ON reading_room_object_genres(genre_id);
CREATE INDEX reading_room_content_languages_idx1 ON reading_room_content_languages(reading_room_content_id);
CREATE INDEX reading_room_content_languages_idx2 ON reading_room_content_languages(language_id);
CREATE INDEX reading_room_content_contributors_idx1 ON reading_room_content_contributors(reading_room_content_id);
CREATE INDEX reading_room_content_contributors_idx2 ON reading_room_content_contributors(reading_room_contributor_id);
	
-- Formats are in the database because they have a many-to-many relationship with reading_room_objects that we 
-- need to persist, as opposed to something like support which is just a one-to-one with reading_room_content.
-- In those cases it's simpler (and more efficient) to just keep the valid values in an enum in Java and
-- store the selected value in a column on the reading_room_content table.
insert into reading_room_formats values (null, 'Digital');
insert into reading_room_formats values (null, 'Microform');
insert into reading_room_formats values (null, 'Scanned Microform');
insert into reading_room_formats values (null, 'Photo print');

insert into reading_room_genres values (null, 'Codex/Volume', null);
insert into reading_room_genres values (null, 'Scroll', null);
insert into reading_room_genres values (null, 'Sheet/Parchment', null);
insert into reading_room_genres values (null, 'Wax Tablet', null);
insert into reading_room_genres values (null, 'Fragment', null);
insert into reading_room_genres values (null, 'Other', null);

insert into reading_room_subjects values (null, 'Malta--History--Siege, 1565', null);
insert into reading_room_subjects values (null, 'Ethiopia-History-16th century', null);

/* ADD INDEXES AND ADD SCRIPT TO ALTER KEYS TO AUTOINCREMENT AFTER IMPORTING THE DATA */

