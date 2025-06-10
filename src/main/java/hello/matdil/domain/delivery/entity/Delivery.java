package hello.matdil.domain.delivery.entity;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "deliveries", indexes = {
        @Index(name = "idx_delivery_order_id", columnList = "orderId", unique = true),
        @Index(name = "idx_delivery_rider_id", columnList = "riderId"),
        @Index(name = "idx_delivery_status", columnList = "deliveryStatus")
})
public class Delivery extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column
    private Long riderId;

    @Embedded
    private Address address;

    @Column(nullable = false)
    private BigDecimal deliveryFee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column
    private LocalDateTime assignedTime;

    @Column
    private LocalDateTime pickedUpAt;

    @Column
    private LocalDateTime deliveredAt;


    @Builder
    public Delivery(Long orderId, Long riderId, Address address, BigDecimal deliveryFee, DeliveryStatus deliveryStatus, LocalDateTime assignedTime, LocalDateTime pickedUpAt, LocalDateTime deliveredAt) {
        this.orderId = orderId;
        this.riderId = riderId;
        this.address = address;
        this.deliveryFee = deliveryFee;
        this.deliveryStatus = deliveryStatus;
        this.assignedTime = assignedTime;
        this.pickedUpAt = pickedUpAt;
        this.deliveredAt = deliveredAt;
    }

    public void assignRider(Long riderId) {
        this.riderId = riderId;
        this.assignedTime = LocalDateTime.now();
    }

    public void markAsPickedUp() {
        this.deliveryStatus = DeliveryStatus.PICKED_UP;
        this.pickedUpAt = LocalDateTime.now();
    }
}