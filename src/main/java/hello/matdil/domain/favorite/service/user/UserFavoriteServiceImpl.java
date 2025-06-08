package hello.matdil.domain.favorite.service.user;

import hello.matdil.domain.favorite.dto.FavoriteCursorRequestDto;
import hello.matdil.domain.favorite.dto.FavoriteCursorResponseDto;
import hello.matdil.domain.favorite.dto.FavoriteStoreSummaryDto;
import hello.matdil.domain.favorite.repository.FavoriteRepository;
import hello.matdil.domain.favorite.service.FavoriteCacheService;
import hello.matdil.domain.favorite.service.FavoriteCommand;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.domain.store.validator.StoreValidator;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.util.pagination.PageAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFavoriteServiceImpl implements UserFavoriteService {

    private final FavoriteCommand favoriteCommand;
    private final FavoriteRepository favoriteRepository;
    private final FavoriteCacheService favoriteCacheService;
    private final PageAssembler pageAssembler;
    private final StoreValidator storeValidator;

    @Override
    @Transactional
    public void addFavorite(Long userId, Long storeId) {
        storeValidator.validateUserExists(userId);
        favoriteCommand.addFavorite(userId, storeId);
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long storeId) {
        storeValidator.validateUserExists(userId);
        favoriteCommand.removeFavorite(userId, storeId);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponse<StoreSummaryResponseDto, FavoriteCursorResponseDto> getFavoriteStores(
            Long userId, FavoriteCursorRequestDto request) {

        boolean isFirstPage = request.lastStoreId() == null;

        List<FavoriteStoreSummaryDto> storeSummaryList = isFirstPage
                ? favoriteCacheService.getFavoriteStores(userId, request)
                : favoriteRepository.loadFavoriteStoreSummaries(userId, request);

        int size = request.pageSize();

        return pageAssembler.assemble(
                storeSummaryList,
                size,
                last -> FavoriteCursorResponseDto.from(last, request.getSortType(), size),
                FavoriteStoreSummaryDto::getSummary
        );
    }
}
