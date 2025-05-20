package hello.matdil.domain.store.service;

import hello.matdil.domain.store.dto.*;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.entity.StoreStatus;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.factory.StoreFactory;
import hello.matdil.domain.store.mapper.StoreMapper;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.store.validator.StoreValidator;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.domain.store.dto.StoreCursorResponseDto;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.util.pagination.PageAssembler;
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
    private final StoreValidator storeValidator;
    private final PageAssembler pageAssembler;

    @Override
    @Transactional
    public StoreResponseDto createStore(Long userId, UserRole role, StoreCreateRequestDto requestDto) {
        PermissionValidator.validateOwnerOrAdmin(role);

        Store store = storeFactory.createStore(userId, requestDto);
        storeRepository.save(store);
        return StoreResponseDto.from(store);
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponse<StoreSummaryResponseDto, StoreCursorResponseDto> getStores(
            Long userId, UserRole role, StoreSearchRequestDto request) {

        List<Store> stores = storeRepository.findStoresByCondition(userId, role, request);
        int pageSize = request.getSize();

        return pageAssembler.assemble(
                stores,
                pageSize,
                last -> extractCursor(last, request.getSort()),    // Cursor 생성
                StoreSummaryResponseDto::from                      // DTO 매핑
        );
    }

    private StoreCursorResponseDto extractCursor(Store store, String sort) {
        return switch (sort.toLowerCase()) {
            case RATING -> StoreCursorResponseDto.from(store.getRating(), store.getId());
            case NAME -> StoreCursorResponseDto.from(store.getName(), store.getId());
            case REVIEW -> StoreCursorResponseDto.from(store.getReviewCount(), store.getId());
            case DELIVERY_TIME -> StoreCursorResponseDto.from(store.getDeliveryTimeEstimate(), store.getId());
            default -> StoreCursorResponseDto.from(store.getId(), store.getId());
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
        Store store = storeValidator.validateStoreOwner(userId, storeId, role);
        store.validateBusinessHours(dto.getOpenTime(), dto.getCloseTime());
        storeMapper.update(store, dto);
        return StoreResponseDto.from(store);
    }

    @Override
    @Transactional
    public void changeStoreStatus(Long userId, UserRole role, Long storeId, StoreStatus storeStatus) {
        Store store = storeValidator.validateStoreOwner(userId, storeId, role);
        store.changeStoreStatus(storeStatus);
    }
}
