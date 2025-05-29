package hello.matdil.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class RedisCacheHelper {

    private final RedisTemplate<String, Object> redisTemplate;

    public <T> Optional<T> get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (clazz.isInstance(value)) {
            return Optional.of(clazz.cast(value));
        }
        else{
            log.warn("Redis 역직렬화 실패. key={}, value class={}", key, value != null ? value.getClass().getName() : "null");
        }
        return Optional.empty();
    }

    public void put(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public <T> Map<String, T> multiGet(List<String> keys, Class<T> clazz) {
        List<Object> values = redisTemplate.opsForValue().multiGet(keys);
        Map<String, T> result = new HashMap<>();

        if (values == null || values.size() != keys.size()) {
            return result;
        }

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

