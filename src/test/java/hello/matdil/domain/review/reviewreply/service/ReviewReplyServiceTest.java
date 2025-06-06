package hello.matdil.domain.review.reviewreply.service;

import hello.matdil.domain.review.entity.Review;
import hello.matdil.domain.review.reviewreply.entity.ReviewReply;
import hello.matdil.domain.review.reviewreply.exception.ReviewReplyErrorCode;
import hello.matdil.domain.review.reviewreply.exception.ReviewReplyException;
import hello.matdil.domain.review.reviewreply.repository.ReviewReplyRepository;
import hello.matdil.domain.review.service.ReviewCacheService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ReviewReplyServiceTest {

    @InjectMocks
    private ReviewReplyService reviewReplyService;

    @Mock
    private ReviewReplyRepository reviewReplyRepository;

    @Mock
    private ReviewCacheService reviewCacheService;

    @Mock
    private Review review;

    @Test
    void 리뷰_답글_생성_성공() {
        // given (주어진 상황)
        Long ownerId = 1L;
        Long storeId = 10L;
        String replyText = "답글입니다.";

        ReviewReply reply = ReviewReply.builder().ownerId(ownerId).review(review).replyText(replyText).build();

        given(review.getStoreId()).willReturn(storeId);
        given(reviewReplyRepository.save(any(ReviewReply.class))).willReturn(reply);

        // when
        ReviewReply createdReply = reviewReplyService.createReply(ownerId, review, replyText);

        // then
        assertThat(createdReply.getReplyText()).isEqualTo(replyText);
        then(reviewReplyRepository).should().save(any(ReviewReply.class));
        then(reviewCacheService).should().deleteAll(storeId);
    }

    @Test
    void 답글_생성_실패_답글이_존재할_경우() {
        // given
        Long ownerId = 1L;
        String replyText = "답글입니다.";

        given(reviewReplyRepository.save(any(ReviewReply.class)))
                .willThrow(new DataIntegrityViolationException("중복"));

        // when & then
        assertThatThrownBy(() -> reviewReplyService.createReply(ownerId, review, replyText))
                .isInstanceOf(ReviewReplyException.class)
                .hasMessageContaining(ReviewReplyErrorCode.ALREADY_REVIEW_REPLY.getErrorMessage());

        then(reviewCacheService).should(never()).deleteAll(any(Long.class));
    }
}