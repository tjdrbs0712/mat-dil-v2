package hello.matdil.domain.review.reviewreply.repository;

import hello.matdil.domain.review.reviewreply.entity.ReviewReply;

import java.util.Optional;

public interface ReviewReplyQueryRepository {
    Optional<ReviewReply> findByIdAndOwnerWithReviewAndIsDeletedFalse(Long replyId, Long ownerId);
}
