-- need to trim any names with leading/trailing spaces so they sort by name correctly
update reading_room_archival_containers set name = trim(name);
update reading_room_cities set name = trim(name);
update reading_room_contributors set name = trim(name);
update reading_room_countries set name = trim(name);
update reading_room_features set name = trim(name);
update reading_room_formats set name = trim(name);		
update reading_room_genres set name = trim(name);
update reading_room_institutions INSTITUTION set name = trim(name);
update reading_room_languages LANGUAGE set name = trim(name);
update reading_room_repositories set name = trim(name);
update reading_room_scripts set name = trim(name);
update reading_room_uniform_titles set name = trim(name);
update reading_room_writing_systems set name = trim(name);