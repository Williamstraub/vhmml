CREATE TABLE `tags` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `tag` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11235 DEFAULT CHARSET=utf8;

CREATE TABLE `reference_tags` (
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `reference_entry_id` INT(11) not null,
	`tag_id` INT(11) not null,
     PRIMARY KEY(`id`)
) ENGINE=InnoDb AUTO_INCREMENT=11235 DEFAULT CHARSET=utf8;