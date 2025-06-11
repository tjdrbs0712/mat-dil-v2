package hello.matdil.domain.delivery.service;

import hello.matdil.infrastructure.redis.RedisGeoHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.domain.geo.Metrics;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class DeliveryGeoServiceTest {

    @Mock
    private RedisGeoHelper geoHelper; // 의존성을 Mock 객체로 만듭니다.

    @InjectMocks
    private DeliveryGeoService deliveryGeoService; // 테스트 대상 객체

    @Test
    void 배달_목록_조회_성공() {
        // given
        Point location = new Point(127.0, 37.5);
        Distance radius = new Distance(3, Metrics.KILOMETERS);
        List<String> expectedIds = List.of("10", "20", "30");

        given(geoHelper.geoRadius(any(), any(), any(), any())).willReturn(expectedIds);

        // when
        List<Long> actualIds = deliveryGeoService.findNearbyDeliveryIds(location, radius);

        // then
        assertThat(actualIds).hasSize(3);
        assertThat(actualIds).containsExactly(10L, 20L, 30L);

        then(geoHelper).should().geoRadius(any(), any(), any(), any());
    }
}