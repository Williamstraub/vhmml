alter table reading_room_archival_data add column writing_system varchar(255);
alter table reading_room_archival_data add column century_uncertain boolean default 0;

alter table reading_room_archival_content add column writing_system varchar(255);
alter table reading_room_archival_content add column native_date varchar(255);
alter table reading_room_archival_content add column century_uncertain boolean default 0;
alter table reading_room_archival_content add column item_type varchar(30);