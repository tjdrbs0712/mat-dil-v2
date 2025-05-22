//package hello.matdil.domain.order.service;
//
//import hello.matdil.domain.order.dto.OrderCreateDto;
//import hello.matdil.domain.order.dto.OrderItemCreateDto;
//import hello.matdil.domain.order.entity.Order;
//import hello.matdil.domain.order.event.OrderCreateEvent;
//import hello.matdil.domain.order.repository.OrderRepository;
//import hello.matdil.event.GenericEventPublisher;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//
//@ExtendWith(MockitoExtension.class)
//class
//OrderServiceTest {
//
//    @InjectMocks
//    private OrderService orderService;
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private GenericEventPublisher eventPublisher;
//
//    @Test
//    void 주문_생성() {
//        // given
//        OrderCreateDto orderCreateDto = new OrderCreateDto(
//                1L, // userId
//                10L, // storeId
//                List.of(new OrderItemCreateDto(100L, 2, 10000)),
//                "문 앞에 놔주세요",
//                LocalDateTime.now().plusMinutes(30)
//        );
//
//        // when
//        orderService.createOrder(orderCreateDto);
//
//        // then
//        verify(orderRepository).save(any(Order.class));
//        verify(eventPublisher).publish(any(OrderCreateEvent.class));
//    }
//
//}