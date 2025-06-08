package hello.matdil.domain.review.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.matdil.domain.review.dto.ReviewCursorRequestDto;
import hello.matdil.domain.review.dto.ReviewResponseDto;
import hello.matdil.domain.review.entity.QReview;
import hello.matdil.domain.review.entity.QReviewImage;
import hello.matdil.domain.review.entity.Review;
import hello.matdil.domain.review.entity.ReviewSortType;
import hello.matdil.domain.review.reviewreply.entity.QReviewReply;
import hello.matdil.domain.review.reviewreply.entity.ReviewReply;
import hello.matdil.domain.review.sort.ReviewSortStrategy;
import hello.matdil.domain.review.sort.ReviewSortStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepositoryImpl implements ReviewQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final ReviewSortStrategyFactory strategyFactory;

    /**
     * 삭제되지 않은 리뷰 목록을 조회 (사용자용)
     */
    @Override
    public List<ReviewResponseDto> loadReviewsByCursor(Long storeId, ReviewCursorRequestDto requestDto) {
        List<Long> reviewIds = fetchReviewIds(storeId, requestDto, false);
        return getReviewResponseDtos(reviewIds, requestDto.getSortType());
    }

    /**
     * 모든 리뷰 목록을 조회 (관리자용)
     */
    @Override
    public List<ReviewResponseDto> loadAllReviewsByCursor(Long storeId, ReviewCursorRequestDto requestDto) {
        List<Long> reviewIds = fetchReviewIds(storeId, requestDto, null);
        return getReviewResponseDtos(reviewIds, requestDto.getSortType());
    }

    private List<Long> fetchReviewIds(Long storeId, ReviewCursorRequestDto requestDto, Boolean isDeleted) {
        QReview review = QReview.review;
        ReviewSortStrategy strategy = strategyFactory.getStrategy(requestDto.getSortType());

        return queryFactory
                .select(review.id)
                .from(review)
                .where(
                        review.storeId.eq(storeId),
                        isDeleted(isDeleted),
                        strategy.buildCursorPredicate(review, requestDto.toCursorParamMap())
                )
                .orderBy(strategy.getOrderSpecifiers(review))
                .limit(requestDto.pageSize() + 1)
                .fetch();
    }

    private BooleanExpression isDeleted(Boolean isDeleted) {
        if (isDeleted == null) {
            return null;
        }
        return QReview.review.isDeleted.eq(isDeleted);
    }

    private List<ReviewResponseDto> getReviewResponseDtos(List<Long> reviewIds, ReviewSortType sortType) {
        if (reviewIds.isEmpty()) {
            return List.of();
        }

        ReviewSortStrategy strategy = strategyFactory.getStrategy(sortType);
        List<Review> reviews = findReviewsWithImagesByIds(reviewIds, strategy);
        Map<Long, ReviewReply> replyMap = findReplyMapByReviews(reviews);

        return reviews.stream()
                .map(r -> ReviewResponseDto.from(r, replyMap.get(r.getId())))
                .toList();
    }

    private List<Review> findReviewsWithImagesByIds(List<Long> reviewIds, ReviewSortStrategy strategy) {
        QReview review = QReview.review;
        QReviewImage image = QReviewImage.reviewImage;

        return queryFactory
                .selectFrom(review).distinct()
                .leftJoin(review.images, image).fetchJoin()
                .where(review.id.in(reviewIds))
                .orderBy(strategy.getOrderSpecifiers(review))
                .fetch();
    }

    private Map<Long, ReviewReply> findReplyMapByReviews(List<Review> reviews) {
        QReviewReply reviewReply = QReviewReply.reviewReply;

        List<ReviewReply> replies = queryFactory
                .selectFrom(reviewReply)
                .where(
                        reviewReply.review.in(reviews),
                        reviewReply.isDeleted.isFalse()
                )
                .fetch();

        return replies.stream()
                .collect(Collectors.toMap(reply -> reply.getReview().getId(), Function.identity()));
    }
}