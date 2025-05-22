package hello.matdil.domain.order.service;

import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.order.entity.OrderStatus;
import hello.matdil.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Test
    void 주문_생성에_성공() {
        // given
        Long userId = 1L;
        Long storeId = 1L;
        String note = "문 앞에 놓아주세요";
        LocalDateTime deliveryTime = LocalDateTime.now().plusHours(1);

        List<OrderItem> orderItems = List.of(
                OrderItem.builder().menuId(1L).price(10000 * 2).quantity(2).build(),
                OrderItem.builder().menuId(2L).price(5000).quantity(1).build()
        );

        Order expectedOrder = Order.create(userId, storeId, deliveryTime, note, orderItems);

        given(orderRepository.save(any(Order.class))).willReturn(expectedOrder);

        // when
        Order order = orderService.createOrder(userId, storeId, deliveryTime, note, orderItems);

        // then
        assertThat(order.getUserId()).isEqualTo(userId);
        assertThat(order.getStoreId()).isEqualTo(storeId);
        assertThat(order.getOrderItems()).hasSize(2);
        assertThat(order.getTotalPrice()).isEqualTo(25000);
    }
}
