package hello.matdil.domain.store.service;

import hello.matdil.domain.store.dto.*;
import hello.matdil.domain.store.entity.StoreStatus;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;

public interface StoreService {
    StoreResponseDto createStore(Long userId, UserRole role, StoreCreateRequestDto requestDto);

    SliceResponse<StoreSummaryResponseDto, StoreCursorResponseDto> getStores(Long userId, UserRole role, StoreSearchRequestDto request);

    StoreResponseDto getStore(Long userId, UserRole role, Long storeId);

    StoreResponseDto updateStore(Long userId, UserRole role, Long storeId, StoreUpdateRequestDto requestDto);

    void changeStoreStatus(Long userId, UserRole role, Long storeId, StoreStatus storeStatus);
}
