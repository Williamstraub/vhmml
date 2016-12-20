package org.vhmml.repository.readingroom;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.vhmml.entity.readingroom.ReadingRoomObjectOverview;

@Repository
public interface ReadingRoomOverviewRepository extends CrudRepository<ReadingRoomObjectOverview, Long> {
	
	@Query(value = OBJECT_OVERVIEW_QUERY + OBJECT_OVERVIEW_WHERE_CLAUSE + OBJECT_OVERVIEW_GROUP_BY, nativeQuery = true)
	public ReadingRoomObjectOverview findObjectOverview(Long readingRoomObjectId);
	
	@Query(value = OBJECT_OVERVIEW_QUERY + OBJECT_OVERVIEW_GROUP_BY, nativeQuery = true)
	public List<ReadingRoomObjectOverview> findAllObjectOverviews();		
	
	public static final String OBJECT_OVERVIEW_WHERE_CLAUSE = " where o.id = ?1 "; 
	public static final String OBJECT_OVERVIEW_GROUP_BY = " group by o.id, " +
			"o.hmml_project_number, " +
			"o.inputter, " +
			"o.input_date, " +
			"o.assigned_scholar, " +
			"o.shelf_mark, " +
			"o.object_type, " +
			"o.provenance, " +
		    "o.binding, " +
		    "o.processed_by, " +
		    "o.access_restriction, " +
		    "o.icon_name, " +
		    "o.support, " +
		    "o.capture_date, " +
		    "o.bibliography, " +
		    "o.common_name, " +
		    "o.notes, " +
		    "o.active," + 
		    "o.locked," + 
		    "o.locked_by," + 
		    "ad.creator," +
		    "ad.title," +
		    "ad.scope_content," +
		    "ad.custodial_history," +
		    "ad.historical_note";
	
	public static final String OBJECT_OVERVIEW_QUERY = "select \r\n" + 
			"	o.id,\r\n" + 
			"	o.hmml_project_number,\r\n" + 
			"	o.inputter,\r\n" + 
			"	o.input_date,\r\n" + 
			"	o.assigned_scholar,\r\n" + 
			"	o.shelf_mark,\r\n" + 
			"	o.object_type,\r\n" + 
			"	o.provenance,\r\n" + 
			"	o.binding,\r\n" + 
			"	o.processed_by,\r\n" + 
			"	o.access_restriction,\r\n" + 
			"	o.icon_name,\r\n" + 
			"	if(o.support IS NULL or o.support = '', group_concat(distinct p.support_imported separator '; '), o.support) as support,\r\n" + 
			"	o.capture_date,\r\n" + 
			"	o.bibliography,\r\n" + 
			"	o.common_name,\r\n" + 
			"	concat_ws('; ', ifnull(o.notes, null), ifnull(group_concat(distinct p.part_notes), null), ifnull(group_concat(distinct cnt.item_notes separator '; '), null)) as notes,\r\n" + 
			"	o.active,\r\n" + 
			"	o.locked,\r\n" + 
			"	o.locked_by,\r\n" + 
			"	(case when o.download_option is not null and o.download_option <> 'NO' \r\n" + 
			"			THEN true\r\n" + 
			"		else\r\n" + 
			"			false\r\n" + 
			"	end) as downloadable,\r\n" + 
			"	ifnull(concat(o.binding_height, ' x ', o.binding_width, ' x ', o.binding_depth, ' cm'), group_concat(distinct p.support_dimensions_imported separator '; ')) as dimensions,\r\n" + 
			"	ifnull(group_concat(distinct concat(oe.extent_count, ' ', oe.display_name) separator '; '), group_concat(distinct oe.folio_imported separator '; ')) as extent, \r\n" + 
			"	group_concat(distinct oas.name order by oas.name separator '; ') as alternate_surrogates,\r\n" + 
			"	group_concat(distinct cnt.incipit order by cnt.incipit separator '; ') as incipit,\r\n" + 
			"	group_concat(distinct cnt.provisional_title order by cnt.provisional_title separator '; ') as titles, \r\n" + 
			"	group_concat(distinct cnt.title_ns order by cnt.title_ns separator '; ') as titles_ns, \r\n" + 
			"	group_concat(distinct alt_title.title order by alt_title.title separator '; ') as alt_titles,\r\n" + 
			"	group_concat(distinct u_title.name order by u_title.name separator '; ') as uniform_titles,  \r\n" + 
			"	group_concat(distinct feat.name order by feat.name separator '; ') as features,		    \r\n" + 
			"	group_concat(distinct ifnull(ac.script, ifnull(p.script, p.script_imported)) order by p.script separator '; ') as scripts,\r\n" + 
			"	ifnull(ad.writing_system, group_concat(distinct ifnull(p.writing_system, p.writing_system_imported))) as writing_systems,\r\n" + 
			"	group_concat(distinct concat_ws(' ', p.cardinal_of_origin, concat_ws(', ', p.country_of_origin, p.region_of_origin, p.city_of_origin)) separator '; ') as place_of_origin,\r\n" + 
			"	f.name as format,\r\n" + 
			"	co.name as country,\r\n" + 
			"	ci.name as city,\r\n" + 
			"	r.name as repository,\r\n" + 			
			"	i.name as holding_institution,\r\n" +
			"	ifnull(\r\n" +
			"		ifnull(group_concat(DISTINCT adc.century SEPARATOR ' '), group_concat(DISTINCT ad.century_imported SEPARATOR ' ')),\r\n" +
			"		ifnull(group_concat(DISTINCT pc.century SEPARATOR ' '), group_concat(DISTINCT p.century_imported SEPARATOR ' '))\r\n" +
			"	) as centuries,\r\n" + 
			"	ifnull(max(adc.century), max(pc.century)) as last_century,\r\n" +
			"	ifnull(min(adc.century), min(pc.century)) as first_century,\r\n" +			
			"	ifnull(max(ad.century_uncertain), max(p.century_uncertain)) as century_uncertain,\r\n" + 
			"	min(ifnull(ad.begin_date, p.begin_date)) as begin_date,\r\n" + 
			"	max(ifnull(ad.end_date, p.end_date)) as end_date,\r\n" +
			"	ifnull(ad.decoration, p.decoration) as decoration,\r\n" +
			"	ad.creator as archival_creators,\r\n" +
			"	ad.title as archival_title,\r\n" +
			"	ad.scope_content as archival_scope_content,\r\n" +	
			"	ad.custodial_history as archival_custodial_history,\r\n" +
			"	ad.historical_note as archival_historical_note,\r\n" +
			"	group_concat(DISTINCT ifnull(l2.name, l.name) SEPARATOR '; ') as languages,\r\n" + 
			"	group_concat(DISTINCT contrib.name SEPARATOR '; ') as authors,\r\n" + 
			"	group_concat(DISTINCT authors.name_ns SEPARATOR '; ') as authors_ns,\r\n" + 
			"	group_concat(DISTINCT contrib.display_name SEPARATOR '; ') as authors_display,\r\n" + 
			"	group_concat(DISTINCT ifnull(contrib8.name, contrib2.name) SEPARATOR '; ') as other_contributors,\r\n" + 
			"	group_concat(DISTINCT ifnull(contrib8.display_name, contrib2.display_name) SEPARATOR '; ') as other_contributors_display,\r\n" + 
			"	group_concat(DISTINCT contrib3.name SEPARATOR '; ') as former_owners,\r\n" + 
			"	group_concat(DISTINCT ifnull(contrib6.name, contrib4.name) SEPARATOR '; ') as artists,\r\n" + 
			"	group_concat(DISTINCT ifnull(contrib7.name, contrib5.name) SEPARATOR '; ') as scribes,\r\n" + 
			"	group_concat(DISTINCT ifnull(content_scribes.name_ns, scribes.name_ns) SEPARATOR '; ') as scribes_ns,\r\n" + 										
			"	group_concat(DISTINCT contrib9.name SEPARATOR '; ') as archival_contributors,\r\n" +
			"	group_concat(DISTINCT contrib9.display_name SEPARATOR '; ') as archival_contributors_display,\r\n" +
			"	group_concat(DISTINCT contrib10.name SEPARATOR '; ') as part_other_contributors,\r\n" +
			"	group_concat(DISTINCT contrib10.display_name SEPARATOR '; ') as part_other_contributors_display,\r\n" +
			"	group_concat(DISTINCT s.name SEPARATOR '; ') as subjects,\r\n" + 
			"	group_concat(DISTINCT g.name SEPARATOR '; ') as genres,\r\n" + 
			"   group_concat(DISTINCT ac.description SEPARATOR '; ') as archival_content_description,\r\n" + 
			"   group_concat(DISTINCT ac.notes SEPARATOR '; ') as archival_content_notes,\r\n" + 
			"   group_concat(DISTINCT ac.bibliography SEPARATOR '; ') as archival_bibliography\r\n" +
			"from reading_room_objects o left outer join\r\n" + 
			"	reading_room_archival_data ad on o.id = ad.reading_room_object_id left outer join\r\n" + 
			"	reading_room_archival_data_centuries adc on ad.id = adc.archival_data_id left outer join\r\n" +
			"   reading_room_archival_content ac on ad.id = ac.archival_data_id left outer join\r\n" + 
			"   reading_room_content archive_parent_content on ac.id = archive_parent_content.id left outer join\r\n" + 
			"	reading_room_countries co on o.country_id = co.id left outer join\r\n" + 
			"	reading_room_cities ci on o.city_id = ci.id left outer join\r\n" + 
			"	reading_room_repositories r on o.repository_id = r.id left outer join\r\n" + 			
			"	reading_room_institutions i on o.holding_institution_id = i.id left outer join\r\n" +			
			"	reading_room_object_parts p on o.id = p.reading_room_object_id left join\r\n" + 
			"	reading_room_formats f on o.surrogate_format_id = f.id left outer join\r\n" + 
			"	reading_room_object_features of on o.id = of.reading_room_object_id left outer join\r\n" + 
			"	reading_room_features feat on of.feature_id = feat.id left outer join\r\n" + 
			"	reading_room_content cnt on p.id = cnt.reading_room_object_part_id left join\r\n" + 
			"	reading_room_content_languages cl on cnt.id = cl.reading_room_content_id left outer join\r\n" + 
			"	reading_room_languages l on cl.language_id = l.id left outer join\r\n" + 
			"   reading_room_archival_data_languages adl on adl.archival_data_id = ad.id left outer join\r\n" + 
			"   reading_room_languages l2 on adl.language_id = l2.id left outer join\r\n" + 
			"	reading_room_content_authors authors on cnt.id = authors.reading_room_content_id left outer join\r\n" + 
			"	reading_room_contributors contrib on authors.reading_room_contributor_id = contrib.id left outer join\r\n" + 
			"	reading_room_content_other_contributors other_contrib on cnt.id = other_contrib.reading_room_content_id left outer join\r\n" + 
			"	reading_room_contributors contrib2 on other_contrib.reading_room_contributor_id = contrib2.id left outer join\r\n" + 
			"	reading_room_former_owners former_owners on o.id = former_owners.reading_room_object_id left outer join \r\n" + 
			"	reading_room_contributors contrib3 on former_owners.contributor_id = contrib3.id left outer join\r\n" + 
			"	reading_room_artists artists on p.id = artists.part_id left outer join\r\n" + 
			"	reading_room_contributors contrib4 on artists.contributor_id = contrib4.id left outer join\r\n" + 
			"	reading_room_scribes scribes on p.id = scribes.part_id left outer join\r\n" + 
			"	reading_room_contributors contrib5 on scribes.contributor_id = contrib5.id left outer join\r\n" +									
			"   reading_room_content_artists content_artists on content_artists.reading_room_content_id = archive_parent_content.id left outer join\r\n" + 
			"   reading_room_contributors contrib6 on content_artists.reading_room_contributor_id = contrib6.id left outer join\r\n" + 
			"   reading_room_content_scribes content_scribes on content_scribes.reading_room_content_id = archive_parent_content.id left outer join\r\n" + 
			"   reading_room_contributors contrib7 on content_scribes.reading_room_contributor_id = contrib7.id left outer join\r\n" + 
			"	reading_room_content_other_contributors other_contrib2 on other_contrib2.reading_room_content_id = archive_parent_content.id left outer join\r\n" + 
			"   reading_room_contributors contrib8 on other_contrib2.reading_room_contributor_id = contrib8.id left outer join\r\n" +
			"   reading_room_archival_associated_names archival_associated_names on archival_associated_names.archival_data_id = ad.id left outer join\r\n" +
			"   reading_room_contributors contrib9 on archival_associated_names.contributor_id = contrib9.id left outer join\r\n" +
			"	reading_room_part_other_contributors part_contrib on p.id = part_contrib.part_id left outer join\r\n" +
			"	reading_room_contributors contrib10 on part_contrib.contributor_id = contrib10.id left outer join\r\n" +
			"	reading_room_object_subjects os on o.id = os.reading_room_object_id left outer join\r\n" + 
			"	reading_room_subjects s on os.subject_id = s.id left outer join\r\n" + 
			"	reading_room_object_genres og on o.id = og.reading_room_object_id left outer join\r\n" + 
			"	reading_room_genres g on og.genre_id = g.id left outer join\r\n" + 
			"	reading_room_object_extents oe on o.id = oe.reading_room_object_id left outer join\r\n" + 
			"	reading_room_object_alternate_surrogates oas on o.id = oas.reading_room_object_id left outer join\r\n" + 
			"	reading_room_alternate_titles alt_title on cnt.id = alt_title.content_id left outer join\r\n" + 
			"	reading_room_uniform_titles u_title ON cnt.uniform_title_id = u_title.id left outer join\r\n" + 
			"	reading_room_part_centuries pc on p.id = pc.part_id";
}
