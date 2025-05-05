package hello.matdil.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long storeId;

    @ElementCollection
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private int totalPrice;

    @Column(length = 500)
    private String requestNote;

    @Column(nullable = false)
    private LocalDateTime expectedDeliveryTime;

    @Column(nullable = false)
    private boolean isPaid;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Order(Long userId, Long storeId, List<OrderItem> orderItems, OrderStatus orderStatus,
                 int totalPrice, String requestNote, LocalDateTime expectedDeliveryTime) {
        this.userId = userId;
        this.storeId = storeId;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.requestNote = requestNote;
        this.expectedDeliveryTime = expectedDeliveryTime;
        this.isPaid = false;
        this.createdAt = LocalDateTime.now();
    }
}