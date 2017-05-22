create table viewings (
  id bigint not null auto_increment primary key,
  user_id bigint,
  show_id bigint,
  episode_id bigint,
  updated_at datetime not null,
  timecode int not null default 0,
  constraint fk_users foreign key (user_id) REFERENCES users(id),
  constraint fk_viewings_shows foreign key (show_id) REFERENCES shows(id),
  constraint fk_episodes foreign key (episode_id) REFERENCES episodes(id)
);