alter table reading_room_object_parts drop column date_precise;
alter table reading_room_object_parts add column date_precise_year int(4);
alter table reading_room_object_parts add column date_precise_month int(2);
alter table reading_room_object_parts add column date_precise_day int(2);