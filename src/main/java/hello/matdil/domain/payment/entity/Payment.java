package hello.matdil.domain.payment.entity;

import hello.matdil.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payments")
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private boolean isPaid;

    @Builder
    public Payment(Long orderId, PaymentMethod method, int amount, boolean isPaid) {
        this.orderId = orderId;
        this.method = method;
        this.amount = amount;
        this.isPaid = isPaid;
    }
}