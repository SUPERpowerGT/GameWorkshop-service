-- V1.0.0__init_schema.sql
-- Core schema initialization for developer game platform

-- 1. Developer profile table
-- Stores developer-specific information
create table developer_profile(
                                  id varchar(36) primary key, -- Unique developer profile identifier
                                  user_id varchar(36) not null unique, -- External user ID from user service
    -- No foreign key constraint: In microservice architecture, user data is managed in separate user service
    -- Developer profiles are automatically created via event synchronization when users register
                                  project_count INT NOT NULL DEFAULT 0 -- Number of projects created by the developer
);

-- 2. Game table
-- Stores game metadata
create table dev_game(
                         id varchar(36) primary key, -- Unique game identifier
                         developer_profile_id varchar(36) not null references developer_profile(id), -- Associated developer profile
                         name varchar(100) not null, -- Game name
                         description varchar(1000) not null, -- Game description
                         release_date timestamp, -- Official release date (nullable for unreleased games)
                         created_at timestamp not null default now(), -- Record creation timestamp
                         updated_at timestamp not null default now(), -- Last update timestamp
                         CONSTRAINT uk_developer_game_name UNIQUE (developer_profile_id, name) -- Prevents duplicate game names per developer
);

create INDEX idx_dev_game_developer ON dev_game(developer_profile_id); -- Optimizes queries for developer's games

-- 4. Game asset table (modified: asset_type as VARCHAR instead of ENUM)
-- Stores game-related file assets
create table dev_game_asset(
                               id varchar(36) primary key, -- Unique asset identifier
                               dev_game_id varchar(36) not null references dev_game(id) on delete cascade, -- Associated game
                               asset_type varchar(50) NOT NULL, -- Type of asset as string (replaces ENUM)
                               file_name varchar(200) not null, -- Original file name
                               storage_path varchar(500) not null, -- Storage location path
                               file_size bigint not null CHECK (file_size > 0), -- File size in bytes (positive value required)
                               mime_type varchar(50), -- File MIME type
                               uploaded_at timestamp not null default now() -- Upload timestamp
);

create INDEX idx_dev_game_asset_game_type ON dev_game_asset(dev_game_id, asset_type); -- Optimizes asset queries by game and type
