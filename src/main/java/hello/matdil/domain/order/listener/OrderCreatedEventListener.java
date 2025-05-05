package hello.matdil.domain.order.listener;

import hello.matdil.domain.delivery.service.DeliveryService;
import hello.matdil.domain.order.event.OrderCreateEvent;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.entity.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCreatedEventListener {
    private final UserRepository userRepository;
    private final DeliveryService deliveryService;

    @EventListener
    public void handle(OrderCreateEvent event) {
        User user = userRepository.findById(event.userId())
                .orElseThrow(() -> new EntityNotFoundException("사용자 없음"));

        deliveryService.createDelivery(event.orderId(), user.getAddress());
    }
}
