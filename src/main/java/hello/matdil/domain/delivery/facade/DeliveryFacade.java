package hello.matdil.domain.delivery.facade;

import hello.matdil.domain.delivery.dto.DeliveryAssignmentResponseDto;
import hello.matdil.domain.delivery.entity.Delivery;
import hello.matdil.domain.delivery.service.DeliveryService;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.reader.OrderReader;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.domain.store.reader.StoreSummaryLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryFacade {

    private final DeliveryService deliveryService;
    private final OrderReader orderReader;
    private final StoreSummaryLoader storeSummaryLoader;


    public DeliveryAssignmentResponseDto assignDeliveryToRider(Long deliveryId, Long riderId) {
        Delivery assignedDelivery = deliveryService.assignDeliveryToRider(deliveryId, riderId);
        Order order = orderReader.readWithItems(assignedDelivery.getOrderId());
        StoreSummaryResponseDto storeSummary = storeSummaryLoader.loadWithCacheFallback(order.getStoreId());

        return DeliveryAssignmentResponseDto.from(assignedDelivery, order, storeSummary);
    }
}