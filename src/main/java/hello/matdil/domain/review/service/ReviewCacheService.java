package hello.matdil.domain.review.service;

import hello.matdil.domain.review.dto.ReviewCursorRequestDto;
import hello.matdil.domain.review.dto.ReviewResponseDto;
import hello.matdil.domain.review.entity.ReviewSortType;
import hello.matdil.domain.review.repository.ReviewRepository;
import hello.matdil.infrastructure.redis.RedisListJsonCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewCacheService {

    private final RedisListJsonCache redisCache;
    private final ReviewRepository reviewRepository;

    private String getKey(Long storeId, ReviewSortType sortType) {
        return "reviews:" + storeId + ":" + sortType.name().toLowerCase();
    }

    public List<ReviewResponseDto> getReviews(Long storeId, ReviewCursorRequestDto dto) {
        String key = getKey(storeId, dto.getSortType());

        return redisCache.getList(key, ReviewResponseDto.class)
                .orElseGet(() -> {
                    List<ReviewResponseDto> result =
                            reviewRepository.loadReviewsByCursor(storeId, dto);
                    redisCache.putList(key, result, Duration.ofMinutes(5));
                    return result;
                });
    }

    public void delete(Long storeId, ReviewSortType sortType) {
        redisCache.delete(getKey(storeId, sortType));
    }

    public void deleteAll(Long storeId) {
        for (ReviewSortType sortType : ReviewSortType.values()) {
            redisCache.delete(getKey(storeId, sortType));
        }
    }
}
