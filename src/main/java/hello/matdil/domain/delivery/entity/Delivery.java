package hello.matdil.domain.delivery.entity;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "deliveries")
public class Delivery extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus deliveryStatus;

    private LocalDateTime assignedTime;

    @Builder
    public Delivery(Long orderId, Address address, DeliveryStatus deliveryStatus, LocalDateTime assignedTime) {
        this.orderId = orderId;
        this.address = address;
        this.deliveryStatus = deliveryStatus;
        this.assignedTime = assignedTime;
    }
}