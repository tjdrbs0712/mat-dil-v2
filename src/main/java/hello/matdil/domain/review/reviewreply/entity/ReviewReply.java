package hello.matdil.domain.review.reviewreply.entity;

import hello.matdil.domain.common.BaseTimeEntity;
import hello.matdil.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review_replies")
public class ReviewReply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id", nullable = false, unique = true)
    private Review review;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false, length = 1000)
    private String replyText;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @Builder
    public ReviewReply(Review review, Long ownerId, String replyText) {
        this.review = review;
        this.ownerId = ownerId;
        this.replyText = replyText;
    }

    public void updateReplyText(String newText) {
        this.replyText = newText;
    }

    public void markAsDeleted() {
        this.isDeleted = true;
    }

    public boolean isOwner(Long userId) {
        return this.ownerId.equals(userId);
    }
}