package hello.matdil.domain.favorite.entity;

import hello.matdil.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "favorites",
        uniqueConstraints = @UniqueConstraint(name = "UK_favorite_user_store", columnNames = {"user_id", "store_id"}),
        indexes = {
                @Index(name = "idx_favorite_user_id", columnList = "user_id"),
                @Index(name = "idx_favorite_store_id", columnList = "store_id")
        }
)
public class Favorite extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long storeId;

    @Builder
    public Favorite(Long userId, Long storeId){
        this.userId = userId;
        this.storeId = storeId;
    }

    public static Favorite create(Long userId, Long storeId) {
        return Favorite.builder()
                .userId(userId)
                .storeId(storeId)
                .build();
    }
}