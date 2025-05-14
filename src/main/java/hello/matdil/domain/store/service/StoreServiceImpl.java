package hello.matdil.domain.store.service;

import hello.matdil.domain.store.dto.StoreCreateRequestDto;
import hello.matdil.domain.store.dto.StoreResponseDto;
import hello.matdil.domain.store.dto.StoreSummaryDto;
import hello.matdil.domain.store.dto.StoreUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService{
    @Override
    public StoreResponseDto createStore(Long userId, String role, StoreCreateRequestDto requestDto) {
        return null;
    }

    @Override
    public List<StoreSummaryDto> getStores(String role, String address, String sort) {
        return List.of();
    }

    @Override
    public StoreResponseDto getStore(Long userId, String role, Long storeId) {
        return null;
    }

    @Override
    public StoreResponseDto updateStore(Long userId, String role, Long storeId, StoreUpdateRequestDto requestDto) {
        return null;
    }

    @Override
    public void changeStoreStatus(Long userId, String role, Long storeId, String storeStatus) {

    }

    @Override
    public void deleteStore(Long userId, String role, Long storeId) {

    }
}
