package hello.matdil.domain.order.service;

import hello.matdil.domain.order.dto.OrderCursorRequestDto;
import hello.matdil.domain.order.dto.OrderCursorResponseDto;
import hello.matdil.domain.order.dto.OrderSummaryDto;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.entity.OrderStatus;
import hello.matdil.domain.order.factory.OrderFactory;
import hello.matdil.domain.order.reader.OrderReader;
import hello.matdil.domain.order.repository.OrderRepository;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.domain.store.reader.StoreSummaryLoader;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.util.pagination.PageAssembler;
import hello.matdil.test.TestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderFactory orderFactory;

    @Mock
    private OrderReader orderReader;

    @Mock
    private StoreSummaryLoader storeSummaryLoader;

    @Mock
    private PageAssembler pageAssembler;

    @Test
    void 주문_생성에_성공() {
        // given
        Order order = TestData.setOrder();

        given(orderFactory.create(
                order.getUserId(),
                order.getStoreId(),
                order.getExpectedDeliveryTime(),
                order.getRequestNote(),
                order.getOrderItems()))
                .willReturn(order);
        given(orderRepository.save(order)).willReturn(order);

        // when
        Order result = orderService.createOrder(order.getUserId(),
                order.getStoreId(),
                order.getExpectedDeliveryTime(),
                order.getRequestNote(),
                order.getOrderItems());

        // then
        assertThat(result.getUserId()).isEqualTo(order.getUserId());
        assertThat(result.getStoreId()).isEqualTo(order.getStoreId());
        assertThat(result.getOrderItems()).hasSize(2);
        assertThat(result.getTotalPrice()).isEqualTo(25000);
    }

    @Test
    void 주문_목록_조회에_성공() {
        // given
        Long userId = 1L;
        OrderCursorRequestDto cursor = new OrderCursorRequestDto(10, null, null);

        // 가짜 주문 목록
        Order order1 = Order.builder()
                .userId(userId)
                .storeId(1L)
                .orderStatus(OrderStatus.COMPLETED)
                .orderItems(List.of())
                .expectedDeliveryTime(LocalDateTime.now().plusHours(1))
                .requestNote("note")
                .build();
        order1.setTotalPrice(15000);

        Order order2 = Order.builder()
                .userId(userId)
                .storeId(2L)
                .orderStatus(OrderStatus.COMPLETED)
                .orderItems(List.of())
                .expectedDeliveryTime(LocalDateTime.now().plusHours(2))
                .requestNote("note")
                .build();
        order2.setTotalPrice(25000);

        List<Order> orders = List.of(order1, order2);

        // 가게 요약 정보
        Map<Long, StoreSummaryResponseDto> storeSummaryMap = Map.of(
                1L, new StoreSummaryResponseDto(),
                2L, new StoreSummaryResponseDto()
        );

        // 기대하는 응답 DTO
        SliceResponse<OrderSummaryDto, OrderCursorResponseDto> expectedResponse =
                SliceResponse.of(
                        List.of(
                                OrderSummaryDto.from(order1, storeSummaryMap.get(1L)),
                                OrderSummaryDto.from(order2, storeSummaryMap.get(2L))
                        ),
                        false,
                        null
                );

        // stubbing
        given(orderReader.getOrdersByUserIdWithCursor(userId, cursor)).willReturn(orders);
        given(storeSummaryLoader.loadWithCacheFallback(List.of(1L, 2L))).willReturn(storeSummaryMap);
        given(pageAssembler.assemble(
                any(),
                anyInt(),
                any(Function.class),
                any(Function.class)))
                .willReturn(expectedResponse);

        // when
        SliceResponse<OrderSummaryDto, OrderCursorResponseDto> result = orderService.getOrders(userId, cursor);

        // then
        assertThat(result.getContent()).hasSize(2);

        verify(orderReader).getOrdersByUserIdWithCursor(userId, cursor);
        verify(storeSummaryLoader).loadWithCacheFallback(List.of(1L, 2L));
        verify(pageAssembler).assemble(any(), anyInt(), any(Function.class), any(Function.class));
    }
}
