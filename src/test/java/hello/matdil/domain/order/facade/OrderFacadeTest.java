package hello.matdil.domain.order.facade;

import hello.matdil.domain.order.dto.OrderCreateRequestDto;
import hello.matdil.domain.order.dto.OrderItemRequestDto;
import hello.matdil.domain.order.dto.OrderResponseDto;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.service.OrderService;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.menu.entity.Menu;
import hello.matdil.domain.store.menu.reader.MenuReader;
import hello.matdil.domain.store.reader.StoreReader;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.test.TestData;
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
    private MenuReader menuReader;

    @Mock
    private OrderService orderService;

    @Test
    void 주문_생성_퍼싸드_흐름_성공() {
        Long userId = 1L;
        Long storeId = 10L;
        UserRole role = UserRole.USER;
        Menu menu = TestData.setUpMenu();
        Store store = mock(Store.class);
        Order order = mock(Order.class);

        OrderItemRequestDto OrderItemRequestDto = new OrderItemRequestDto();
        ReflectionTestUtils.setField(OrderItemRequestDto, "menuId", 1L);
        ReflectionTestUtils.setField(OrderItemRequestDto, "quantity", 2);

        OrderCreateRequestDto requestDto = new OrderCreateRequestDto();
        ReflectionTestUtils.setField(requestDto, "storeId", storeId);
        ReflectionTestUtils.setField(requestDto, "expectedDeliveryTime", LocalDateTime.now().plusHours(1));
        ReflectionTestUtils.setField(requestDto, "requestNote", "조심히 와주세요");
        ReflectionTestUtils.setField(requestDto, "orderItems", List.of(OrderItemRequestDto));

        given(store.getId()).willReturn(storeId);
        given(storeReader.getStoreWithPermission(userId, storeId, role)).willReturn(store);
        given(menuReader.getMenuWithStoreValidation(1L, storeId)).willReturn(menu);
        given(orderService.createOrder(any(), any(), any(), any(), any())).willReturn(order);

        // when
        OrderResponseDto response = orderFacade.createOrder(userId, role, requestDto);

        // then
        assertThat(response).isNotNull();
        verify(storeReader).getStoreWithPermission(userId, storeId, role);
        verify(menuReader).getMenuWithStoreValidation(1L, storeId);
        verify(orderService).createOrder(eq(userId), eq(storeId), any(), any(), any());

    }
}