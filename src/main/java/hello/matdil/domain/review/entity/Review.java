package hello.matdil.domain.review.entity;

import hello.matdil.domain.common.BaseTimeEntity;
import hello.matdil.domain.review.exception.ReviewErrorCode;
import hello.matdil.domain.review.exception.ReviewException;
import hello.matdil.domain.user.entity.UserRole;
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

    public void updateReview(int rating, String comment, List<String> imageUrls) {
        this.rating = rating;
        this.comment = comment;
        this.images.clear();

        List<ReviewImage> newImages = imageUrls.stream()
                .map(url -> ReviewImage.create(url, this))
                .toList();
        this.images.addAll(newImages);
    }

    public void clearImages() {
        this.images.clear();
    }

    public void validateAccessibleTo(Long userId, UserRole role){
        boolean isAdmin = role == UserRole.ADMIN;
        boolean isUser = this.userId.equals(userId);

        if (!(isAdmin || isUser)) {
            throw new ReviewException(ReviewErrorCode.NO_PERMISSION);
        }
    }

    public void validateIsDeleted(){
        if(this.isDeleted){
            throw new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND);
        }
    }

    public void isDeleted(){
        this.isDeleted = true;
    }

}