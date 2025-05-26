package hello.matdil.domain.favorite.controller;

import hello.matdil.auth.annotation.LoginUser;
import hello.matdil.auth.model.AuthUser;
import hello.matdil.domain.favorite.dto.FavoriteRequestDto;
import hello.matdil.domain.favorite.service.FavoriteService;
import hello.matdil.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
