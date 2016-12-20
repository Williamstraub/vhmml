drop view library_list;

create view library_list as
	select
		le.id,
		le.title,
		le.short_title,
		coalesce(nullif(le.short_title, ''), le.title) display_title,		
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
		le.encyclopedia_title,
		le.date, 
		le.publication_title, 
		le.item_type,
		le.place, 
		le.publisher, 
		le.date, 
		le.series, 
		le.url;	