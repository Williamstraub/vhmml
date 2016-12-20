alter table reading_room_objects drop column decoration_illustration;

alter table reading_room_archival_data add column decoration varchar(255);
alter table reading_room_archival_data add column sub_sub_series varchar(500);
alter table reading_room_archival_content add column decoration varchar(255);