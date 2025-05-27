package hello.matdil.domain.userorderstatus.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_order_stats")
public class UserOrderStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private int orderCount = 0;

    private LocalDateTime lastOrderedAt;

    @Builder
    private UserOrderStats(Long userId, Long storeId, int orderCount, LocalDateTime lastOrderedAt) {
        this.userId = userId;
        this.storeId = storeId;
        this.orderCount = orderCount;
        this.lastOrderedAt = lastOrderedAt;
    }

    public static UserOrderStats create(Long userId, Long storeId, LocalDateTime orderedAt){
        return UserOrderStats.builder()
                .userId(userId)
                .storeId(storeId)
                .lastOrderedAt(orderedAt)
                .build();
    }

    public void incrementOrderCount() {
        this.orderCount += 1;
        this.lastOrderedAt = LocalDateTime.now();
    }

    public void updateOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public void updateOrderedAt(LocalDateTime lastOrderedAt) {
        this.lastOrderedAt = lastOrderedAt;
    }
}