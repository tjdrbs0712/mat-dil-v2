package hello.matdil.domain.payment.entity;

import hello.matdil.domain.common.BaseTimeEntity;
import hello.matdil.domain.payment.exception.PaymentErrorCode;
import hello.matdil.domain.payment.exception.PaymentException;
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
    private Long userId;


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
    public Payment(Long userId, Long orderId, PaymentMethod method, BigDecimal amount) {
        this.userId = userId;
        this.orderId = orderId;
        this.method = method;
        this.amount = amount;
        this.status = PaymentStatus.READY;
    }

    public static Payment create(Long userId, Long orderId, PaymentMethod method, BigDecimal amount) {
        return Payment.builder()
                .userId(userId)
                .orderId(orderId)
                .method(method)
                .amount(amount)
                .build();
    }

    public void completePayment(String paymentKey) {
        this.paymentKey = paymentKey;
        this.status = PaymentStatus.COMPLETED;
        this.paidAt = LocalDateTime.now();
    }

    public boolean validateAmountCompare(BigDecimal amount) {
        return this.amount.compareTo(amount) != 0;
    }

    public void failPayment(String failReason) {
        this.status = PaymentStatus.FAILED;
        this.failReason = failReason;
    }

    public void markAsCanceled() {
        if (this.status == PaymentStatus.CANCELLED) {
            throw new PaymentException(PaymentErrorCode.ALREADY_CANCELLED);
        }
        this.status = PaymentStatus.CANCELLED;
    }
}
