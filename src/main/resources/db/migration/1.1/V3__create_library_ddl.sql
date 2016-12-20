CREATE TABLE library_entries (
    `id` INT(11) NOT NULL AUTO_INCREMENT,    
    `blog_title` VARCHAR(255),
	`book_title` VARCHAR(255),
	`conference_name` VARCHAR(255),
	`date` VARCHAR(50),
	`dictionary_title` VARCHAR(255),
	`director` VARCHAR(50),
	`edition` VARCHAR(100), 
	`encyclopedia_title` VARCHAR(255),
	`episode_number` VARCHAR(10),
	`institution` VARCHAR(100),
	`issue` VARCHAR(50),
	`item_type` VARCHAR(20),	
	`manuscript_type` VARCHAR(20),
	`pages` VARCHAR(50),
	`place` VARCHAR(255),
	`proceedings_title` VARCHAR(255),
	`publication_title` VARCHAR(100),
	`publisher` VARCHAR(255),
	`series` VARCHAR(255),
	`series_number` VARCHAR(50),
	`series_title` VARCHAR(255),
	`short_title` VARCHAR(100),
	`studio` VARCHAR(50),
	`title` TEXT,
	`type` VARCHAR(50),
	`university` VARCHAR(50),
	`url` VARCHAR(255),
	`version` VARCHAR(20),
	`volume` VARCHAR(100),
	`zotero_group_id` INT(11),
	`zotero_collection_key` VARCHAR(10),
	`zotero_key` VARCHAR(20),
	 PRIMARY KEY(`id`) 
	 -- example of how to add full text index if we upgrade MySQL or move to MariaDB
	 -- FULLTEXT(`title`), FULLTEXT(`short_title`), FULLTEXT(`book_title`), FULLTEXT(`encyclopedia_title`), FULLTEXT(`dictionary_title`)
) ENGINE=InnoDb AUTO_INCREMENT=11235 DEFAULT CHARSET=utf8;

CREATE TABLE library_creators (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `library_entry_id` INT(11) not null,
	`creator_id` INT(11) not null,
	`creator_type` VARCHAR(20),
     PRIMARY KEY(`id`)
) ENGINE=InnoDb AUTO_INCREMENT=11235 DEFAULT CHARSET=utf8;

CREATE INDEX library_creators_idx1 ON library_creators(library_entry_id);

CREATE INDEX library_creators_idx2 ON library_creators(creator_id);

CREATE TABLE creators (
    `id` INT(11) NOT NULL AUTO_INCREMENT,    
    `name` VARCHAR(100),
    `first_name` VARCHAR(100),
	`last_name` VARCHAR(100),
     PRIMARY KEY(`id`)
) ENGINE=InnoDb AUTO_INCREMENT=11235 DEFAULT CHARSET=utf8;

-- create views to simplify search
-- we have 2 views because we can't have the authors subquery in the from clause of the lib items view @see http://stackoverflow.com/questions/8428641/views-select-contains-a-subquery-in-the-from-clause

create view authors as 
	select 			
		le.id,		
		coalesce(concat(c.last_name, ', ', c.first_name), c.name) author
	from 
		library_entries le left join 
		library_creators lc on le.id = lc.library_entry_id left join
		creators c on lc.creator_id = c.id;
		
create view library_list as
	select
		le.id, 
		le.title,
		le.short_title,
		le.book_title,
		le.series_title,
		le.encyclopedia_title,
		GROUP_CONCAT(author order by author SEPARATOR '; ') author,
		le.publication_title,
		le.item_type,
		le.place,
		le.publisher,
		le.date,
		le.series,
		le.url
	from
		library_entries le inner join
		authors a on (a.id = le.id)
	group by
		le.id, 
		le.title, 
		le.short_title, 
		le.book_title, 
		le.series_title, 
		le.date, 
		le.publication_title, 
		le.item_type,
		le.place, 
		le.publisher, 
		le.date, 
		le.series, 
		le.url;