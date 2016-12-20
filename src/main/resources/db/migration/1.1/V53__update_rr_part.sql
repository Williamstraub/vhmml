alter table reading_room_object_parts change column part_number part_number varchar(7);

alter table reading_room_object_parts change column country_of_origin_id country_of_origin varchar(255);
alter table reading_room_object_parts change column city_of_origin_id city_of_origin varchar(255);