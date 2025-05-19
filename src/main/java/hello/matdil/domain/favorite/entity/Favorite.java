package hello.matdil.domain.favorite.entity;

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
@Table(name = "favorites")
public class Favorite extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Favorite(Long userId, Long storeId){
        this.userId = userId;
        this.storeId = storeId;
        this.createdAt = LocalDateTime.now();
    }
}