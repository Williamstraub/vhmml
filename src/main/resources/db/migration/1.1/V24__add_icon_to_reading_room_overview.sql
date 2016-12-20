drop view if exists reading_room_overview;

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
    o.icon_name,
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
    o.access_restriction,
    o.icon_name;