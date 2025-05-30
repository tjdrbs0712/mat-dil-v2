package hello.matdil.domain.review.repository;

import hello.matdil.domain.review.dto.ReviewCursorRequestDto;
import hello.matdil.domain.review.dto.ReviewResponseDto;

import java.util.List;

public interface ReviewQueryRepository {
    List<ReviewResponseDto> loadReviewsByCursor(Long storeId, ReviewCursorRequestDto requestDto);
}
