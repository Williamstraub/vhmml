alter table reading_room_objects add column locked boolean default 0;

create or replace view reading_room_overview as
select 
	o.id,
    o.archival_data_id,
	o.hmml_project_number,
	o.inputter,
	o.input_date,
	o.assigned_scholar,
	o.shelf_mark,
	o.object_type,
	o.provenance,
	o.binding,
	o.processed_by,
	o.access_restriction,
	o.icon_name,
	if(o.support IS NULL or o.support = '', group_concat(distinct p.support_imported separator '; '), o.support) as support,
	o.capture_date,
	o.bibliography,
	o.common_name,
	concat_ws('; ', ifnull(o.notes, null), ifnull(group_concat(distinct p.part_notes), null), ifnull(group_concat(distinct cnt.item_notes separator '; '), null)) as notes,
	o.active,
    o.locked,
	(case when o.download_option is not null and o.download_option <> 'NO' 
			THEN true
		else
			false
	end) as downloadable,
	ifnull(concat(o.binding_height, ' x ', o.binding_width, ' x ', o.binding_depth, ' cm'), group_concat(distinct p.support_dimensions_imported separator '; ')) as dimensions,
	ifnull(group_concat(distinct concat(oe.extent_count, ' ', oe.display_name) separator '; '), group_concat(distinct oe.folio_imported separator '; ')) as extent, 
	group_concat(distinct oas.name order by oas.name separator '; ') as alternate_surrogates,
	group_concat(distinct cnt.incipit order by cnt.incipit separator '; ') as incipit,
	group_concat(distinct cnt.provisional_title order by cnt.provisional_title separator '; ') as titles, 
	group_concat(distinct cnt.title_ns order by cnt.title_ns separator '; ') as titles_ns, 
	group_concat(distinct alt_title.title order by alt_title.title separator '; ') as alt_titles,
	group_concat(distinct u_title.name order by u_title.name separator '; ') as uniform_titles,  
	group_concat(distinct feat.name order by feat.name separator '; ') as features,		    
	group_concat(distinct ifnull(ac.script, ifnull(p.script, p.script_imported)) order by p.script separator '; ') as scripts,
	group_concat(distinct ifnull(ac.writing_system, ifnull(p.writing_system, p.writing_system_imported)) order by p.writing_system separator '; ') as writing_systems,		    		    
	group_concat(distinct concat_ws(' ', p.cardinal_of_origin, concat_ws(', ', p.country_of_origin, p.region_of_origin, p.city_of_origin)) separator '; ') as place_of_origin,
	f.name as format,
	co.name as country,
	ci.name as city,
	r.name as repository, 	
    i.name as holding_institution,
	ifnull(group_concat(DISTINCT pc.century SEPARATOR ' '), group_concat(DISTINCT p.century_imported SEPARATOR ' ')) as centuries,
	max(pc.century) as last_century,
	min(pc.century) as first_century,	    
	max(p.century_uncertain) as century_uncertain,	  
	min(ifnull(ad.begin_date, p.begin_date)) as begin_date,
	max(ifnull(ad.end_date, p.end_date)) as end_date,    
	ad.creator as archival_creators,
	ad.title as archival_title,
    ad.scope_content as archival_scope_content,
	group_concat(DISTINCT ifnull(l2.name, l.name) SEPARATOR '; ') as languages,
	group_concat(DISTINCT contrib.name SEPARATOR '; ') as authors,
	group_concat(DISTINCT authors.name_ns SEPARATOR '; ') as authors_ns,
	group_concat(DISTINCT contrib.display_name SEPARATOR '; ') as authors_display,           
	group_concat(DISTINCT ifnull(contrib8.name, contrib2.name) SEPARATOR '; ') as other_contributors,
	group_concat(DISTINCT ifnull(contrib8.display_name, contrib2.display_name) SEPARATOR '; ') as other_contributors_display,	
	group_concat(DISTINCT contrib3.name SEPARATOR '; ') as former_owners,
	group_concat(DISTINCT ifnull(contrib6.name, contrib4.name) SEPARATOR '; ') as artists,
	group_concat(DISTINCT ifnull(contrib7.name, contrib5.name) SEPARATOR '; ') as scribes,    
	group_concat(DISTINCT ifnull(content_scribes.name_ns, scribes.name_ns) SEPARATOR '; ') as scribes_ns,   
	group_concat(DISTINCT contrib9.name SEPARATOR '; ') as archival_contributors,
	group_concat(DISTINCT contrib9.display_name SEPARATOR '; ') as archival_contributors_display,
	group_concat(DISTINCT contrib10.name SEPARATOR '; ') as part_other_contributors,
    group_concat(DISTINCT contrib10.display_name SEPARATOR '; ') as part_other_contributors_display,
	group_concat(DISTINCT s.name SEPARATOR '; ') as subjects,
	group_concat(DISTINCT g.name SEPARATOR '; ') as genres,
    group_concat(DISTINCT ac.description SEPARATOR '; ') as archival_content_description,
    group_concat(DISTINCT ac.notes SEPARATOR '; ') as archival_content_notes,
    group_concat(DISTINCT ac.bibliography SEPARATOR '; ') as archival_bibliography
from reading_room_objects o left outer join
	reading_room_archival_data ad on ad.id = o.archival_data_id left outer join
    reading_room_archival_content ac on ad.id = ac.archival_data_id left outer join
    reading_room_content archive_parent_content on ac.id = archive_parent_content.id left outer join
	reading_room_countries co on o.country_id = co.id left outer join
	reading_room_cities ci on o.city_id = ci.id left outer join
	reading_room_repositories r on o.repository_id = r.id left outer join    
    reading_room_institutions i on o.holding_institution_id = i.id left outer join    
	reading_room_object_parts p on o.id = p.reading_room_object_id left join			
	reading_room_formats f on o.surrogate_format_id = f.id left outer join	
	reading_room_object_features of on o.id = of.reading_room_object_id left outer join		
	reading_room_features feat on of.feature_id = feat.id left outer join
	reading_room_content cnt on p.id = cnt.reading_room_object_part_id left join
	reading_room_content_languages cl on cnt.id = cl.reading_room_content_id left outer join
	reading_room_languages l on cl.language_id = l.id left outer join	 
    reading_room_archival_data_languages adl on adl.archival_data_id = o.archival_data_id left outer join
    reading_room_languages l2 on adl.language_id = l2.id left outer join           
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
    reading_room_content_artists content_artists on content_artists.reading_room_content_id = archive_parent_content.id left outer join
    reading_room_contributors contrib6 on content_artists.reading_room_contributor_id = contrib6.id left outer join    
    reading_room_content_scribes content_scribes on content_scribes.reading_room_content_id = archive_parent_content.id left outer join
    reading_room_contributors contrib7 on content_scribes.reading_room_contributor_id = contrib7.id left outer join    
	reading_room_content_other_contributors other_contrib2 on other_contrib2.reading_room_content_id = archive_parent_content.id left outer join
    reading_room_contributors contrib8 on other_contrib2.reading_room_contributor_id = contrib8.id left outer join
    reading_room_archival_associated_names archival_associated_names on archival_associated_names.archival_data_id = ad.id left outer join
    reading_room_contributors contrib9 on archival_associated_names.contributor_id = contrib9.id left outer join
    reading_room_part_other_contributors part_contrib on p.id = part_contrib.part_id left outer join
    reading_room_contributors contrib10 on part_contrib.contributor_id = contrib10.id left outer join
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
	o.assigned_scholar,
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
	o.common_name,
	o.notes,
	o.active,
    o.locked,
    ad.creator,
	ad.title,
	ad.scope_content,
	ad.custodial_history,
	ad.historical_note;