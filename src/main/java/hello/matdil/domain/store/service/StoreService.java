package hello.matdil.domain.store.service;

import hello.matdil.domain.store.dto.StoreCreateRequestDto;
import hello.matdil.domain.store.dto.StoreResponseDto;
import hello.matdil.domain.store.dto.StoreSummaryDto;
import hello.matdil.domain.store.dto.StoreUpdateRequestDto;

import java.util.List;

public interface StoreService {
    StoreResponseDto createStore(Long userId, String role, StoreCreateRequestDto requestDto);

    List<StoreSummaryDto> getStores(String role, String address, String sort);

    StoreResponseDto getStore(Long userId, String role, Long storeId);

    StoreResponseDto updateStore(Long userId, String role, Long storeId, StoreUpdateRequestDto requestDto);

    void changeStoreStatus(Long userId, String role, Long storeId, String storeStatus);

    void deleteStore(Long userId, String role, Long storeId);
}
