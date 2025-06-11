//package hello.matdil.infrastructure;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.geo.Distance;
//import org.springframework.data.geo.GeoResults;
//import org.springframework.data.geo.Point;
//import org.springframework.data.redis.connection.RedisGeoCommands;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class RedisGeoCacheHelper {
//
//    private final RedisTemplate<String, Object> redisTemplate;
//    /**
//     * GEO 자료구조에 위치 정보를 추가.
//     * @param key GEO 자료구조의 키
//     * @param point 위도(y), 경도(x) 정보를 담은 Point 객체
//     * @param member 멤버 (예: deliveryId)
//     */
//    public void geoAdd(String key, Point point, String member) {
//        redisTemplate.opsForGeo().add(key, point, member);
//    }
//
//    /**
//     * 특정 지점을 중심으로 반경 내의 멤버들을 조회. (GEORADIUS 또는 GEOSEARCH)
//     * @param key GEO 자료구조의 키
//     * @param point 중심 지점
//     * @param distance 반경 거리
//     * @param args 정렬, 개수 제한 등 추가 옵션
//     * @return 반경 내에 있는 멤버(deliveryId) 목록
//     */
//    public List<String> geoRadius(String key, Point point, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs args) {
//        GeoResults<RedisGeoCommands.GeoLocation<Object>> results = redisTemplate.opsForGeo()
//                .radius(key, point, distance, args);
//
//        if (results == null) {
//            return List.of();
//        }
//
//        return results.getContent().stream()
//                .map(result -> result.getContent().getName().toString())
//                .toList();
//    }
//
//    /**
//     * GEO 자료구조에서 특정 멤버를 삭제합니다. (ZREM)
//     * @param key GEO 자료구조의 키
//     * @param member 삭제할 멤버 (예: deliveryId)
//     */
//    public void geoRemove(String key, String member) {
//        redisTemplate.opsForGeo().remove(key, member);
//    }
//}