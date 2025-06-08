package hello.matdil.domain.review.reviewreply.facade;

import hello.matdil.domain.review.reviewreply.entity.ReviewReply;
import hello.matdil.domain.review.reviewreply.reader.ReviewReplyReader;
import hello.matdil.domain.review.reviewreply.service.ReviewReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminReviewReplyFacade {

    private final ReviewReplyService reviewReplyService;
    private final ReviewReplyReader reviewReplyReader;

    public void deleteReply(Long replyId) {
        ReviewReply reviewReply = reviewReplyReader.getReviewReplyWithReview(replyId);
        Long storeId = reviewReply.getReview().getStoreId();
        reviewReplyService.deleteReply(storeId, reviewReply);
    }
}
