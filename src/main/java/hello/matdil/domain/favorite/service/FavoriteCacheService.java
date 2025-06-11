package hello.matdil.domain.favorite.service;

import hello.matdil.domain.favorite.dto.FavoriteCursorRequestDto;
import hello.matdil.domain.favorite.dto.FavoriteStoreSummaryDto;
import hello.matdil.domain.favorite.entity.FavoriteSortType;
import hello.matdil.domain.favorite.repository.FavoriteRepository;
import hello.matdil.infrastructure.redis.RedisListJsonCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteCacheService {

    private final RedisListJsonCache redisCache;
    private final FavoriteRepository favoriteRepository;

    private String getKey(Long userId, FavoriteSortType sortType) {
        return "fav:stores:" + userId + ":" + sortType.name().toLowerCase();
    }

    public List<FavoriteStoreSummaryDto> getFavoriteStores(Long userId, FavoriteCursorRequestDto dto) {
        String key = getKey(userId, dto.getSortType());

        return redisCache.getList(key, FavoriteStoreSummaryDto.class)
                .orElseGet(() -> {
                    List<FavoriteStoreSummaryDto> result =
                            favoriteRepository.loadFavoriteStoreSummaries(userId, dto);
                    redisCache.putList(key, result, Duration.ofMinutes(5));
                    return result;
                });
    }

    public void delete(Long userId, FavoriteSortType sortType) {
        redisCache.delete(getKey(userId, sortType));
    }

    public void deleteAll(Long userId) {
        for (FavoriteSortType sortType : FavoriteSortType.values()) {
            redisCache.delete(getKey(userId, sortType));
        }
    }
}
