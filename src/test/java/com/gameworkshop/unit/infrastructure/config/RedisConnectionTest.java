package com.gameworkshop.unit.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisConnectionTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void testRedisConnection() {
        redisTemplate.opsForValue().set("test:key", "hello-redis");
        String value = redisTemplate.opsForValue().get("test:key");
        System.out.println("✅ Redis 返回值: " + value);
        assertThat(value).isEqualTo("hello-redis");
    }
}