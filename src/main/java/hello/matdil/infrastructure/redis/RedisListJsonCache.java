package hello.matdil.infrastructure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisListJsonCache {

    @Qualifier("objectRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> Optional<List<T>> getList(String key, Class<T> clazz) {
        try {
            String json = (String) redisTemplate.opsForValue().get(key);
            if (json != null) {
                return Optional.of(
                        objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz))
                );
            }
        } catch (Exception e) {
            log.warn("Redis 역직렬화 실패: {}", e.getMessage());
        }
        return Optional.empty();
    }

    public void putList(String key, List<?> value, Duration ttl) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, ttl);
        } catch (Exception e) {
            log.error("Redis 직렬화 실패: {}", e.getMessage());
        }
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
