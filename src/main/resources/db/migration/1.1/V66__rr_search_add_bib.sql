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
    o.capture_date,
    o.bibliography,
    o.active,
    (case when o.download_option is not null and o.download_option <> 'NO' 
    		THEN true
    	else
    		false
	end) as downloadable,
    concat(o.binding_height, ' x ', o.binding_width, ' x ', o.binding_depth, ' cm') as dimensions,    
    group_concat(distinct concat(oe.extent_count, ' ', oe.display_name) separator '; ') as extent,	
    group_concat(distinct oas.name order by oas.name separator '; ') as alternate_surrogates,
    group_concat(distinct cnt.incipit order by cnt.incipit separator '; ') as incipit,
    group_concat(distinct cnt.provisional_title order by cnt.provisional_title separator '; ') as titles,  
    group_concat(distinct cnt.title_ns order by cnt.title_ns separator '; ') as titles_ns,
    group_concat(distinct alt_title.title order by alt_title.title separator '; ') as alt_titles, 
    group_concat(distinct u_title.name order by u_title.name separator '; ') as uniform_titles,
    group_concat(distinct feat.name order by feat.name separator '; ') as features,
    group_concat(distinct p.script order by p.script separator '; ') as scripts,
    group_concat(distinct p.writing_system order by p.writing_system separator '; ') as writing_systems,
    group_concat(distinct concat_ws(' ', p.cardinal_of_origin, concat_ws(', ', p.country_of_origin, p.region_of_origin, p.city_of_origin)) separator '; ') as place_of_origin,
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
    group_concat(DISTINCT contrib.name_ns SEPARATOR '; ') as authors_ns,
    group_concat(DISTINCT contrib.display_name SEPARATOR '; ') as authors_display,
    group_concat(DISTINCT contrib2.name SEPARATOR '; ') as other_contributors,
    group_concat(DISTINCT contrib2.display_name SEPARATOR '; ') as other_contributors_display, 
    group_concat(DISTINCT contrib3.name SEPARATOR '; ') as former_owners,
    group_concat(DISTINCT contrib4.name SEPARATOR '; ') as artists,
    group_concat(DISTINCT contrib5.name SEPARATOR '; ') as scribes,
    group_concat(DISTINCT contrib5.name_ns SEPARATOR '; ') as scribes_ns,
    group_concat(DISTINCT s.name SEPARATOR '; ') as subjects,
    group_concat(DISTINCT g.name SEPARATOR '; ') as genres    
from reading_room_objects o left outer join
	reading_room_countries co on o.country_id = co.id left outer join
	reading_room_cities ci on o.city_id = ci.id left outer join
	reading_room_repositories r on o.repository_id = r.id left outer join	
	reading_room_object_parts p on o.id = p.reading_room_object_id left join
   	reading_room_formats f on o.surrogate_format_id = f.id left outer join
   	reading_room_object_features of on o.id = of.reading_room_object_id left outer join		
    reading_room_features feat on of.feature_id = feat.id left outer join
    reading_room_content cnt on p.id = cnt.reading_room_object_part_id left join
    reading_room_content_languages cl on cnt.id = cl.reading_room_content_id left outer join
    reading_room_languages l on cl.language_id = l.id left outer join    
    reading_room_content_authors authors on cnt.id = authors.reading_room_content_id left outer join
	reading_room_contributors contrib on authors.reading_room_contributor_id = contrib.id left outer join        
    reading_room_content_other_contributors other_contrib on cnt.id = other_contrib.reading_room_content_id left outer join    
    reading_room_contributors contrib2 on other_contrib.reading_room_contributor_id = contrib2.id left outer join
    reading_room_former_owners former_owners on o.id = former_owners.reading_room_object_id left outer join        
	reading_room_contributors contrib3 on former_owners.contributor_id = contrib3.id left outer join    
    reading_room_artists artists on p.id = artists.part_id left outer join        
	reading_room_contributors contrib4 on artists.contributor_id = contrib4.id left outer join                
	reading_room_scribes scribes on p.id = scribes.part_id left outer join        
	reading_room_contributors contrib5 on scribes.contributor_id = contrib5.id left outer join
    reading_room_object_subjects os on o.id = os.reading_room_object_id left outer join
    reading_room_subjects s on os.subject_id = s.id left outer join
    reading_room_object_genres og on o.id = og.reading_room_object_id left outer join
    reading_room_genres g on og.genre_id = g.id left outer join
    reading_room_object_extents oe on o.id = oe.reading_room_object_id left outer join
    reading_room_object_alternate_surrogates oas on o.id = oas.reading_room_object_id left outer join
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
    o.capture_date,
    o.bibliography,
    o.active;