-- 1. Developer profile table
create table developer_profile(
                                  id varchar(36) primary key,
                                  user_id varchar(36) not null unique,
                                  project_count INT NOT NULL DEFAULT 0
);

-- 2. Game table
create table dev_game(
                         id varchar(36) primary key,
                         developer_profile_id varchar(36) not null references developer_profile(id),
                         name varchar(100) not null,
                         description varchar(1000) not null,
                         release_date timestamp,
                         created_at timestamp not null default now(),
                         updated_at timestamp not null default now(),
                         CONSTRAINT uk_developer_game_name UNIQUE (developer_profile_id, name)
);
create INDEX idx_dev_game_developer ON dev_game(developer_profile_id);

-- 3. Game asset table
create table dev_game_asset(
                               id varchar(36) primary key,
                               dev_game_id varchar(36) not null references dev_game(id) on delete cascade,
                               asset_type varchar(50) NOT NULL,
                               file_name varchar(200) not null,
                               storage_path varchar(500) not null,
                               file_size bigint not null CHECK (file_size > 0),
                               mime_type varchar(50),
                               uploaded_at timestamp not null default now()
);
create INDEX idx_dev_game_asset_game_type ON dev_game_asset(dev_game_id, asset_type);
