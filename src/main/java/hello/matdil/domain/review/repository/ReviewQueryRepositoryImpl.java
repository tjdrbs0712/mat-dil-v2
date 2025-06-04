package hello.matdil.domain.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.matdil.domain.review.dto.ReviewCursorRequestDto;
import hello.matdil.domain.review.dto.ReviewResponseDto;
import hello.matdil.domain.review.entity.QReview;
import hello.matdil.domain.review.entity.QReviewImage;
import hello.matdil.domain.review.entity.Review;
import hello.matdil.domain.review.entity.ReviewSortType;
import hello.matdil.domain.review.sort.ReviewSortStrategy;
import hello.matdil.domain.review.sort.ReviewSortStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepositoryImpl implements ReviewQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final ReviewSortStrategyFactory strategyFactory;

    @Override
    public List<ReviewResponseDto> loadReviewsByCursor(Long storeId, ReviewCursorRequestDto requestDto) {
        QReview review = QReview.review;
        QReviewImage image = QReviewImage.reviewImage;

        ReviewSortType sortType = requestDto.getSortType();
        ReviewSortStrategy strategy = strategyFactory.getStrategy(sortType);

        List<Long> reviewIds = queryFactory
                .select(review.id)
                .from(review)
                .where(
                        review.storeId.eq(storeId),
                        review.isDeleted.isFalse(),
                        strategy.buildCursorPredicate(review, requestDto.toCursorParamMap())
                )
                .orderBy(strategy.getOrderSpecifiers(review))
                .limit(requestDto.pageSize() + 1)
                .fetch();

        return getReviewResponseDtos(reviewIds, review, image, strategy);
    }

    @Override
    public List<ReviewResponseDto> loadAllReviewsByCursor(Long storeId, ReviewCursorRequestDto requestDto) {
        QReview review = QReview.review;
        QReviewImage image = QReviewImage.reviewImage;

        ReviewSortType sortType = requestDto.getSortType();
        ReviewSortStrategy strategy = strategyFactory.getStrategy(sortType);

        List<Long> reviewIds = queryFactory
                .select(review.id)
                .from(review)
                .where(
                        review.storeId.eq(storeId),
                        strategy.buildCursorPredicate(review, requestDto.toCursorParamMap())
                )
                .orderBy(strategy.getOrderSpecifiers(review))
                .limit(requestDto.pageSize() + 1)
                .fetch();

        return getReviewResponseDtos(reviewIds, review, image, strategy);
    }

    private List<ReviewResponseDto> getReviewResponseDtos(List<Long> reviewIds, QReview review, QReviewImage image, ReviewSortStrategy strategy) {
        if (reviewIds.isEmpty()) {
            return List.of();
        }

        List<Review> reviews = queryFactory
                .selectFrom(review)
                .distinct()
                .leftJoin(review.images, image).fetchJoin()
                .where(review.id.in(reviewIds))
                .orderBy(strategy.getOrderSpecifiers(review))
                .fetch();

        return reviews.stream()
                .map(ReviewResponseDto::from)
                .toList();
    }
}
