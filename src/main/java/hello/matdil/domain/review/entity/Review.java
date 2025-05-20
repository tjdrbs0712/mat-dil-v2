package hello.matdil.domain.review.entity;

import hello.matdil.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "reviews")
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private int rating;

    @Column
    private String comment;

    @Builder
    public Review(Long userId, Long orderId, Long storeId, int rating, String comment) {
        this.userId = userId;
        this.orderId = orderId;
        this.storeId = storeId;
        this.rating = rating;
        this.comment = comment;
    }
}