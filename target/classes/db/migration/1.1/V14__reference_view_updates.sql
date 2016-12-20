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
		re.proceedings_title,
		re.item_type,
		re.place,
		re.publisher,
		re.date,
		re.series,
		re.edition,
		re.volume,
		re.version,
		re.pages,
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
		re.proceedings_title,
		re.item_type,
		re.place, 
		re.publisher, 
		re.date, 
		re.series, 
		re.edition,
		re.volume,
		re.version,
		re.pages,
		re.url;	