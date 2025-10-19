package com.gameworkshop.unit.common;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 通用仓储层测试基类：
 * - 自动加载 Spring Boot 环境
 * - 自动创建开发者与游戏基础数据
 * - 自动事务回滚（测试完成后数据库会恢复干净）
 */
@SpringBootTest
@Transactional
public abstract class BaseRepositoryTest {

    @Autowired
    protected JdbcTemplate jdbc;

    protected String developerProfileId;
    protected String userId;
    protected String gameId;

    @BeforeEach
    void setupBaseData() {
        this.developerProfileId = "dev-" + UUID.randomUUID();
        this.userId = "user-" + UUID.randomUUID();
        this.gameId = UUID.randomUUID().toString();

        // 插入 developer_profile
        jdbc.update("""
            INSERT INTO developer_profile (id, user_id, project_count)
            VALUES (?, ?, 0)
        """, developerProfileId, userId);

        // 插入 dev_game
        jdbc.update("""
            INSERT INTO dev_game (id, developer_profile_id, name, description, release_date, created_at, updated_at)
            VALUES (?, ?, 'TestGame', 'Test game for repository test.', now(), now(), now())
        """, gameId, developerProfileId);

        System.out.printf("🧱 Initialized base data: devProfile=%s, user=%s, game=%s%n",
                developerProfileId, userId, gameId);
    }
}
