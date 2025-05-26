package hello.matdil.domain.order.policy;

import hello.matdil.domain.order.entity.OrderStatus;
import hello.matdil.domain.order.exception.OrderErrorCode;
import hello.matdil.domain.order.exception.OrderException;
import hello.matdil.domain.user.entity.UserRole;
import org.springframework.stereotype.Component;

@Component
public class DefaultOrderStatusChangePolicy implements OrderStatusChangePolicy {

    @Override
    public void validateChange(OrderStatus currentStatus, OrderStatus newStatus, UserRole role) {
        if (currentStatus == newStatus) {
            throw new OrderException(OrderErrorCode.ALREADY_IN_TARGET_STATUS);
        }

        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new OrderException(OrderErrorCode.INVALID_STATUS);
        }

        switch (newStatus) {
            case DELETED -> {
                if (role != UserRole.ADMIN) {
                    throw new OrderException(OrderErrorCode.NO_PERMISSION);
                }
            }
            case CANCELED -> {
                if (role == UserRole.USER && !currentStatus.canBeCanceledByUser()) {
                    throw new OrderException(OrderErrorCode.NO_PERMISSION);
                }
                if (role == UserRole.OWNER && !currentStatus.canBeCanceledByOwner()) {
                    throw new OrderException(OrderErrorCode.NO_PERMISSION);
                }
            }
            default -> {
                if (role == UserRole.USER) {
                    throw new OrderException(OrderErrorCode.NO_PERMISSION);
                }
            }
        }
    }
}
