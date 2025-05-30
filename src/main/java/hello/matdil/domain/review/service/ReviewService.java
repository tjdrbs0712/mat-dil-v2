package hello.matdil.domain.review.service;

import hello.matdil.domain.review.dto.*;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;
import jakarta.validation.Valid;

public interface ReviewService {
    ReviewResponseDto createReview(Long userId, UserRole role, ReviewCreateRequestDto requestDto);

    ReviewResponseDto updateReview(Long userId, UserRole role, Long reviewId, @Valid ReviewUpdateRequestDto requestDto);

    SliceResponse<ReviewResponseDto, ReviewCursorResponseDto> getReviews(
            Long userId, UserRole role, Long storeId, ReviewCursorRequestDto request);

}
