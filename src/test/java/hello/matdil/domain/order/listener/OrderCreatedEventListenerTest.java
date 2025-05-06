package hello.matdil.domain.order.listener;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.delivery.service.DeliveryService;
import hello.matdil.domain.order.event.OrderCreateEvent;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderCreatedEventListenerTest {

    @InjectMocks
    private OrderCreatedEventListener eventListener;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeliveryService deliveryService;

    @Test
    @DisplayName("OrderCreatedEvent를 수신하면 유저 주소를 기반으로 배달을 생성한다")
    void 배달_생성() {
        // given
        Long userId = 1L;
        Long orderId = 10L;
        Address address = new Address("서울시", "101동", "010101");
        User user = mock(User.class);

        when(user.getAddress()).thenReturn(address);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        OrderCreateEvent event = new OrderCreateEvent(orderId, userId);

        // when
        eventListener.handle(event);

        // then
        verify(userRepository).findById(userId);
        verify(deliveryService).createDelivery(orderId, address);
    }
}
