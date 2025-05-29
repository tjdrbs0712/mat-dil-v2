package hello.matdil.domain.order.facade;

import hello.matdil.domain.order.dto.OrderCreateRequestDto;
import hello.matdil.domain.order.dto.OrderItemRequestDto;
import hello.matdil.domain.order.dto.OrderResponseDto;
import hello.matdil.domain.order.entity.OrderItem;
import hello.matdil.domain.order.service.OrderCreateProcessor;
import hello.matdil.domain.order.service.OrderService;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.reader.StoreReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class OrderFacadeTest {

    @InjectMocks
    private OrderFacade orderFacade;

    @Mock
    private StoreReader storeReader;

    @Mock
    private OrderCreateProcessor orderCreateProcessor;

    @Mock
    private OrderService orderService;

    @Test
    void 주문_생성_퍼싸드_흐름_성공() {
        Long userId = 1L;
        Long storeId = 10L;
        Store store = mock(Store.class);
        OrderResponseDto responseDto = mock(OrderResponseDto.class);
        OrderItem orderItem = mock(OrderItem.class);

        OrderItemRequestDto OrderItemRequestDto = new OrderItemRequestDto();
        ReflectionTestUtils.setField(OrderItemRequestDto, "menuId", 1L);
        ReflectionTestUtils.setField(OrderItemRequestDto, "quantity", 2);

        OrderCreateRequestDto requestDto = new OrderCreateRequestDto();
        ReflectionTestUtils.setField(requestDto, "storeId", storeId);
        ReflectionTestUtils.setField(requestDto, "expectedDeliveryTime", LocalDateTime.now().plusHours(1));
        ReflectionTestUtils.setField(requestDto, "requestNote", "조심히 와주세요");
        ReflectionTestUtils.setField(requestDto, "orderItems", List.of(OrderItemRequestDto));

        given(store.getId()).willReturn(storeId);
        given(storeReader.readWithOpen(storeId)).willReturn(store);
        given(orderCreateProcessor.toOrderItems(
                requestDto.getOrderItems(), requestDto.getStoreId())).willReturn(List.of(orderItem));
        given(orderService.createOrder(any(), any(), any(), any(), any())).willReturn(responseDto);

        // when
        OrderResponseDto result = orderFacade.createOrder(userId, requestDto);

        // then
        assertThat(result).isNotNull();
        verify(storeReader).readWithOpen(storeId);
        verify(orderCreateProcessor).toOrderItems(requestDto.getOrderItems(), storeId);
        verify(orderService).createOrder(eq(userId), eq(storeId), any(), any(), any());

    }
}