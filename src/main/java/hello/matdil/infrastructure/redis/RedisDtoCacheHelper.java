package hello.matdil.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisDtoCacheHelper {

    @Qualifier("objectRedisTemplate")
    private final RedisTemplate<String, Object> redisTemplate;

    public <T> Optional<T> get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) return Optional.empty();
        if (clazz.isInstance(value)) {
            return Optional.of(clazz.cast(value));
        }
        log.error("Redis 데이터 타입 불일치! key={}, expectedType={}, actualType={}",
                key, clazz.getName(), value.getClass().getName());
        return Optional.empty();
    }

    public void put(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void multiSet(Map<String, Object> data, Duration ttl) {
        redisTemplate.opsForValue().multiSet(data);
        data.keySet().forEach(key -> redisTemplate.expire(key, ttl));
    }

    public <T> Map<String, T> multiGet(List<String> keys, Class<T> clazz) {
        List<Object> values = redisTemplate.opsForValue().multiGet(keys);
        Map<String, T> result = new HashMap<>();
        if (values == null) return result;
        for (int i = 0; i < keys.size(); i++) {
            Object value = values.get(i);
            if (clazz.isInstance(value)) {
                result.put(keys.get(i), clazz.cast(value));
            }
        }

        return result;
    }

    public <T> T getOrLoad(String key, Class<T> clazz, Supplier<T> dbLoader, Duration ttl) {
        return get(key, clazz).orElseGet(() -> {
            T value = dbLoader.get();
            put(key, value, ttl);
            return value;
        });
    }
}

