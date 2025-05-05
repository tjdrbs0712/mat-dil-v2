package hello.matdil.domain.order.service;

import hello.matdil.domain.delivery.entity.Delivery;
import hello.matdil.domain.delivery.repository.DeliveryRepository;
import hello.matdil.domain.order.dto.OrderCreateDto;
import hello.matdil.domain.order.dto.OrderItemCreateDto;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Test
    @DisplayName("정상 주문 생성 시 Order와 Delivery가 함께 생성된다")
    void createOrder_success() {
        // given
        OrderCreateDto orderCreateDto = new OrderCreateDto(
                1L, // userId
                10L, // storeId
                List.of(new OrderItemCreateDto(100L, 2, 10000)),
                "문 앞에 놔주세요",
                LocalDateTime.now().plusMinutes(30)
        );

        // when
        orderService.createOrder(orderCreateDto);

        // then
        verify(orderRepository).save(any(Order.class));
        verify(deliveryRepository).save(any(Delivery.class));
    }

}