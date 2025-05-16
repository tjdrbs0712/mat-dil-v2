package hello.matdil.domain.store.service;

import hello.matdil.domain.store.dto.*;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.factory.StoreFactory;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.user.entity.UserRole;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.validator.PermissionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService{
    private final StoreRepository storeRepository;
    private final StoreFactory storeFactory;

    @Override
    @Transactional
    public StoreResponseDto createStore(Long userId, UserRole role, StoreCreateRequestDto requestDto) {
        PermissionValidator.validateOwnerOrAdmin(role);

        Store store = storeFactory.createStore(userId, requestDto);
        return StoreResponseDto.from(storeRepository.save(store));
    }

    @Override
    @Transactional(readOnly = true)
    public SliceResponse<StoreSummaryResponseDto> getStores(StoreSearchRequestDto request) {

        Slice<Store> slice = storeRepository.findStoresByCondition(request);

        List<StoreSummaryResponseDto> content = slice.getContent().stream()
                .map(StoreSummaryResponseDto::from)
                .toList();

        Object nextCursor = content.isEmpty() ? null : extractCursor(content.get(content.size() - 1), request.getSort());

        return SliceResponse.of(
                content,
                slice.hasNext(),
                nextCursor
        );
    }

    private Object extractCursor(StoreSummaryResponseDto lastDto, String sort) {
        return switch (sort.toLowerCase()) {
            case "rating" -> lastDto.getRating();
            case "name" -> lastDto.getName();
            case "review" -> lastDto.getReviewCount();
            case "deliverytime" -> lastDto.getDeliveryTimeEstimate();
            case "id" -> lastDto.getId();
            default -> lastDto.getId();
        };
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
