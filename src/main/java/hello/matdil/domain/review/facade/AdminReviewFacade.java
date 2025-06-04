package hello.matdil.domain.review.facade;

import hello.matdil.domain.review.dto.*;
import hello.matdil.domain.review.entity.Review;
import hello.matdil.domain.review.service.ReviewCommandService;
import hello.matdil.domain.review.service.ReviewSearchService;
import hello.matdil.domain.store.validator.StoreExistenceValidatorStrategy;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminReviewFacade {

    private final ReviewCommandService commandService;
    private final ReviewSearchService searchService;
    private final StoreExistenceValidatorStrategy validatorStrategy;

    public ReviewResponseDto createReview(UserRole role, AdminReviewCreateRequestDto adminDto) {
        ReviewCreateRequestDto dto = ReviewCreateRequestDto.from(adminDto);
        validatorStrategy.validate(role, dto.storeId());
        return commandService.create(adminDto.userId(), dto);
    }

    public void deleteReview(UserRole role, Long reviewId) {
        Review review = commandService.getReviewById(reviewId);
        validatorStrategy.validate(role, review.getStoreId());
        commandService.delete(review);
    }

    public SliceResponse<ReviewResponseDto, ReviewCursorResponseDto> getAllReviews(
            UserRole role, Long storeId, @Valid ReviewCursorRequestDto request) {
        validatorStrategy.validate(role, storeId);
        return searchService.getAllStoreReviewsForAdmin(storeId, request);
    }

    public ReviewResponseDto updateReview(UserRole role, Long reviewId, AdminReviewUpdateRequestDto adminDto) {
        ReviewUpdateRequestDto dto = ReviewUpdateRequestDto.from(adminDto);
        Review review = commandService.getReviewById(reviewId);
        validatorStrategy.validate(role, review.getStoreId());
        return commandService.update(review, dto);
    }
}
