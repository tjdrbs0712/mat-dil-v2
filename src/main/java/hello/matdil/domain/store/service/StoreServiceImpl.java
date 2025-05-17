package hello.matdil.domain.store.service;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.store.dto.*;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.entity.StoreStatus;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.factory.StoreFactory;
import hello.matdil.domain.store.mapper.StoreMapper;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.Cursor;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.validator.PermissionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hello.matdil.global.util.SortTypeKey.*;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService{
    private final StoreRepository storeRepository;
    private final StoreFactory storeFactory;
    private final StoreMapper storeMapper;

    @Override
    @Transactional
    public StoreResponseDto createStore(Long userId, UserRole role, StoreCreateRequestDto requestDto) {
        PermissionValidator.validateOwnerOrAdmin(role);

        Store store = storeFactory.createStore(userId, requestDto);
        return StoreResponseDto.from(storeRepository.save(store));
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponse<StoreSummaryResponseDto, Cursor> getStores(Long userId, UserRole role, StoreSearchRequestDto request) {

        Slice<Store> slice = storeRepository.findStoresByCondition(userId, role, request);

        List<StoreSummaryResponseDto> content = slice.getContent().stream()
                .map(StoreSummaryResponseDto::from)
                .toList();

        Cursor nextCursor = content.isEmpty() ? null : extractCursor(content.get(content.size() - 1), request.getSort());

        return SliceResponse.of(
                content,
                slice.hasNext(),
                nextCursor
        );
    }

    private Cursor extractCursor(StoreSummaryResponseDto lastDto, String sort) {
        return switch (sort.toLowerCase()) {
            case RATING -> Cursor.of(lastDto.getRating(), lastDto.getId());
            case NAME -> Cursor.of(lastDto.getName(), lastDto.getId());
            case REVIEW -> Cursor.of(lastDto.getReviewCount(), lastDto.getId());
            case DELIVERY_TIME -> Cursor.of(lastDto.getDeliveryTimeEstimate(), lastDto.getId());
            default -> Cursor.of(lastDto.getId(), lastDto.getId());
        };
    }


    @Override
    @Transactional(readOnly = true)
    public StoreResponseDto getStore(Long userId, UserRole role, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));

        if(store.getStatus() == StoreStatus.INACTIVE){
            PermissionValidator.validateOwnerOrAdmin(userId, store.getOwnerId(), role);
        }

        return StoreResponseDto.from(store);
    }

    @Override
    @Transactional
    public StoreResponseDto updateStore(Long userId, UserRole role, Long storeId, StoreUpdateRequestDto dto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));
        PermissionValidator.validateOwnerOrAdmin(userId, store.getOwnerId(), role);
        store.validateBusinessHours(dto.getOpenTime(), dto.getCloseTime());
        storeMapper.update(store, dto);
        return StoreResponseDto.from(store);
    }

    @Override
    public void changeStoreStatus(Long userId, String role, Long storeId, String storeStatus) {

    }

    @Override
    public void deleteStore(Long userId, String role, Long storeId) {

    }
}
