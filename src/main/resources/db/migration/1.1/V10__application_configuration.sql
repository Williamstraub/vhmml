CREATE TABLE `application_configuration` (
  `prop_key` VARCHAR(255) NOT NULL,
  `prop_value` VARCHAR(255) NULL,
  PRIMARY KEY (`prop_key`)
) ENGINE=InnoDB AUTO_INCREMENT=11235 DEFAULT CHARSET=utf8;

insert into application_configuration values('default.page.size', '25');
insert into application_configuration values('zotero.user.id', '1750527');
insert into application_configuration values('zotero.auth.key', 'JN2av81xq9nnIJqtzfXPvsAJ');
insert into application_configuration values('web.service.proxy.server', 'proxysrvfe.ad.csbsju.edu');
insert into application_configuration values('web.service.proxy.port', '80');
insert into application_configuration values('vhmml.systems.librarian.email', 'wstraub@hmml.org');
