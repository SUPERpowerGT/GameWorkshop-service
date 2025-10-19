-- ==========================================================
--  GameVault Developer Service - Database Schema (PostgreSQL)
--  Version: 1.0
--  Author: GameWorkshop Team
-- ==========================================================

-- 1️⃣ Developer Profile 表
CREATE TABLE IF NOT EXISTS developer_profile (
                                                 id VARCHAR(64) PRIMARY KEY,
                                                 user_id VARCHAR(64) NOT NULL UNIQUE,
                                                 project_count INT NOT NULL DEFAULT 0
);

-- 2️⃣ Dev Game 主表
CREATE TABLE IF NOT EXISTS dev_game (
                                        id VARCHAR(64) PRIMARY KEY,
                                        developer_profile_id VARCHAR(64) NOT NULL REFERENCES developer_profile(id),
                                        name VARCHAR(100) NOT NULL,
                                        description VARCHAR(1000) NOT NULL,
                                        release_date TIMESTAMP,
                                        created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                        updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                        CONSTRAINT uk_developer_game_name UNIQUE (developer_profile_id, name)
);

CREATE INDEX IF NOT EXISTS idx_dev_game_developer
    ON dev_game (developer_profile_id);

-- 3️⃣ Dev Game Asset 资源表
CREATE TABLE IF NOT EXISTS dev_game_asset (
                                              id VARCHAR(64) PRIMARY KEY,
                                              dev_game_id VARCHAR(64) NOT NULL REFERENCES dev_game(id) ON DELETE CASCADE,
                                              asset_type VARCHAR(50) NOT NULL,
                                              file_name VARCHAR(200) NOT NULL,
                                              storage_path VARCHAR(500) NOT NULL,
                                              file_size BIGINT NOT NULL CHECK (file_size > 0),
                                              mime_type VARCHAR(50),
                                              uploaded_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_dev_game_asset_game_type
    ON dev_game_asset (dev_game_id, asset_type);

-- 4️⃣ Dev Game Statistics 统计表
CREATE TABLE IF NOT EXISTS dev_game_statistics (
                                                   id VARCHAR(64) PRIMARY KEY,
                                                   game_id VARCHAR(64) NOT NULL UNIQUE REFERENCES dev_game(id),
                                                   view_count INT DEFAULT 0,
                                                   download_count INT DEFAULT 0,
                                                   rating DOUBLE PRECISION DEFAULT 0,
                                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================================
-- ✅ Schema Initialized Successfully
-- ==========================================================
