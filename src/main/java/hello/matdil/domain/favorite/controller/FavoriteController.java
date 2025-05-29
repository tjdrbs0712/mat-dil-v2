package hello.matdil.domain.favorite.controller;

import hello.matdil.auth.annotation.LoginUser;
import hello.matdil.auth.model.AuthUser;
import hello.matdil.domain.favorite.dto.FavoriteCursorRequestDto;
import hello.matdil.domain.favorite.dto.FavoriteCursorResponseDto;
import hello.matdil.domain.favorite.dto.FavoriteRequestDto;
import hello.matdil.domain.favorite.service.FavoriteService;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // 즐겨찾기 등록
    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> addFavorite(
            @LoginUser AuthUser loginUser,
            @RequestBody FavoriteRequestDto request
    ) {
        favoriteService.addFavorite(loginUser.getUserId(), loginUser.getRole(), request.storeId());
        return ResponseEntity.ok(SuccessResponse.success(null));
    }

    // 즐겨찾기 취소
    @DeleteMapping("/{storeId}")
    public ResponseEntity<SuccessResponse<Void>> removeFavorite(
            @LoginUser AuthUser loginUser,
            @PathVariable Long storeId
    ) {
        favoriteService.removeFavorite(loginUser.getUserId(), loginUser.getRole(), storeId);
        return ResponseEntity.ok(SuccessResponse.success(null));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<SliceResponse<StoreSummaryResponseDto, FavoriteCursorResponseDto>>> getFavoriteStores(
            @LoginUser AuthUser authUser,
            @ModelAttribute FavoriteCursorRequestDto request
    ) {
        SliceResponse<StoreSummaryResponseDto, FavoriteCursorResponseDto> response = favoriteService.getFavoriteStores(
                authUser.getUserId(), authUser.getRole(), request);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }


}
