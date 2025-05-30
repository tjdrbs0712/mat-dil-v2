package hello.matdil.domain.review.service;

import hello.matdil.domain.review.dto.ReviewCreateRequestDto;
import hello.matdil.domain.review.dto.ReviewResponseDto;
import hello.matdil.domain.user.entity.UserRole;

public interface ReviewService {
    ReviewResponseDto createReview(Long userId, UserRole role, ReviewCreateRequestDto requestDto);
}
