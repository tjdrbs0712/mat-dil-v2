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
@Table(name = "review_replies")
public class ReviewReply extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long reviewId;

    @Column(nullable = false)
    private String replyText;

    @Builder
    public ReviewReply(Long reviewId, String replyText) {
        this.reviewId = reviewId;
        this.replyText = replyText;
    }
}