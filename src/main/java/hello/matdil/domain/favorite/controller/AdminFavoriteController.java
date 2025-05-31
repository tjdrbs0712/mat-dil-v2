package hello.matdil.domain.favorite.controller;

import hello.matdil.auth.annotation.LoginUser;
import hello.matdil.auth.model.AuthUser;
import hello.matdil.domain.favorite.dto.AdminFavoriteRequestDto;
import hello.matdil.domain.favorite.service.admin.AdminFavoriteService;
import hello.matdil.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/admin/favorites")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminFavoriteController {

    private final AdminFavoriteService adminFavoriteService;

    @PostMapping
    public ResponseEntity<SuccessResponse<Void>> addFavorite(
            @LoginUser AuthUser loginUser,
            @RequestBody @Valid AdminFavoriteRequestDto request
    ) {
        adminFavoriteService.addFavorite(request);
        return ResponseEntity.ok(SuccessResponse.success(null));
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse<Void>> removeFavorite(
            @LoginUser AuthUser loginUser,
            @RequestBody @Valid AdminFavoriteRequestDto request
    ) {
        adminFavoriteService.removeFavorite(request);
        return ResponseEntity.ok(SuccessResponse.success(null));
    }

}
