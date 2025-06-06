package hello.matdil.domain.review.reviewreply.reader;

import hello.matdil.domain.review.reviewreply.entity.ReviewReply;
import hello.matdil.domain.review.reviewreply.exception.ReviewReplyErrorCode;
import hello.matdil.domain.review.reviewreply.exception.ReviewReplyException;
import hello.matdil.domain.review.reviewreply.repository.ReviewReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReviewReplyReader {

    private final ReviewReplyRepository reviewReplyRepository;

    @Transactional(readOnly = true)
    public ReviewReply getFindByIdAndOwnerWithReviewAndIsDeletedFalse (Long reviewId, Long ownerId){
        return reviewReplyRepository.findByIdAndOwnerWithReviewAndIsDeletedFalse(reviewId, ownerId)
                .orElseThrow(() -> new ReviewReplyException(ReviewReplyErrorCode.REVIEW_REPLY_NOT_FOUND));
    }

}
