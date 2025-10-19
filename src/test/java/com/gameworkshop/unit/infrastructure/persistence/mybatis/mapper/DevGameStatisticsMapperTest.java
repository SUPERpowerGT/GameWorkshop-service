package com.gameworkshop.unit.infrastructure.persistence.mybatis.mapper;

import com.gameworkshop.domain.DevGameStatistics.model.DevGameStatistics;
import com.gameworkshop.infrastructure.persistence.mybatis.mapper.DevGameStatisticsMapper;
import com.gameworkshop.unit.common.BaseRepositoryTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DevGameStatisticsMapperTest extends BaseRepositoryTest {

    @Autowired
    private DevGameStatisticsMapper mapper;

    @Test
    @Order(1)
    @DisplayName("✅ Insert & Query DevGameStatistics")
    void testInsertAndQuery() {
        DevGameStatistics stats = new DevGameStatistics(
                UUID.randomUUID().toString(),
                gameId,
                5, 2, 4.5,
                LocalDateTime.now()
        );

        mapper.insert(stats);
        Optional<DevGameStatistics> found = mapper.findByGameId(gameId);

        assertTrue(found.isPresent());
        assertEquals(5, found.get().getViewCount());
        System.out.println("✅ testInsertAndQuery passed: " + found.get());
    }

    @Test
    @Order(2)
    @DisplayName("✅ Update counts correctly")
    void testUpdateCounts() {
        DevGameStatistics stats = new DevGameStatistics(
                UUID.randomUUID().toString(),
                gameId,
                10, 5, 4.2,
                LocalDateTime.now()
        );
        mapper.insert(stats);
        mapper.updateCounts(gameId, 3, 2);

        Optional<DevGameStatistics> updated = mapper.findByGameId(gameId);
        assertTrue(updated.isPresent());
        assertEquals(13, updated.get().getViewCount());
        assertEquals(7, updated.get().getDownloadCount());
        System.out.println("✅ testUpdateCounts passed: " + updated.get());
    }
}
