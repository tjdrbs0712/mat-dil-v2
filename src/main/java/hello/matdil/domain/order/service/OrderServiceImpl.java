package hello.matdil.domain.order.service;

import hello.matdil.domain.order.dto.OrderCursorRequestDto;
import hello.matdil.domain.order.dto.OrderCursorResponseDto;
import hello.matdil.domain.order.dto.OrderResponseDto;
import hello.matdil.domain.order.dto.OrderSummaryDto;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.order.factory.OrderFactory;
import hello.matdil.domain.order.reader.OrderReader;
import hello.matdil.domain.order.repository.OrderRepository;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.reader.StoreSummaryLoader;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.util.pagination.PageAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;
    private final OrderReader orderReader;
    private final PageAssembler pageAssembler;
    private final StoreSummaryLoader storeSummaryLoader;
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public OrderResponseDto createOrder(Long userId, Long storeId, LocalDateTime expectedDeliveryTime,
                             String requestNote, List<OrderItem> orderItems) {

        Order order = orderFactory.create(userId, storeId, expectedDeliveryTime, requestNote, orderItems);
        order = orderRepository.save(order);
        return getOrderResponseDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponse<OrderSummaryDto, OrderCursorResponseDto> getOrders(
            Long userId,
            OrderCursorRequestDto cursor
    ) {
        List<Order> orders = orderReader.getOrdersByUserIdWithCursor(userId, cursor);
        int pageSize = cursor.pageSize();

        List<Long> storeIds = orders.stream()
                .map(Order::getStoreId)
                .distinct()
                .toList();

        Map<Long, StoreSummaryResponseDto> storeSummaryMap = storeSummaryLoader.loadWithCacheFallback(storeIds);

        return pageAssembler.assemble(
                orders,
                pageSize,
                last -> new OrderCursorResponseDto(pageSize, last.getCreatedAt(), last.getId()),
                order -> {
                    StoreSummaryResponseDto summary = storeSummaryMap.get(order.getStoreId());
                    return OrderSummaryDto.from(order, summary);
                }
        );
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(Long userId, UserRole role, Long orderId) {
        Order order = orderReader.getOrderWithPermission(orderId, userId, role);
        return getOrderResponseDto(order);
    }

    @Override
    public SliceResponse<OrderSummaryDto, OrderCursorResponseDto> getOwnerOrders(
            Long userId, UserRole role, OrderCursorRequestDto cursor, Long storeId) {
        List<Order> orders = orderReader.getOrdersByStoreIdWithCursor(storeId, cursor);

        Store store = storeRepository.findByIdWithNotDeleted(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));
        store.validateAccessibleTo(userId, role);

        int pageSize = cursor.pageSize();

        return pageAssembler.assemble(
                orders,
                pageSize,
                last -> new OrderCursorResponseDto(pageSize, last.getCreatedAt(), last.getId()),
                order -> {
                    return OrderSummaryDto.from(order, StoreSummaryResponseDto.from(store));
                }
        );
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOwnerOrder(Long userId, UserRole role, Long orderId) {
        Order order = orderReader.getOwnerOrderWithPermission(orderId, userId, role);
        return getOrderResponseDto(order);
    }

    private OrderResponseDto getOrderResponseDto(Order order) {
        StoreSummaryResponseDto storeSummary = storeSummaryLoader
                .loadWithCacheFallback(List.of(order.getStoreId()))
                .get(order.getStoreId());

        return OrderResponseDto.from(order, storeSummary);
    }
}
