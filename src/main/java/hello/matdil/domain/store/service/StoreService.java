package hello.matdil.domain.store.service;

import hello.matdil.domain.store.dto.StoreCreateRequestDto;
import hello.matdil.domain.store.dto.StoreResponseDto;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.domain.store.dto.StoreUpdateRequestDto;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;

public interface StoreService {
    StoreResponseDto createStore(Long userId, UserRole role, StoreCreateRequestDto requestDto);

    SliceResponse<StoreSummaryResponseDto> getStores(
            String address, String name, String sort, int page, int size);

    StoreResponseDto getStore(Long userId, String role, Long storeId);

    StoreResponseDto updateStore(Long userId, String role, Long storeId, StoreUpdateRequestDto requestDto);

    void changeStoreStatus(Long userId, String role, Long storeId, String storeStatus);

    void deleteStore(Long userId, String role, Long storeId);
}
