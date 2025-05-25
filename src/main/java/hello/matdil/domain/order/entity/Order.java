package hello.matdil.domain.order.entity;

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
                  LocalDateTime expectedDeliveryTime) {
        this.userId = userId;
        this.storeId = storeId;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.requestNote = requestNote;
        this.expectedDeliveryTime = expectedDeliveryTime;
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

}
