package hello.matdil.domain.review.service;

import hello.matdil.domain.review.dto.ReviewCursorRequestDto;
import hello.matdil.domain.review.dto.ReviewCursorResponseDto;
import hello.matdil.domain.review.dto.ReviewResponseDto;
import hello.matdil.domain.review.repository.ReviewRepository;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.util.pagination.PageAssembler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewSearchServiceTest {

    @InjectMocks
    private ReviewSearchService reviewSearchService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewCacheService reviewCacheService;

    @Mock
    private PageAssembler pageAssembler;

    @Test
    void 첫_페이지일_경우_캐시에서_리뷰_조회() {
        // given
        Long storeId = 100L;

        ReviewCursorRequestDto request = new ReviewCursorRequestDto(
                30, null, null, null, "LATEST");
        ReviewCursorResponseDto response = mock(ReviewCursorResponseDto.class);

        List<ReviewResponseDto> cachedReviews = List.of(
                ReviewResponseDto.builder().id(1L).userId(1L).storeId(1L).build(),
                ReviewResponseDto.builder().id(2L).userId(1L).storeId(1L).build()
        );

        given(reviewCacheService.getReviews(storeId, request)).willReturn(cachedReviews);

        SliceResponse<ReviewResponseDto, ReviewCursorResponseDto> expectedSlice =
                SliceResponse.of(cachedReviews, false, response);

        given(pageAssembler.assemble(
                eq(cachedReviews),
                eq(30),
                any(Function.class),
                any(Function.class)))
                .willReturn(expectedSlice);

        // when
        SliceResponse<ReviewResponseDto, ReviewCursorResponseDto> result = reviewSearchService.getReviews(storeId, request);

        // then
        assertThat(result).isEqualTo(expectedSlice);
        verify(reviewCacheService).getReviews(storeId, request);
        verify(reviewRepository, never()).loadReviewsByCursor(any(), any());
    }

    @Test
    void 커서가_있는_경우_DB에서_리뷰_조회() {
        // given
        Long storeId = 100L;
        LocalDateTime localDateTime = LocalDateTime.now();

        ReviewCursorRequestDto request = new ReviewCursorRequestDto(
                10, null, localDateTime, 2L, "LATEST");
        ReviewCursorResponseDto response = mock(ReviewCursorResponseDto.class);

        List<ReviewResponseDto> dbReviews = List.of(
                ReviewResponseDto.builder().id(3L).build(),
                ReviewResponseDto.builder().id(4L).build()
        );

        given(reviewRepository.loadReviewsByCursor(storeId, request)).willReturn(dbReviews);

        SliceResponse<ReviewResponseDto, ReviewCursorResponseDto> expectedSlice =
                SliceResponse.of(dbReviews, true, response);

        given(pageAssembler.assemble(
                eq(dbReviews),
                eq(10),
                any(Function.class),
                any(Function.class)))
                .willReturn(expectedSlice);

        // when
        SliceResponse<ReviewResponseDto, ReviewCursorResponseDto> result =
                reviewSearchService.getReviews(storeId, request);

        // then
        assertThat(result).isEqualTo(expectedSlice);
        verify(reviewCacheService, never()).getReviews(any(), any());
        verify(reviewRepository).loadReviewsByCursor(storeId, request);
    }

}