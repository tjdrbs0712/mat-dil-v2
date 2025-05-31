package hello.matdil.domain.review.service;

import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.reader.OrderReader;
import hello.matdil.domain.review.dto.*;
import hello.matdil.domain.review.entity.Review;
import hello.matdil.domain.review.exception.ReviewErrorCode;
import hello.matdil.domain.review.exception.ReviewException;
import hello.matdil.domain.review.factory.ReviewFactory;
import hello.matdil.domain.review.repository.ReviewRepository;
import hello.matdil.domain.store.reader.StoreReader;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.util.pagination.PageAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewFactory reviewFactory;
    private final ReviewCacheService reviewCacheService;
    private final StoreReader storeReader;
    private final OrderReader orderReader;
    private final PageAssembler pageAssembler;

    @Override
    @Transactional
    public ReviewResponseDto createReview(Long userId, UserRole role, ReviewCreateRequestDto requestDto) {
        storeReader.readByIdWithPermission(userId, requestDto.storeId(), role);
        Order order = orderReader.readWithUserPermission(requestDto.orderId(), userId, role);

        order.isCompleted();
        Review review = reviewFactory.create(userId, requestDto);

        try {
            review = reviewRepository.save(review);
        } catch (DataIntegrityViolationException e) {
            throw new ReviewException(ReviewErrorCode.ALREADY_REVIEW);
        }

        reviewCacheService.deleteAll(requestDto.storeId());
        return ReviewResponseDto.from(review);
    }

    @Override
    @Transactional
    public ReviewResponseDto updateReview(Long userId, UserRole role, Long reviewId, ReviewUpdateRequestDto requestDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        storeReader.readByIdWithPermission(userId, review.getStoreId(), role);

        review.validateAccessibleTo(userId, role);
        review.validateIsDeleted();

        review.updateReview(requestDto.rating(), requestDto.comment(), requestDto.imageUrls());

        reviewCacheService.deleteAll(review.getStoreId());
        return ReviewResponseDto.from(review);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponse<ReviewResponseDto, ReviewCursorResponseDto> getReviews(
            Long userId,
            UserRole role,
            Long storeId,
            ReviewCursorRequestDto request
    ) {
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

    @Override
    @Transactional
    public void deleteReview(Long userId, UserRole role, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        storeReader.readByIdWithPermission(userId, review.getStoreId(), role);

        review.validateAccessibleTo(userId, role);
        review.validateIsDeleted();

        review.isDeleted();
        reviewCacheService.deleteAll(review.getStoreId());
    }
}
