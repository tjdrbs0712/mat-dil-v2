package hello.matdil.domain.store.service;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.common.validator.PermissionValidator;
import hello.matdil.domain.store.dto.StoreCreateRequestDto;
import hello.matdil.domain.store.dto.StoreResponseDto;
import hello.matdil.domain.store.dto.StoreSummaryDto;
import hello.matdil.domain.store.dto.StoreUpdateRequestDto;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.entity.StoreStatus;
import hello.matdil.domain.store.repository.StoreRepository;
import hello.matdil.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService{
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public StoreResponseDto createStore(Long userId, UserRole role, StoreCreateRequestDto requestDto) {

        PermissionValidator.validateOwnerOrAdmin(role); // 본인이 owner이므로 둘 다 userId

        // 2. 주소 객체 생성
        Address address = new Address(
                requestDto.getCity(),
                requestDto.getStreet(),
                requestDto.getDetailAddress()
        );

        // 3. Store 엔티티 생성
        Store store = Store.builder()
                .name(requestDto.getName())
                .phoneNumber(requestDto.getPhoneNumber())
                .address(address)
                .ownerId(userId)
                .openTime(requestDto.getOpenTime())
                .closeTime(requestDto.getCloseTime())
                .minOrderPrice(requestDto.getMinOrderPrice())
                .deliveryTimeEstimate(requestDto.getDeliveryTimeEstimate())
                .status(StoreStatus.OPEN) // 기본값 OPEN
                .build();

        // 4. 저장
        Store saved = storeRepository.save(store);

        return StoreResponseDto.from(saved);
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
