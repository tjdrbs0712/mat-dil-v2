package hello.matdil.domain.review.entity;

import hello.matdil.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "reviews",
        uniqueConstraints = @UniqueConstraint(name = "UK_review_user_store", columnNames = {"user_id", "store_id"})
)
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

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private int rating;

    @Column(length = 1000)
    private String comment;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> images = new ArrayList<>();

    @Builder
    public Review(Long userId, Long orderId, Long storeId, int rating, String comment) {
        this.userId = userId;
        this.orderId = orderId;
        this.storeId = storeId;
        this.rating = rating;
        this.comment = comment;
    }

}