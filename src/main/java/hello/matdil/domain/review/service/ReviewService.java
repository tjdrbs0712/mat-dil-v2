package hello.matdil.domain.review.service;

import hello.matdil.domain.review.dto.ReviewCreateRequestDto;
import hello.matdil.domain.review.dto.ReviewResponseDto;
import hello.matdil.domain.review.dto.ReviewUpdateRequestDto;
import hello.matdil.domain.user.entity.UserRole;
import jakarta.validation.Valid;

public interface ReviewService {
    ReviewResponseDto createReview(Long userId, UserRole role, ReviewCreateRequestDto requestDto);

    ReviewResponseDto updateReview(Long userId, UserRole role, Long reviewId, @Valid ReviewUpdateRequestDto requestDto);

}
