ALTER TABLE `application_configuration` ADD COLUMN `input_type` varchar(50) not null DEFAULT 'text';
ALTER TABLE `application_configuration` ADD COLUMN `list_values` varchar(255) not null DEFAULT 'text';

update application_configuration set input_type = 'textarea' where prop_key = 'admin.email.elastic.search.unavailable';
update application_configuration set input_type = 'textarea' where prop_key = 'admin.email.image.server.unavailable';
update application_configuration set input_type = 'textarea' where prop_key = 'vhmml.email.signature';

