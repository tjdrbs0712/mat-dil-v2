package hello.matdil.domain.review.service;

import hello.matdil.domain.review.dto.ReviewCreateRequestDto;
import hello.matdil.domain.review.dto.ReviewResponseDto;
import hello.matdil.domain.review.dto.ReviewUpdateRequestDto;
import hello.matdil.domain.review.entity.Review;
import hello.matdil.domain.review.exception.ReviewErrorCode;
import hello.matdil.domain.review.exception.ReviewException;
import hello.matdil.domain.review.factory.ReviewFactory;
import hello.matdil.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewCommandService {

    private final ReviewRepository reviewRepository;
    private final ReviewFactory reviewFactory;
    private final ReviewCacheService reviewCacheService;

    @Transactional
    public ReviewResponseDto create(Long userId, ReviewCreateRequestDto dto) {
        Review review = reviewFactory.create(userId, dto);

        try {
            review = reviewRepository.save(review);
        } catch (DataIntegrityViolationException e) {
            throw new ReviewException(ReviewErrorCode.ALREADY_REVIEW);
        }

        reviewCacheService.deleteAll(dto.storeId());
        return ReviewResponseDto.from(review);
    }

    @Transactional
    public ReviewResponseDto update(Review review, ReviewUpdateRequestDto dto) {
        review.validateIsDeleted();
        review.updateReview(dto.rating(), dto.comment(), dto.imageUrls());
        reviewCacheService.deleteAll(review.getStoreId());
        return ReviewResponseDto.from(review);
    }

    @Transactional
    public void delete(Review review) {
        review.validateIsDeleted();
        review.markAsDeleted();
        reviewCacheService.deleteAll(review.getStoreId());
    }

}
