create or replace view reading_room_part_other_contributors as
select 
	part_id, 
    contributor_id,
    name_ns
from 
	reading_room_part_contributors 
where 
	contributor_type not in('SCRIBE', 'ARTIST') or contributor_type is null;