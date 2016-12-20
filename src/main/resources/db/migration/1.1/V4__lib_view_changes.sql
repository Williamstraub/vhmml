drop view authors;
drop view library_list;

create view authors as 
	select 			
		le.id,		
		coalesce(concat(c.last_name, ', ', c.first_name), c.name) author
	from 
		library_entries le left join 
		library_creators lc on le.id = lc.library_entry_id left join
		creators c on lc.creator_id = c.id
	where lc.creator_type = 'author';
	
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