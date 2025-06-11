//package hello.matdil.domain.delivery.service;
//
//import hello.matdil.domain.delivery.dto.DeliverySearchResponseDto;
//import hello.matdil.domain.delivery.entity.Delivery;
//import hello.matdil.domain.delivery.repository.DeliveryRepository;
//import hello.matdil.infrastructure.redis.RedisCacheHelper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.geo.Distance;
//import org.springframework.data.geo.Metrics;
//import org.springframework.data.geo.Point;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class DeliverySearchService {
//    private final RedisCacheHelper redisCacheHelper;
//    private final DeliveryRepository deliveryRepository;
//    private static final String DELIVERY_GEO_KEY = "deliveries:ready";
//
//    public List<DeliverySearchResponseDto> findNearbyDeliveries(double latitude, double longitude, double radiusKm) {
//        Point riderLocation = new Point(longitude, latitude);
//        Distance radius = new Distance(radiusKm, Metrics.KILOMETERS);
////        RedisGeoCommands.GeoRadiusCommandArgs args = // ... (옵션 설정)
//
//                List<Long> deliveryIds = redisCacheHelper
//                        .geoRadius(DELIVERY_GEO_KEY, riderLocation, radius, args)
//                        .stream()
//                        .map(Long::parseLong)
//                        .toList();
//
//        if (deliveryIds.isEmpty()) {
//            return List.of();
//        }
//
//        // 2. ✨ [중요] ID 목록으로 상세 정보를 조회할 때, 만들어두신 'getBatch' 로직을 활용할 수 있습니다.
//        //    (단, getBatch가 아닌 개별 상세 정보 캐시를 조회해야 함. OrderCacheService의 getBatch 참고)
//        //    Map<Long, OrderCacheDto> details = orderCacheService.getBatch(deliveryIds);
//        //    만약 상세 정보 캐시가 없다면, DB에서 조회합니다.
//        List<Delivery> deliveries = deliveryRepository.findAllByIdInWithDetails(deliveryIds);
//
//        return deliveries.stream()
//                .map(DeliverySearchResponseDto::from)
//                .toList();
//    }
//}
