package hello.matdil.domain.review.facade;

import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.reader.OrderReader;
import hello.matdil.domain.review.dto.*;
import hello.matdil.domain.review.entity.Review;
import hello.matdil.domain.review.service.ReviewCommandService;
import hello.matdil.domain.review.service.ReviewSearchService;
import hello.matdil.domain.store.validator.StoreExistenceValidatorStrategy;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewFacade {

    private final OrderReader orderReader;
    private final ReviewCommandService commandService;
    private final ReviewSearchService searchService;
    private final StoreExistenceValidatorStrategy validatorStrategy;

    public ReviewResponseDto createReview(Long userId, UserRole role, ReviewCreateRequestDto dto) {
        validatorStrategy.validate(role, dto.storeId());
        Order order = orderReader.readWithUserPermission(dto.orderId(), userId, role);
        order.validateIsCompleted();

        return commandService.create(userId, dto);
    }

    public ReviewResponseDto updateReview(Long userId, UserRole role, Long reviewId, ReviewUpdateRequestDto dto) {
        Review review = commandService.getReviewByIdDeletedFalse(reviewId, userId);
        validatorStrategy.validate(role, review.getStoreId());
        return commandService.update(review, dto);
    }

    public void deleteReview(Long userId, UserRole role, Long reviewId) {
        Review review = commandService.getReviewByIdDeletedFalse(reviewId, userId);
        validatorStrategy.validate(role, review.getStoreId());
        commandService.delete(review);
    }

    public SliceResponse<ReviewResponseDto, ReviewCursorResponseDto> getReviews(Long storeId, ReviewCursorRequestDto request) {
        return searchService.getReviews(storeId, request);
    }
}
