insert into reading_room_archival_containers values(null, 'box(es)');
insert into reading_room_archival_containers values(null, 'folder(s)');
insert into reading_room_archival_containers values(null, 'frame(s)');
insert into reading_room_archival_containers values(null, 'mat(s)');
insert into reading_room_archival_containers values(null, 'portfolio(s)');
insert into reading_room_archival_containers values(null, 'sleeve(s)');
insert into reading_room_archival_containers values(null, 'volume(s)');
insert into reading_room_archival_containers values(null, 'other');

alter table reading_room_archival_data add column date_precise_year int(4); 
alter table reading_room_archival_data add column date_precise_month int(2); 
alter table reading_room_archival_data add column date_precise_day int(2);

alter table reading_room_archival_data drop column date_range;
alter table reading_room_archival_data drop column language;
alter table reading_room_archival_data add column begin_date int(2);
alter table reading_room_archival_data add column end_date int(2);
alter table reading_room_archival_data add column native_date varchar(255);

alter table reading_room_archival_content drop column incipit;
alter table reading_room_archival_content drop column incipit_ns;
alter table reading_room_archival_content add column extent varchar(100);
alter table reading_room_archival_content add column description text;

alter table reading_room_archival_content add column date_precise_year int(4); 
alter table reading_room_archival_content add column date_precise_month int(2); 
alter table reading_room_archival_content add column date_precise_day int(2);

create table reading_room_archival_data_centuries (
  id int(11) NOT NULL AUTO_INCREMENT,
  archival_data_id int(11),
  century INT(11),
  PRIMARY KEY (`id`)  
) ENGINE=InnoDB AUTO_INCREMENT=142436 DEFAULT CHARSET=utf8;


create table reading_room_archival_data_languages (
  id int(11) NOT NULL AUTO_INCREMENT,
  archival_data_id int(11),
  language_id INT(11),
  PRIMARY KEY (`id`)  
) ENGINE=InnoDB AUTO_INCREMENT=142436 DEFAULT CHARSET=utf8;