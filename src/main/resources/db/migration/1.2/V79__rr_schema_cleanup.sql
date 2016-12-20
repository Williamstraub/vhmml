alter table reading_room_objects drop column revisit;
alter table reading_room_objects drop column reviser_date;
alter table reading_room_objects drop column url;
alter table reading_room_objects drop column url2;
alter table reading_room_objects drop column url3;
alter table reading_room_objects drop column uri;
alter table reading_room_objects drop column add_resources;
alter table reading_room_objects drop column associated_works;
alter table reading_room_objects drop column binding_notes;
alter table reading_room_objects drop column composite;

alter table reading_room_objects add column foliation varchar(255);

alter table reading_room_object_parts drop column medium;
alter table reading_room_object_parts drop column document;
alter table reading_room_object_parts drop column dated_or_not;
alter table reading_room_object_parts drop column music;
alter table reading_room_object_parts drop column watermark;
alter table reading_room_object_parts drop column revisit;
alter table reading_room_object_parts drop column reviser;
alter table reading_room_object_parts drop column foliation;

alter table reading_room_content drop column docket;
alter table reading_room_content drop column music;
alter table reading_room_content drop column reviser;

alter table reading_room_countries drop column name_iso_3166;