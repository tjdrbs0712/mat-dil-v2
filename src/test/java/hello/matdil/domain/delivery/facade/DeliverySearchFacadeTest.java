package hello.matdil.domain.delivery.facade;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.delivery.dto.DeliverySearchResponseDto;
import hello.matdil.domain.delivery.entity.DeliveryStatus;
import hello.matdil.domain.delivery.service.DeliveryGeoService;
import hello.matdil.domain.delivery.util.DeliveryDetailLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DeliverySearchFacadeTest {

    @Mock
    
    private DeliveryGeoService deliveryGeoService;
    @Mock
    
    private DeliveryDetailLoader deliveryDetailLoader;

    @InjectMocks
    private DeliverySearchFacade deliverySearchFacade;

    @Test
    void 라이더_기준_거리순_정렬_성공() {
        // given
        List<Long> sortedIds = List.of(3L, 1L, 2L);
        given(deliveryGeoService.findNearbyDeliveryIds(any(Point.class), any(Distance.class))).willReturn(sortedIds);

        Address address = new Address("서울시", "강남대로", "123", 37.0, 127.0);
        DeliverySearchResponseDto dto1 = new DeliverySearchResponseDto(
                1L, 10L, "가게1", address, BigDecimal.valueOf(3000), DeliveryStatus.READY);
        DeliverySearchResponseDto dto2 = new DeliverySearchResponseDto(
                2L, 20L, "가게2", address, BigDecimal.valueOf(3000), DeliveryStatus.READY);
        DeliverySearchResponseDto dto3 = new DeliverySearchResponseDto(
                3L, 30L, "가게3", address, BigDecimal.valueOf(3000), DeliveryStatus.READY);
        Map<Long, DeliverySearchResponseDto> detailsMap = Map.of(1L, dto1, 2L, dto2, 3L, dto3);
        given(deliveryDetailLoader.loadByDeliveryIds(sortedIds)).willReturn(detailsMap);

        // when
        List<DeliverySearchResponseDto> finalResult = deliverySearchFacade.getDeliveries(37.5, 127.0, 3.0);

        // then
        assertThat(finalResult).hasSize(3);
        assertThat(finalResult)
                .extracting(DeliverySearchResponseDto::deliveryId)
                .containsExactly(3L, 1L, 2L);
    }
}