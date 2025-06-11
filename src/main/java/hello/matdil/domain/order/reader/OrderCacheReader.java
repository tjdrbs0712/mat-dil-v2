package hello.matdil.domain.order.reader;

import hello.matdil.domain.order.dto.cache.OrderCacheDto;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.exception.OrderErrorCode;
import hello.matdil.domain.order.exception.OrderException;
import hello.matdil.domain.order.repository.OrderRepository;
import hello.matdil.domain.order.service.OrderCacheService;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.domain.store.reader.StoreSummaryLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCacheReader {

    private final OrderCacheService orderCacheService;
    private final OrderRepository orderRepository;
    private final StoreSummaryLoader storeSummaryLoader;

    public OrderCacheDto getCashOrder(Long orderId) {
        return orderCacheService.get(orderId)
                .orElseGet(() -> {
                    Order order = orderRepository.findByIdWithNotDeleted(orderId)
                            .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

                    StoreSummaryResponseDto dto = storeSummaryLoader.loadWithCacheFallback(order.getStoreId());
                    OrderCacheDto cacheDto = OrderCacheDto.from(order, dto);
                    orderCacheService.put(order, dto);
                    return cacheDto;
                });
    }
}
