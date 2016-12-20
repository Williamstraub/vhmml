alter table reading_room_object_parts drop column support_depth;

alter table reading_room_object_parts change column support_width support_width float;
alter table reading_room_object_parts change column support_height support_height float;