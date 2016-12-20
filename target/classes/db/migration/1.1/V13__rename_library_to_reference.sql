RENAME TABLE library_creators TO reference_creators;
ALTER TABLE reference_creators CHANGE COLUMN library_entry_id reference_entry_id INT(11) NOT NULL;

RENAME TABLE library_entries TO reference_entries;

DELETE FROM roles WHERE name = 'ROLE_LIBRARY_CREATOR';

INSERT INTO roles values(null, 'ROLE_REFERENCE_CREATOR', 'Reference Creator');

drop view if exists authors;

create view authors as 
	select 			
		re.id,		
		coalesce(concat(c.last_name, ', ', c.first_name), c.name) author
	from 
		reference_entries re left join 
		reference_creators rc on re.id = rc.reference_entry_id left join
		creators c on rc.creator_id = c.id
	where rc.creator_type = 'author';

drop view if exists library_list;
drop view if exists reference_list;

create view reference_list as
	select
		re.id,
		re.title,
		re.short_title,
		coalesce(nullif(re.short_title, ''), re.title) display_title,		
		re.book_title,
		re.series_title,
		re.encyclopedia_title,
		GROUP_CONCAT(author order by author SEPARATOR '; ') author,
		re.publication_title,
		re.item_type,
		re.place,
		re.publisher,
		re.date,
		re.series,
		re.url
	from
		reference_entries re left outer join
		authors a on (a.id = re.id)
	group by
		re.id, 
		re.title,
		re.short_title,
		re.book_title, 
		re.series_title,
		re.encyclopedia_title,
		re.date, 
		re.publication_title, 
		re.item_type,
		re.place, 
		re.publisher, 
		re.date, 
		re.series, 
		re.url;	
