package hello.matdil.domain.order.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import hello.matdil.domain.order.exception.OrderErrorCode;
import hello.matdil.domain.order.exception.OrderException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Getter
@Slf4j
public enum OrderStatus {
    CREATED,
    ACCEPTED,
    COOKING,
    READY,
    DELIVERING,
    COMPLETED,
    CANCELED,
    DELETED;

    private Set<OrderStatus> next;

    static {
        CREATED.next = Set.of(ACCEPTED, CANCELED, DELETED);
        ACCEPTED.next = Set.of(COOKING, CANCELED, DELETED);
        COOKING.next = Set.of(READY, DELETED);
        READY.next = Set.of(DELIVERING, DELETED);
        DELIVERING.next = Set.of(COMPLETED, DELETED);
        COMPLETED.next = Set.of(DELETED);
        CANCELED.next = Set.of(DELETED);
        DELETED.next = Set.of();
    }

    public boolean canTransitionTo(OrderStatus nextStatus) {
        return next.contains(nextStatus);
    }

    public boolean canBeCanceledByUser() {
        return this == CREATED;
    }

    public boolean canBeCanceledByOwner() {
        return this == CREATED || this == ACCEPTED;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OrderStatus from(String input) {
        try {
            return OrderStatus.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new OrderException(OrderErrorCode.INVALID_STATUS);
        }
    }
}