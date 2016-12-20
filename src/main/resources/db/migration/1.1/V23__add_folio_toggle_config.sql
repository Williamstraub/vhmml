delete from application_configuration where prop_key = 'hide.folio';
insert into application_configuration (prop_key, prop_value, input_type) values('hide.folio', 'false', 'checkbox');