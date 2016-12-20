alter table reading_room_content drop column supplied_title;
alter table reading_room_content drop column edition_statement; 
alter table reading_room_content drop column printing_statement; 
alter table reading_room_content drop column format_statement;

alter table reading_room_object_parts add column edition_statement varchar(255);
alter table reading_room_object_parts add column printing_statement varchar(255);
alter table reading_room_object_parts add column format_statement varchar(255);