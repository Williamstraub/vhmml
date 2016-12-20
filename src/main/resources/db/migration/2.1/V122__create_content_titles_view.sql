create or replace view reading_room_content_titles_view as(
	select 
		o.hmml_project_number, 
		c.id as content_id, 
   		c.provisional_title as title 
	from
		reading_room_objects o join 
		reading_room_object_parts p on o.id = p.reading_room_object_id join
	   	reading_room_content c on p.id = c.reading_room_object_part_id  		
	where c.provisional_title is not null
);