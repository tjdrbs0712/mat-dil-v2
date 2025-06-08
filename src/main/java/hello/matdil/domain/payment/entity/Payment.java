package hello.matdil.domain.payment.entity;

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
@Table(name = "payments")
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(unique = true)
    private String paymentKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private LocalDateTime paidAt;

    private String failReason;


    @Builder
    public Payment(Long orderId, PaymentMethod method, BigDecimal amount) {
        this.orderId = orderId;
        this.method = method;
        this.amount = amount;
        this.status = PaymentStatus.READY;
    }

    public void completePayment(String paymentKey) {
        this.paymentKey = paymentKey;
        this.status = PaymentStatus.COMPLETED;
        this.paidAt = LocalDateTime.now();
    }

    public void failPayment(String failReason) {
        this.status = PaymentStatus.FAILED;
        this.failReason = failReason;
    }
}
