package hello.matdil.domain.review.reviewreply.facade;

import hello.matdil.domain.review.entity.Review;
import hello.matdil.domain.review.reader.ReviewReader;
import hello.matdil.domain.review.reviewreply.dto.ReviewReplyCreateRequestDto;
import hello.matdil.domain.review.reviewreply.dto.ReviewReplyResponseDto;
import hello.matdil.domain.review.reviewreply.dto.ReviewReplyUpdateRequestDto;
import hello.matdil.domain.review.reviewreply.entity.ReviewReply;
import hello.matdil.domain.review.reviewreply.exception.ReviewReplyErrorCode;
import hello.matdil.domain.review.reviewreply.exception.ReviewReplyException;
import hello.matdil.domain.review.reviewreply.reader.ReviewReplyReader;
import hello.matdil.domain.review.reviewreply.service.ReviewReplyService;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.validator.StoreValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ReviewReplyFacadeTest {

    @InjectMocks
    private ReviewReplyFacade reviewReplyFacade;

    @Mock
    private ReviewReplyReader reviewReplyReader;

    @Mock
    private ReviewReader reviewReader;
    @Mock
    private ReviewReplyService reviewReplyService;
    @Mock
    private StoreValidator storeValidator;

    @Test
    void 퍼사드_리뷰_답글_생성_성공_리팩토링() {
        // given
        Long ownerId = 1L;
        Long reviewId = 2L;
        Long storeId = 10L;
        ReviewReplyCreateRequestDto requestDto = new ReviewReplyCreateRequestDto("감사합니다!");

        Review mockReview = mock(Review.class);

        ReviewReply realReply = ReviewReply.builder()
                .review(mockReview)
                .ownerId(ownerId)
                .replyText(requestDto.replyText())
                .build();

        given(reviewReader.getReviewByIdAndDeletedFalse(reviewId)).willReturn(mockReview);
        given(mockReview.getStoreId()).willReturn(storeId);
        given(reviewReplyService.createReply(ownerId, mockReview, requestDto.replyText())).willReturn(realReply);

        // when
        ReviewReplyResponseDto responseDto = reviewReplyFacade.createReply(ownerId, reviewId, requestDto);

        // then
        assertThat(responseDto.replyText()).isEqualTo(requestDto.replyText());

        then(reviewReader).should().getReviewByIdAndDeletedFalse(reviewId);
        then(storeValidator).should().validateOwnerOf(storeId, ownerId);
        then(reviewReplyService).should().createReply(ownerId, mockReview, requestDto.replyText());
    }

    @Test
    void 점주_권한이_없을_경우() {
        // given
        Long ownerId = 1L;
        Long reviewId = 2L;
        Long storeId = 10L;
        ReviewReplyCreateRequestDto requestDto = new ReviewReplyCreateRequestDto("감사합니다!");

        Review mockReview = mock(Review.class);

        given(reviewReader.getReviewByIdAndDeletedFalse(reviewId)).willReturn(mockReview);
        given(mockReview.getStoreId()).willReturn(storeId);
        // validateOwnerOf 메서드가 호출되면 예외를 던지도록 설정
        willThrow(new StoreException(StoreErrorCode.NO_PERMISSION))
                .given(storeValidator).validateOwnerOf(storeId, ownerId);

        // when & then
        assertThatThrownBy(() -> reviewReplyFacade.createReply(ownerId, reviewId, requestDto))
                .isInstanceOf(StoreException.class);

        then(reviewReplyService).should(never()).createReply(any(), any(), any());
    }

    @Test
    void 퍼사드_리뷰_답글_수정_성공() {
        // given
        Long ownerId = 1L;
        Long replyId = 2L;
        Long storeId = 10L;
        ReviewReplyUpdateRequestDto requestDto = new ReviewReplyUpdateRequestDto("수정된 답글!");

        Review mockReview = mock(Review.class);
        ReviewReply mockReply = mock(ReviewReply.class);

        given(reviewReplyReader.getFindByIdAndOwnerWithReviewAndIsDeletedFalse(replyId, ownerId)).willReturn(mockReply);
        given(mockReply.getReview()).willReturn(mockReview);
        given(mockReview.getStoreId()).willReturn(storeId);
        given(reviewReplyService.updateReply(storeId, mockReply, requestDto.replyText())).willReturn(mockReply);

        // when
        ReviewReplyResponseDto responseDto = reviewReplyFacade.updateReply(ownerId, replyId, requestDto);

        // then
        assertThat(responseDto).isNotNull();

        then(reviewReplyReader).should().getFindByIdAndOwnerWithReviewAndIsDeletedFalse(replyId, ownerId);
        then(storeValidator).should().validateOwnerOf(storeId, ownerId);
        then(reviewReplyService).should().updateReply(storeId, mockReply, requestDto.replyText());
    }

    @Test
    void 리뷰_답글_수정_실패_수정할_답글이_없음() {
        // given
        Long ownerId = 1L;
        Long replyId = 999L;
        ReviewReplyUpdateRequestDto requestDto = new ReviewReplyUpdateRequestDto("수정된 답글!");

        given(reviewReplyReader.getFindByIdAndOwnerWithReviewAndIsDeletedFalse(replyId, ownerId))
                .willThrow(new ReviewReplyException(ReviewReplyErrorCode.REVIEW_REPLY_NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> reviewReplyFacade.updateReply(ownerId, replyId, requestDto))
                .isInstanceOf(ReviewReplyException.class);

        then(storeValidator).should(never()).validateOwnerOf(anyLong(), anyLong());
        then(reviewReplyService).should(never()).updateReply(anyLong(), any(), anyString());
    }
}