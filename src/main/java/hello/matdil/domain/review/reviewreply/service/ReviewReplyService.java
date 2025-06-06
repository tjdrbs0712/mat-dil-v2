package hello.matdil.domain.review.reviewreply.service;

import hello.matdil.domain.review.entity.Review;
import hello.matdil.domain.review.reviewreply.entity.ReviewReply;
import hello.matdil.domain.review.reviewreply.exception.ReviewReplyErrorCode;
import hello.matdil.domain.review.reviewreply.exception.ReviewReplyException;
import hello.matdil.domain.review.reviewreply.repository.ReviewReplyRepository;
import hello.matdil.domain.review.service.ReviewCacheService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewReplyService {

    private final ReviewReplyRepository reviewReplyRepository;
    private final ReviewCacheService reviewCacheService;

    @Transactional
    public ReviewReply createReply(Long ownerId, Review review, String replyText) {
        ReviewReply reviewReply = ReviewReply.create(ownerId, review, replyText);

        try {
            reviewReply = reviewReplyRepository.save(reviewReply);
        } catch (DataIntegrityViolationException e) {
            throw new ReviewReplyException(ReviewReplyErrorCode.ALREADY_REVIEW_REPLY);
        }

        reviewCacheService.deleteAll(review.getStoreId());
        return reviewReply;
    }

    @Transactional
    public ReviewReply updateReply(Long storeId, ReviewReply reviewReply, String replyText) {
        reviewReply.updateReplyText(replyText);
        reviewCacheService.deleteAll(storeId);
        return reviewReply;
    }
}
