package com.gameworkshop.infrastructure.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DevGameStatisticsCache {

    private final RedisTemplate<String, Long> redisTemplate;

    private static final String PREFIX_VIEW = "devgame:view:";
    private static final String PREFIX_DOWNLOAD = "devgame:download:";

    // ======== INCREMENT METHODS ========
    public void incrementView(String gameId) {
        redisTemplate.opsForValue().increment(PREFIX_VIEW + gameId);
    }

    public void incrementDownload(String gameId) {
        redisTemplate.opsForValue().increment(PREFIX_DOWNLOAD + gameId);
    }

    // ======== GET METHODS ========
    public Long getViewCount(String gameId) {
        Long value = redisTemplate.opsForValue().get(PREFIX_VIEW + gameId);
        return value != null ? value : 0L;
    }

    public Long getDownloadCount(String gameId) {
        Long value = redisTemplate.opsForValue().get(PREFIX_DOWNLOAD + gameId);
        return value != null ? value : 0L;
    }

    // ======== SCAN KEYS (NON-BLOCKING) ========
    public Set<String> getKeysByPrefix(String prefix) {
        Set<String> keys = new HashSet<>();
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            try (var cursor = connection.scan(
                    ScanOptions.scanOptions().match(prefix + "*").count(1000).build())) {
                while (cursor.hasNext()) {
                    keys.add(new String(cursor.next()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
        return keys;
    }

    // ======== CLEAR METHODS ========
    public void resetCounters(String gameId) {
        redisTemplate.delete(PREFIX_VIEW + gameId);
        redisTemplate.delete(PREFIX_DOWNLOAD + gameId);
    }

    public void clearAll() {
        Set<String> keys = redisTemplate.keys("devgame:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
