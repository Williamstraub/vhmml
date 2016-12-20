alter table reading_room_object_parts 
	change decoration decoration MEDIUMTEXT, 
	drop column representational_decoration, 
	drop column other_decoration,
	drop column part_location_count,
	drop column part_location_extent,
	drop column folio_imported,
	add column century_uncertain boolean,
	add column part_location varchar(100);
	
alter table reading_room_content
	change subject_keywords cataloger_tags varchar(255),
	drop column item_location_count,
	drop column item_location_extent,
	drop column folio_span,
	add column item_location varchar(100);
