package hello.matdil.domain.coupon.entity;

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
@Table(
        name = "coupons",
        uniqueConstraints = @UniqueConstraint(name = "UK_coupon_code", columnNames = "code")
)
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private int discountAmount;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Builder
    public Coupon(Long userId, String code, int discountAmount, LocalDateTime expiresAt) {
        this.userId = userId;
        this.code = code;
        this.discountAmount = discountAmount;
        this.expiresAt = expiresAt;
    }
}