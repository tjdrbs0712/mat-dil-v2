package hello.matdil.domain.store.menu.controller;

import hello.matdil.auth.annotation.LoginUser;
import hello.matdil.auth.model.AuthUser;
import hello.matdil.domain.store.menu.dto.*;
import hello.matdil.domain.store.menu.service.MenuService;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores/{storeId}/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<SuccessResponse<MenuResponseDto>> createMenu(
            @LoginUser AuthUser authUser,
            @PathVariable Long storeId,
            @RequestBody @Valid MenuCreateRequestDto requestDto
    ) {
        MenuResponseDto response = menuService.createMenu(authUser.getUserId(), authUser.getRole(), storeId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<SliceResponse<MenuResponseDto, MenuCursorResponseDto>>> getMenus(
            @LoginUser(required = false) AuthUser authUser,
            @PathVariable Long storeId,
            @ModelAttribute MenuCursorRequestDto cursor
    ) {
        return ResponseEntity.ok(SuccessResponse.success(menuService.getMenus(
                authUser.getUserId(), authUser.getRole(), storeId, cursor)));
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<SuccessResponse<MenuResponseDto>> getMenu(
            @LoginUser(required = false) AuthUser authUser,
            @PathVariable Long storeId,
            @PathVariable Long menuId
    ) {
        MenuResponseDto response = menuService.getMenu(
                authUser.getUserId(), authUser.getRole(), storeId, menuId);

        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<SuccessResponse<MenuResponseDto>> updateMenu(
            @LoginUser AuthUser authUser,
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @RequestBody @Valid MenuUpdateRequestDto requestDto
    ) {
        MenuResponseDto response = menuService.updateMenu(
                authUser.getUserId(),
                authUser.getRole(),
                storeId,
                menuId,
                requestDto
        );
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

}
