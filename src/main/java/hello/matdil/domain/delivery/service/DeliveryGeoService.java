package hello.matdil.domain.delivery.service;

import hello.matdil.infrastructure.redis.RedisGeoHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryGeoService {

    private final RedisGeoHelper geoHelper;
    public static final String DELIVERY_GEO_KEY = "deliveries:ready";
    private static final int SEARCH_LIMIT = 50;

    public List<Long> findNearbyDeliveryIds(Point location, Distance radius) {
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeDistance()
                .sortAscending()
                .limit(SEARCH_LIMIT);

        return geoHelper.geoRadius(DELIVERY_GEO_KEY, location, radius, args)
                .stream()
                .map(Long::parseLong)
                .toList();
    }
}