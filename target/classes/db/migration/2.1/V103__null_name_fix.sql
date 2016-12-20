update reading_room_contributors set display_name = null, authority_uri_lc = null, authority_uri_viaf = null where name is null;

update reading_room_uniform_titles set name_ns = null, authority_uri_lc = null, authority_uri_viaf = null where name is null;