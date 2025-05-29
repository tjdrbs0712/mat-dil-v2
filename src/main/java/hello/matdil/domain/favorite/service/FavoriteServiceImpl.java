package hello.matdil.domain.favorite.service;

import hello.matdil.domain.favorite.dto.FavoriteCursorRequestDto;
import hello.matdil.domain.favorite.dto.FavoriteCursorResponseDto;
import hello.matdil.domain.favorite.dto.FavoriteStoreSummaryDto;
import hello.matdil.domain.favorite.entity.Favorite;
import hello.matdil.domain.favorite.execption.FavoriteErrorCode;
import hello.matdil.domain.favorite.execption.FavoriteException;
import hello.matdil.domain.favorite.repository.FavoriteRepository;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.domain.store.reader.StoreReader;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.util.pagination.PageAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteCommand favoriteCommand;
    private final FavoriteRepository favoriteRepository;
    private final FavoriteCacheService favoriteCacheService;
    private final StoreReader storeReader;
    private final PageAssembler pageAssembler;

    @Override
    @Transactional
    public void addFavorite(Long userId, UserRole role, Long storeId) {
        favoriteCommand.addFavorite(userId, role, storeId);
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, UserRole role, Long storeId) {
        favoriteCommand.removeFavorite(userId, role, storeId);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponse<StoreSummaryResponseDto, FavoriteCursorResponseDto> getFavoriteStores(
            Long userId, UserRole role, FavoriteCursorRequestDto request) {

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
