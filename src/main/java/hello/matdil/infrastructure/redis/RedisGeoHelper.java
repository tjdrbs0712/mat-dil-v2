package hello.matdil.infrastructure.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisGeoHelper {

    @Qualifier("geoRedisTemplate")
    private final RedisTemplate<String, String> redisTemplate;

    public RedisGeoHelper(@Qualifier("geoRedisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void geoAdd(String key, Point point, String member) {
        redisTemplate.opsForGeo().add(key, point, member);

    }

    public List<String> geoRadius(String key, Point point, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args) {
        Circle within = new Circle(point, distance);

        GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo().radius(key, within, args);
        if (results == null) return List.of();

        return results.getContent().stream()
                .map(result -> result.getContent().getName())
                .toList();
    }

    public void geoRemove(String key, String member) {
        redisTemplate.opsForGeo().remove(key, member);
    }
}