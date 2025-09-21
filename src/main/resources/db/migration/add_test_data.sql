-- 插入开发者
INSERT INTO developer_profile (id, user_id, project_count)
VALUES ('dev-profile-001', 'user-001', 1);

-- 插入游戏
INSERT INTO dev_game (id, developer_profile_id, name, description, release_date, created_at, updated_at)
VALUES (
           'game-001',
           'dev-profile-001',
           'Mirror Flowers Demo',
           'This is a demo game for MVP testing.',
           now(),
           now(),
           now()
       );

-- 插入游戏资源
INSERT INTO dev_game_asset (id, dev_game_id, asset_type, file_name, storage_path, file_size, mime_type, uploaded_at)
VALUES (
           'asset-001',
           'game-001',
           'zip',
           'mirrorflowersdemo20250825.zip',
           'D:/Project/GameValutProject/game-assets/mirrorflowersdemo20250825.zip',
           1234567,
           'application/zip',
           now()
       );
