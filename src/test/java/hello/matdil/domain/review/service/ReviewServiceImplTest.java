package hello.matdil.domain.review.service;

import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.reader.OrderReader;
import hello.matdil.domain.review.dto.ReviewCreateRequestDto;
import hello.matdil.domain.review.dto.ReviewResponseDto;
import hello.matdil.domain.review.dto.ReviewUpdateRequestDto;
import hello.matdil.domain.review.entity.Review;
import hello.matdil.domain.review.exception.ReviewErrorCode;
import hello.matdil.domain.review.exception.ReviewException;
import hello.matdil.domain.review.factory.ReviewFactory;
import hello.matdil.domain.review.repository.ReviewImageRepository;
import hello.matdil.domain.review.repository.ReviewRepository;
import hello.matdil.domain.store.reader.StoreReader;
import hello.matdil.domain.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewImageRepository reviewImageRepository;

    @Mock
    private ReviewFactory reviewFactory;

    @Mock
    private StoreReader storeReader;

    @Mock
    private OrderReader orderReader;

    @Test
    void 리뷰_정상_생성() {
        // given
        Long userId = 1L;
        Long storeId = 10L;
        Long orderId = 100L;
        UserRole role = UserRole.USER;

        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(
                orderId,
                storeId,
                5,
                "맛있어요!",
                List.of("url1", "url2")
        );

        Review review = mock(Review.class);
        Order order = mock(Order.class);

        given(storeReader.readByIdWithPermission(userId, storeId, role)).willReturn(null);
        given(orderReader.readWithUserPermission(orderId, userId, role)).willReturn(order);
        given(reviewFactory.create(userId, requestDto)).willReturn(review);
        given(reviewRepository.save(review)).willReturn(review);

        // when
        ReviewResponseDto response = reviewService.createReview(userId, role, requestDto);

        // then
        assertThat(response).isNotNull();
        verify(storeReader).readByIdWithPermission(userId, storeId, role);
        verify(orderReader).readWithUserPermission(orderId, userId, role);
        verify(order).isCompleted();
        verify(reviewFactory).create(userId, requestDto);
        verify(reviewRepository).save(review);
    }

    @Test
    void 리뷰_중복일_경우_예외() {
        // given
        Long userId = 1L;
        Long storeId = 10L;
        Long orderId = 100L;
        UserRole role = UserRole.USER;

        ReviewCreateRequestDto requestDto = new ReviewCreateRequestDto(
                orderId,
                storeId,
                4,
                "또 올게요",
                List.of()
        );

        Review review = mock(Review.class);
        Order order = mock(Order.class);

        given(storeReader.readByIdWithPermission(userId, storeId, role)).willReturn(null);
        given(orderReader.readWithUserPermission(orderId, userId, role)).willReturn(order);
        given(reviewFactory.create(userId, requestDto)).willReturn(review);
        given(reviewRepository.save(review)).willThrow(new DataIntegrityViolationException("중복"));

        // when & then
        assertThatThrownBy(() -> reviewService.createReview(userId, role, requestDto))
                .isInstanceOf(ReviewException.class)
                .hasMessageContaining(ReviewErrorCode.ALREADY_REVIEW.getErrorMessage());

        verify(reviewRepository).save(review);
    }

    @Test
    void 리뷰_수정_성공(){
        // given
        Long userId = 1L;
        Long reviewId = 1L;
        UserRole role = UserRole.USER;
        Review review = Review.builder()
                .rating(4)
                .comment("리뷰 작성")
                .userId(1L)
                .orderId(1L)
                .storeId(1L)
                .build();

        ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto(5, "리뷰 수정", List.of("이미지"));

        given(reviewRepository.findById(reviewId)).willReturn(Optional.ofNullable(review));

        // when
        ReviewResponseDto result = reviewService.updateReview(userId, role, reviewId, requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getComment()).isEqualTo("리뷰 수정");
    }

    @Test
    void 리뷰_수정_권한_없는_경우(){
        // given
        Long userId = 2L;
        Long reviewId = 1L;
        UserRole role = UserRole.USER;
        Review review = Review.builder()
                .rating(4)
                .comment("리뷰 작성")
                .userId(1L)
                .orderId(1L)
                .storeId(1L)
                .build();

        ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto(5, "리뷰 수정", List.of("이미지"));

        given(reviewRepository.findById(reviewId)).willReturn(Optional.ofNullable(review));

        // when & then
        assertThatThrownBy(() -> reviewService.updateReview(userId, role, reviewId, requestDto))
                .isInstanceOf(ReviewException.class)
                .hasMessageContaining(ReviewErrorCode.NO_PERMISSION.getErrorMessage());
    }

    @Test
    void 삭제된_리뷰를_수정한_경우(){
        // given
        Long userId = 1L;
        Long reviewId = 1L;
        UserRole role = UserRole.USER;
        Review review = Review.builder()
                .rating(4)
                .comment("리뷰 작성")
                .userId(1L)
                .orderId(1L)
                .storeId(1L)
                .build();
        review.isDeleted();

        ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto(5, "리뷰 수정", List.of("이미지"));

        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));


        // when & then
        assertThatThrownBy(() -> reviewService.updateReview(userId, role, reviewId, requestDto))
                .isInstanceOf(ReviewException.class)
                .hasMessageContaining(ReviewErrorCode.REVIEW_NOT_FOUND.getErrorMessage());
    }

}
