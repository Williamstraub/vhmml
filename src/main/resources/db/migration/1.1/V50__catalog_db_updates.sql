create or replace view reading_room_overview as
select 
	o.id,
	o.hmml_project_number,
	o.inputter,
	o.input_date,
	o.shelf_mark,
	o.object_type,	
	o.provenance,
    o.binding,
    o.processed_by,
    o.access_restriction,
    o.icon_name,
    o.support,
    o.active,
    (case when o.download_option is not null and o.download_option <> 'NO' 
    		THEN true
    	else
    		false
	end) as downloadable,
    concat(o.binding_height, ' x ', o.binding_width, ' x ', o.binding_depth) as dimensions,    
    group_concat(distinct concat(oe.extent_count, ' ', oe.display_name) separator '; ') as extent,	
    group_concat(distinct cnt.provisional_title order by cnt.provisional_title separator '; ') as titles, 
    group_concat(distinct alt_title.title order by alt_title.title separator '; ') as alt_titles, 
    group_concat(distinct u_title.name order by u_title.name separator '; ') as uniform_titles,
    f.name as format,
	co.name as country, 
	ci.name as city, 
	r.name as repository, 	
	group_concat(DISTINCT pc.century SEPARATOR ' ') as centuries, 
	max(pc.century) as last_century, 
	min(pc.century) as first_century,	
	max(p.century_uncertain) as century_uncertain,	
	min(p.begin_date) as begin_date,
    max(p.end_date) as end_date,
    group_concat(DISTINCT l.name SEPARATOR '; ') as languages,
    group_concat(DISTINCT contrib.name SEPARATOR '; ') as authors,
    group_concat(DISTINCT contrib2.name SEPARATOR '; ') as other_contributors,
    group_concat(DISTINCT s.name SEPARATOR '; ') as subjects,
    group_concat(DISTINCT g.name SEPARATOR '; ') as genres    
from reading_room_objects o left outer join
	reading_room_countries co on o.country_id = co.id left outer join
	reading_room_cities ci on o.city_id = ci.id left outer join
	reading_room_repositories r on o.repository_id = r.id left outer join	
	reading_room_object_parts p on o.id = p.reading_room_object_id left join
   	reading_room_formats f on o.surrogate_format_id = f.id left outer join		
    reading_room_content cnt on p.id = cnt.reading_room_object_part_id left join
    reading_room_content_languages cl on cnt.id = cl.reading_room_content_id left outer join
    reading_room_languages l on cl.language_id = l.id left outer join    
    reading_room_content_authors authors on cnt.id = authors.reading_room_content_id left outer join
	reading_room_contributors contrib on authors.reading_room_contributor_id = contrib.id left outer join        
    reading_room_content_other_contributors other_contrib on cnt.id = other_contrib.reading_room_content_id left outer join    
    reading_room_contributors contrib2 on other_contrib.reading_room_contributor_id = contrib2.id left outer join    
    reading_room_object_subjects os on o.id = os.reading_room_object_id left outer join
    reading_room_subjects s on os.subject_id = s.id left outer join
    reading_room_object_genres og on o.id = og.reading_room_object_id left outer join
    reading_room_genres g on og.genre_id = g.id left outer join
    reading_room_object_extents oe on o.id = oe.reading_room_object_id left outer join
    reading_room_alternate_titles alt_title on cnt.id = alt_title.content_id left outer join
    reading_room_uniform_titles u_title ON cnt.uniform_title_id = u_title.id left outer join 
    reading_room_part_centuries pc on p.id = pc.part_id
group by o.id,
	o.hmml_project_number,
	o.inputter,
	o.input_date,
	o.shelf_mark,
	o.object_type,
	o.provenance,
    o.binding,
    o.processed_by,
    o.access_restriction,
    o.icon_name,
    o.support,
    o.active;