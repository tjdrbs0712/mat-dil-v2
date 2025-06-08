package hello.matdil.domain.review.reviewreply.facade;

import hello.matdil.domain.review.entity.Review;
import hello.matdil.domain.review.reader.ReviewReader;
import hello.matdil.domain.review.reviewreply.dto.ReviewReplyCreateRequestDto;
import hello.matdil.domain.review.reviewreply.dto.ReviewReplyResponseDto;
import hello.matdil.domain.review.reviewreply.dto.ReviewReplyUpdateRequestDto;
import hello.matdil.domain.review.reviewreply.entity.ReviewReply;
import hello.matdil.domain.review.reviewreply.reader.ReviewReplyReader;
import hello.matdil.domain.review.reviewreply.service.ReviewReplyService;
import hello.matdil.domain.store.validator.StoreValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewReplyFacade {

    private final ReviewReader reviewReader;
    private final ReviewReplyService reviewReplyService;
    private final ReviewReplyReader reviewReplyReader;
    private final StoreValidator storeValidator;

    public ReviewReplyResponseDto createReply(Long ownerId, Long reviewId, ReviewReplyCreateRequestDto dto) {
        Review review = reviewReader.getReviewByIdAndDeletedFalse(reviewId);
        storeValidator.validateOwnerOf(review.getStoreId(), ownerId);
        ReviewReply reply = reviewReplyService.createReply(ownerId, review, dto.replyText());

        return ReviewReplyResponseDto.from(reply);
    }

    public ReviewReplyResponseDto updateReply(Long ownerId, Long replyId, ReviewReplyUpdateRequestDto dto) {
        ReviewReply reviewReply = reviewReplyReader.getDeletableReplyForOwner(replyId, ownerId);
        Long storeId = reviewReply.getReview().getStoreId();
        storeValidator.validateOwnerOf(storeId, ownerId);
        ReviewReply updateReply = reviewReplyService.updateReply(storeId, reviewReply, dto.replyText());

        return ReviewReplyResponseDto.from(updateReply);
    }

    public void deleteReply(Long ownerId, Long replyId) {
        ReviewReply reviewReply = reviewReplyReader.getDeletableReplyForOwner(replyId, ownerId);
        Long storeId = reviewReply.getReview().getStoreId();
        storeValidator.validateOwnerOf(storeId, ownerId);
        reviewReplyService.deleteReply(storeId, reviewReply);
    }
}
