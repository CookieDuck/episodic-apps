create table episodes (
  id bigint not null auto_increment primary key,
  show_id bigint,
  season_number int,
  episode_number int,
  constraint fk_shows foreign key (show_id) REFERENCES shows(id)
);