package hello.matdil.domain.review.facade;

import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.exception.OrderErrorCode;
import hello.matdil.domain.order.exception.OrderException;
import hello.matdil.domain.order.reader.OrderReader;
import hello.matdil.domain.review.dto.ReviewCreateRequestDto;
import hello.matdil.domain.review.dto.ReviewResponseDto;
import hello.matdil.domain.review.dto.ReviewUpdateRequestDto;
import hello.matdil.domain.review.entity.Review;
import hello.matdil.domain.review.exception.ReviewErrorCode;
import hello.matdil.domain.review.exception.ReviewException;
import hello.matdil.domain.review.reader.ReviewReader;
import hello.matdil.domain.review.service.ReviewCommandService;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.validator.StoreExistenceValidatorStrategy;
import hello.matdil.domain.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ReviewFacadeTest {

    @Mock
    private OrderReader orderReader;

    @Mock
    private ReviewReader reviewReader;

    @Mock
    private ReviewCommandService commandService;

    @Mock
    private StoreExistenceValidatorStrategy validatorStrategy;

    @InjectMocks
    private ReviewFacade reviewFacade;

    @Test
    void 리뷰_생성_성공() {
        // given
        Long userId = 1L;
        UserRole role = UserRole.USER;
        ReviewCreateRequestDto dto = new ReviewCreateRequestDto(1L, 1L, 5, "좋아요", List.of());
        Order order = mock(Order.class);
        ReviewResponseDto expected = ReviewResponseDto.builder().id(1L).comment("좋아요").build();

        given(orderReader.readWithUserPermission(dto.orderId(), userId, role)).willReturn(order);
        given(commandService.create(userId, dto)).willReturn(expected);

        // when
        ReviewResponseDto response = reviewFacade.createReview(userId, role, dto);

        // then
        assertThat(response.getComment()).isEqualTo("좋아요");
    }

    @Test
    void 리뷰_생성_실패_존재하지_않는_가게() {
        // given
        Long userId = 1L;
        UserRole role = UserRole.USER;
        ReviewCreateRequestDto dto = new ReviewCreateRequestDto(99L, 1L, 5, "내용", List.of());

        willThrow(new StoreException(StoreErrorCode.STORE_NOT_FOUND))
                .given(validatorStrategy).validate(role, dto.storeId());

        // when & then
        assertThatThrownBy(() -> reviewFacade.createReview(userId, role, dto))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(StoreErrorCode.STORE_NOT_FOUND.getErrorMessage());
    }

    @Test
    void 리뷰_생성_실패_권한_없음() {
        // given
        Long userId = 1L;
        UserRole role = UserRole.USER;
        ReviewCreateRequestDto dto = new ReviewCreateRequestDto(1L, 999L, 5, "내용", List.of());

        willThrow(new OrderException(OrderErrorCode.NO_PERMISSION))
                .given(orderReader).readWithUserPermission(dto.orderId(), userId, role);

        // when & then
        assertThatThrownBy(() -> reviewFacade.createReview(userId, role, dto))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining(OrderErrorCode.NO_PERMISSION.getErrorMessage());
    }

    @Test
    void 리뷰_수정_성공() {
        // given
        Long userId = 1L;
        UserRole role = UserRole.USER;
        Long reviewId = 1L;
        Review review = mock(Review.class);
        ReviewUpdateRequestDto dto = new ReviewUpdateRequestDto(4, "수정", List.of());
        ReviewResponseDto expected = ReviewResponseDto.builder().id(reviewId).comment("수정").build();

        given(reviewReader.getReviewByIAndUserIdAndDeletedFalse(reviewId, userId)).willReturn(review);
        given(commandService.update(review, dto)).willReturn(expected);

        // when
        ReviewResponseDto response = reviewFacade.updateReview(userId, role, reviewId, dto);

        // then
        assertThat(response.getComment()).isEqualTo("수정");
    }

    @Test
    void 리뷰_수정_실패_삭제된_리뷰() {
        // given
        Long userId = 1L;
        UserRole role = UserRole.USER;
        Long reviewId = 1L;
        ReviewUpdateRequestDto dto = new ReviewUpdateRequestDto(4, "수정", List.of());

        given(reviewReader.getReviewByIAndUserIdAndDeletedFalse(reviewId, userId))
                .willThrow(new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> reviewFacade.updateReview(userId, role, reviewId, dto))
                .isInstanceOf(ReviewException.class)
                .hasMessageContaining(ReviewErrorCode.REVIEW_NOT_FOUND.getErrorMessage());
    }

    @Test
    void 리뷰_삭제_성공() {
        // given
        Long userId = 1L;
        UserRole role = UserRole.USER;
        Long reviewId = 1L;
        Review review = mock(Review.class);

        given(reviewReader.getReviewByIAndUserIdAndDeletedFalse(reviewId, userId)).willReturn(review);

        // when
        reviewFacade.deleteReview(userId, role, reviewId);

        // then
        verify(reviewReader).getReviewByIAndUserIdAndDeletedFalse(reviewId, userId);
        verify(commandService).delete(review);
    }

    @Test
    void 리뷰_삭제_실패_권한_없음() {
        // given
        Long userId = 1L;
        UserRole role = UserRole.USER;
        Long reviewId = 1L;

        given(reviewReader.getReviewByIAndUserIdAndDeletedFalse(reviewId, userId))
                .willThrow(new ReviewException(ReviewErrorCode.NO_PERMISSION));

        // when & then
        assertThatThrownBy(() -> reviewFacade.deleteReview(userId, role, reviewId))
                .isInstanceOf(ReviewException.class)
                .hasMessageContaining(ReviewErrorCode.NO_PERMISSION.getErrorMessage());
    }

    @Test
    void 리뷰_삭제_실패_이미_삭제됨() {
        // given
        Long userId = 1L;
        UserRole role = UserRole.USER;
        Long reviewId = 1L;

        given(reviewReader.getReviewByIAndUserIdAndDeletedFalse(reviewId, userId))
                .willThrow(new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> reviewFacade.deleteReview(userId, role, reviewId))
                .isInstanceOf(ReviewException.class)
                .hasMessageContaining(ReviewErrorCode.REVIEW_NOT_FOUND.getErrorMessage());
    }
}
