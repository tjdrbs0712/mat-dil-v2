package hello.matdil.domain.review.service;

import hello.matdil.domain.review.dto.ReviewCursorRequestDto;
import hello.matdil.domain.review.dto.ReviewCursorResponseDto;
import hello.matdil.domain.review.dto.ReviewResponseDto;
import hello.matdil.domain.review.repository.ReviewRepository;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.util.pagination.PageAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReviewQueryService {

    private final ReviewRepository reviewRepository;
    private final ReviewCacheService reviewCacheService;
    private final PageAssembler pageAssembler;

    @Transactional(readOnly = true)
    public SliceResponse<ReviewResponseDto, ReviewCursorResponseDto> getReviews(Long storeId, ReviewCursorRequestDto request) {
        boolean isFirstPage = !request.hasCursor();

        List<ReviewResponseDto> reviews = isFirstPage
                ? reviewCacheService.getReviews(storeId, request)
                : reviewRepository.loadReviewsByCursor(storeId, request);

        int size = request.pageSize();
        return pageAssembler.assemble(
                reviews,
                size,
                last -> ReviewCursorResponseDto.from(size, request.getSortType(), last),
                Function.identity()
        );
    }
}
