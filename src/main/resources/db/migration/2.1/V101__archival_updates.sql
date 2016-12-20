alter table reading_room_archival_content add column begin_date int(2);
alter table reading_room_archival_content add column end_date int(2);
alter table reading_room_archival_content add column support varchar(255);
alter table reading_room_archival_content add column support_dimensions varchar(100);
alter table reading_room_archival_content add column support_width float;
alter table reading_room_archival_content add column support_height float;
alter table reading_room_archival_content add column medium varchar(255);
alter table reading_room_archival_content add column page_layout varchar(255);

create table reading_room_archival_content_centuries (
  id int(11) NOT NULL AUTO_INCREMENT,
  archival_content_id int(11),
  century INT(11),
  PRIMARY KEY (id)  
) ENGINE=InnoDB AUTO_INCREMENT=142436 DEFAULT CHARSET=utf8;