package hello.matdil.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisCacheHelper {

    private final RedisTemplate<String, Object> redisTemplate;

    public <T> Optional<T> get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (clazz.isInstance(value)) {
            return Optional.of(clazz.cast(value));
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

}

