package hello.matdil.domain.order.listener;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.delivery.entity.Delivery;
import hello.matdil.domain.delivery.repository.DeliveryRepository;
import hello.matdil.domain.order.dto.OrderCreateDto;
import hello.matdil.domain.order.dto.OrderItemCreateDto;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.repository.OrderRepository;
import hello.matdil.domain.order.service.OrderService;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Rollback
class OrderDeliveryIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 주문생성_후_배달이_생성() throws Exception {
        // given
        String uuid = UUID.randomUUID().toString();
        String email = uuid + "@test.com";
        String phoneNumber = "010" + uuid.substring(0, 8); // 길이 제한 고려

        Address address = new Address("서울", "101동", "010101");
        User user = User.builder()
                .role(UserRole.USER)
                .email(email)
                .password("password")
                .name("테스트 유저")
                .address(address)
                .phoneNumber(phoneNumber)
                .build();
        user.verifyEmail(); // 인증된 유저
        userRepository.save(user);


        OrderCreateDto dto = new OrderCreateDto(
                user.getId(),
                1L,
                List.of(new OrderItemCreateDto(1L, 1, 5000)),
                "부재시 연락주세요",
                LocalDateTime.now().plusHours(1)
        );

        // when
        orderService.createOrder(dto);

        // then
        // 비동기 이벤트 기다리기
        Thread.sleep(1000);

        Order savedOrder = orderRepository.findAll().get(0);
        List<Delivery> deliveries = deliveryRepository.findAll();

        assertThat(deliveries)
                .anyMatch(delivery -> delivery.getOrderId().equals(savedOrder.getId()));
    }
}
