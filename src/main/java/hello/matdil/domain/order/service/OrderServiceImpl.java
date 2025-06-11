package hello.matdil.domain.order.service;

import hello.matdil.domain.order.dto.*;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.order.entity.OrderStatus;
import hello.matdil.domain.order.event.OrderEventProducer;
import hello.matdil.domain.order.factory.OrderFactory;
import hello.matdil.domain.order.policy.OrderStatusChangePolicy;
import hello.matdil.domain.order.reader.OrderReader;
import hello.matdil.domain.order.repository.OrderRepository;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.reader.StoreReader;
import hello.matdil.domain.store.reader.StoreSummaryLoader;
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
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;
    private final OrderReader orderReader;
    private final StoreReader storeReader;
    private final PageAssembler pageAssembler;
    private final StoreSummaryLoader storeSummaryLoader;
    private final OrderStatusChangePolicy policy;

    private final OrderEventProducer orderEventProducer;
    private final OrderCacheService orderCacheService;

    @Override
    @Transactional
    public OrderResponseDto createOrder(Long userId, Long storeId, LocalDateTime expectedDeliveryTime,
                                        String requestNote, List<OrderItem> orderItems,
                                        OrderCreateRequestDto.AddressDto address) {
        Order order = orderFactory.create(userId, storeId, expectedDeliveryTime, requestNote, orderItems, address);
        Order savedOrder = orderRepository.save(order);

        orderEventProducer.sendOrderCreatedEvent(savedOrder);

        return buildOrderResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponse<OrderSummaryDto, OrderCursorResponseDto> getUserOrders(Long userId, OrderCursorRequestDto cursor) {
        List<Order> orders = orderReader.readByUserWithCursor(userId, cursor);
        Map<Long, StoreSummaryResponseDto> storeSummaries = loadStoreSummaries(orders);

        return pageAssembler.assemble(
                orders,
                cursor.pageSize(),
                last -> new OrderCursorResponseDto(cursor.pageSize(), last.getCreatedAt(), last.getId()),
                order -> OrderSummaryDto.from(order, storeSummaries.get(order.getStoreId()))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponse<OrderSummaryDto, OrderCursorResponseDto> getStoreOwnerOrders(
            Long userId, UserRole role, OrderCursorRequestDto cursor, Long storeId) {
        Store store = storeReader.readWithNotDeletedWithPermission(userId, storeId, role);
        List<Order> orders = orderReader.readByStoreWithCursor(storeId, cursor);

        return pageAssembler.assemble(
                orders,
                cursor.pageSize(),
                last -> new OrderCursorResponseDto(cursor.pageSize(), last.getCreatedAt(), last.getId()),
                order -> OrderSummaryDto.from(order, StoreSummaryResponseDto.from(store))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getUserOrder(Long userId, UserRole role, Long orderId) {
        Order order = orderReader.readWithUserPermission(orderId, userId, role);
        return buildOrderResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getStoreOwnerOrder(Long userId, UserRole role, Long orderId) {
        Order order = orderReader.readWithStorePermission(orderId, userId, role);
        return buildOrderResponse(order);
    }

    @Override
    @Transactional
    public void changeStoreOwnerOrderStatus(Long userId, UserRole role, Long orderId, OrderStatus newStatus) {
        Order order = orderReader.readWithStorePermission(orderId, userId, role);
        policy.validateChange(order.getOrderStatus(), newStatus, role);
        order.changeStatus(newStatus);
        if (newStatus == OrderStatus.READY) {
            orderEventProducer.sendOrderReadyForDispatch(order);
        }
    }

    @Override
    @Transactional
    public void changeUserOrderStatus(Long userId, UserRole role, Long orderId, OrderStatus newStatus) {
        Order order = orderReader.readWithUserPermission(orderId, userId, role);
        policy.validateChange(order.getOrderStatus(), newStatus, role);
        order.changeStatus(newStatus);
        orderCacheService.evict(order.getId());
    }

    @Override
    @Transactional
    public void updateOrderStatusToPaid(Long orderId) {
        Order order = orderReader.readById(orderId);
        order.markAsPaid();
    }

    private OrderResponseDto buildOrderResponse(Order order) {
        StoreSummaryResponseDto storeSummary = storeSummaryLoader
                .loadWithCacheFallback(List.of(order.getStoreId()))
                .get(order.getStoreId());
        orderCacheService.put(order, storeSummary);
        return OrderResponseDto.from(order, storeSummary);
    }

    private Map<Long, StoreSummaryResponseDto> loadStoreSummaries(List<Order> orders) {
        List<Long> storeIds = orders.stream()
                .map(Order::getStoreId)
                .distinct()
                .toList();
        return storeSummaryLoader.loadWithCacheFallback(storeIds);
    }
}
