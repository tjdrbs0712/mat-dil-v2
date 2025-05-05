package hello.matdil.coupon.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
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