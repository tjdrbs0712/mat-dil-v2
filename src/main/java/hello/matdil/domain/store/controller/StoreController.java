package hello.matdil.domain.store.controller;

import hello.matdil.auth.security.UserDetailsImpl;
import hello.matdil.domain.store.dto.*;
import hello.matdil.domain.store.service.StoreService;
import hello.matdil.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    // 가게 등록 (사장님, 관리자)
    @PostMapping
    public ResponseEntity<SuccessResponse<StoreResponseDto>> createStore(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody StoreCreateRequestDto requestDto) {
        StoreResponseDto responseDto = storeService.createStore(userDetails.getUserId(), userDetails.getRole(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.success(responseDto));
    }

    // 가게 목록 조회 (필터/정렬)
    @GetMapping
    public ResponseEntity<SuccessResponse<List<StoreSummaryDto>>> getStores(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = false) String address,
            @RequestParam(defaultValue = "rating") String sort) {
        List<StoreSummaryDto> stores = storeService.getStores(userDetails.getRole(), address, sort);
        return ResponseEntity.ok(SuccessResponse.success(stores));
    }

    // 가게 단건 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<SuccessResponse<StoreResponseDto>> getStore(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long storeId) {
        StoreResponseDto store = storeService.getStore(userDetails.getUserId(), userDetails.getRole(), storeId);
        return ResponseEntity.ok(SuccessResponse.success(store));
    }

    // 가게 수정 (사장님 본인 or 관리자)
    @PutMapping("/{storeId}")
    public ResponseEntity<SuccessResponse<StoreResponseDto>> updateStore(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long storeId,
            @RequestBody StoreUpdateRequestDto requestDto) {
        StoreResponseDto responseDto = storeService.updateStore(userDetails.getUserId(), userDetails.getRole(), storeId, requestDto);
        return ResponseEntity.ok(SuccessResponse.success(responseDto));
    }

    // 가게 상태 변경
    @PatchMapping("/{storeId}/status")
    public ResponseEntity<SuccessResponse<Void>> changeStoreStatus(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long storeId,
            @RequestBody StoreStatusChangeRequestDto requestDto) {
        storeService.changeStoreStatus(userDetails.getUserId(), userDetails.getRole(), storeId, requestDto.getStatus());
        return ResponseEntity.ok(SuccessResponse.success(null));
    }

    // 가게 삭제 (soft-delete)
    @DeleteMapping("/{storeId}")
    public ResponseEntity<SuccessResponse<Void>> deleteStore(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long storeId) {
        storeService.deleteStore(userDetails.getUserId(), userDetails.getRole(), storeId);
        return ResponseEntity.ok(SuccessResponse.success(null));
    }
}

