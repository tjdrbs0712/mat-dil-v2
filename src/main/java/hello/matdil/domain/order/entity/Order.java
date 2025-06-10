package hello.matdil.domain.order.entity;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.common.BaseTimeEntity;
import hello.matdil.domain.order.exception.OrderErrorCode;
import hello.matdil.domain.order.exception.OrderException;
import hello.matdil.domain.user.entity.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long storeId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Embedded
    private Address deliveryAddress;

    @Setter
    @Column(nullable = false)
    private int totalPrice;

    @Column(length = 500)
    private String requestNote;

    @Column(nullable = false)
    private LocalDateTime expectedDeliveryTime;

    @Builder
    public Order(Long userId,
                 Long storeId,
                 List<OrderItem> orderItems,
                 OrderStatus orderStatus,
                 String requestNote,
                 LocalDateTime expectedDeliveryTime,
                 Address deliveryAddress) {
        this.userId = userId;
        this.storeId = storeId;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.requestNote = requestNote;
        this.expectedDeliveryTime = expectedDeliveryTime;
        this.deliveryAddress = deliveryAddress;
    }

    public void validateAccessibleTo(Long userId, UserRole role) {
        boolean isAdmin = role == UserRole.ADMIN;
        boolean isOwner = this.userId.equals(userId);

        if (!isAdmin && !isOwner) {
            throw new OrderException(OrderErrorCode.NO_PERMISSION);
        }
    }

    public void changeStatus(OrderStatus newStatus) {
        this.orderStatus = newStatus;
    }

    public void validateIsCompleted() {
        if (this.orderStatus != OrderStatus.COMPLETED) {
            throw new OrderException(OrderErrorCode.ORDER_NOT_COMPLETED);
        }
    }

    public void markAsPaid() {
        this.orderStatus = OrderStatus.PAID;
    }
}
