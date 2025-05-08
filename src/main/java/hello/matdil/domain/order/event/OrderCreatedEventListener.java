package hello.matdil.domain.order.event;

import hello.matdil.domain.delivery.service.DeliveryService;
import hello.matdil.domain.user.entity.User;
import hello.matdil.domain.user.exception.UserErrorCode;
import hello.matdil.domain.user.exception.UserException;
import hello.matdil.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderCreatedEventListener {
    private final UserRepository userRepository;
    private final DeliveryService deliveryService;

//    @EventListener
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderCreateEvent event) {
        User user = userRepository.findById(event.userId())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        deliveryService.createDelivery(event.orderId(), user.getAddress());
    }
}
