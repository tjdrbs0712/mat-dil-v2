package hello.matdil.domain.order.facade;

import hello.matdil.domain.order.dto.OrderCreateRequestDto;
import hello.matdil.domain.order.dto.OrderResponseDto;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.order.service.OrderService;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.store.menu.validator.MenuReader;
import hello.matdil.domain.store.validator.StoreReader;
import hello.matdil.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final OrderService orderService;
    private final StoreReader storeReader;
    private final MenuReader menuReader;

    public OrderResponseDto createOrder(Long userId, UserRole role, OrderCreateRequestDto dto) {
        Store store = storeReader.getStoreWithPermission(userId, dto.getStoreId(), role);

        // 메뉴 정보 조회 및 OrderItem 생성
        List<OrderItem> orderItems = dto.getOrderItems().stream()
                .map(itemDto -> {
                    Menu menu = menuReader.getMenuWithStoreValidation(itemDto.getMenuId(), dto.getStoreId());
                    return OrderItem.of(menu, itemDto.getQuantity()); // 메뉴 가격 포함
                })
                .toList();

        // 주문 생성
        Order order = orderService.createOrder(userId, store.getId(), dto.getExpectedDeliveryTime(),
                dto.getRequestNote(), orderItems);

        return OrderResponseDto.from(order);
    }
}