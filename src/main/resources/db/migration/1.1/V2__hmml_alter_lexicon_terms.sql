ALTER TABLE `lexicon_terms` DROP COLUMN sort_field;
ALTER TABLE `lexicon_terms` ADD COLUMN `removed` boolean not null DEFAULT 0;