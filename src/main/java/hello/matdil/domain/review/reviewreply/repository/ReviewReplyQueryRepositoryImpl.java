package hello.matdil.domain.review.reviewreply.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.matdil.domain.review.entity.QReview;
import hello.matdil.domain.review.reviewreply.entity.QReviewReply;
import hello.matdil.domain.review.reviewreply.entity.ReviewReply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewReplyQueryRepositoryImpl implements ReviewReplyQueryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<ReviewReply> findByIdAndOwnerWithReviewAndIsDeletedFalse(Long id, Long ownerId) {

        QReview review = QReview.review;
        QReviewReply reviewReply = QReviewReply.reviewReply;

        ReviewReply result = jpaQueryFactory
                .selectFrom(reviewReply)
                .join(reviewReply.review, review).fetchJoin()
                .where(
                        reviewReply.id.eq(id),
                        reviewReply.ownerId.eq(ownerId),
                        reviewReply.isDeleted.isFalse()
                )
                .fetchOne();

        return Optional.ofNullable(result);
    }
}
