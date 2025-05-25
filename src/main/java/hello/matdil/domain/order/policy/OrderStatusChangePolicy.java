package hello.matdil.domain.order.policy;

import hello.matdil.domain.order.entity.OrderStatus;
import hello.matdil.domain.user.entity.UserRole;

public interface OrderStatusChangePolicy {
    void validateChange(OrderStatus currentStatus, OrderStatus newStatus, UserRole role);
}
