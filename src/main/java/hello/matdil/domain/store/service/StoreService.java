package hello.matdil.domain.store.service;

import hello.matdil.domain.store.dto.*;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.Cursor;
import hello.matdil.global.response.SliceResponse;

public interface StoreService {
    StoreResponseDto createStore(Long userId, UserRole role, StoreCreateRequestDto requestDto);

    SliceResponse<StoreSummaryResponseDto, Cursor> getStores(StoreSearchRequestDto request);

    StoreResponseDto getStore(Long storeId);

    StoreResponseDto updateStore(Long userId, String role, Long storeId, StoreUpdateRequestDto requestDto);

    void changeStoreStatus(Long userId, String role, Long storeId, String storeStatus);

    void deleteStore(Long userId, String role, Long storeId);
}
