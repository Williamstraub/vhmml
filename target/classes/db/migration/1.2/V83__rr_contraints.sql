ALTER TABLE reading_room_countries ADD UNIQUE unique_index(name);
ALTER TABLE reading_room_cities ADD UNIQUE unique_index(name, country_id);
ALTER TABLE reading_room_repositories ADD UNIQUE unique_index(name, city_id);