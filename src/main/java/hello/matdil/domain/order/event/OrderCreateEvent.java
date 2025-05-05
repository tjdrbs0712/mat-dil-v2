package hello.matdil.domain.order.event;

public record OrderCreateEvent(Long orderId, Long userId) {}