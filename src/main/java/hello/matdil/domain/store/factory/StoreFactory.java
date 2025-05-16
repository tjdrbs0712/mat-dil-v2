package hello.matdil.domain.store.factory;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.store.dto.StoreCreateRequestDto;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.entity.StoreStatus;
import org.springframework.stereotype.Component;

@Component
public class StoreFactory {
    public Store createStore(Long ownerId, StoreCreateRequestDto dto) {
        Address address = new Address(dto.getCity(), dto.getStreet(), dto.getDetailAddress());

        return Store.builder()
                .name(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .address(address)
                .ownerId(ownerId)
                .openTime(dto.getOpenTime())
                .closeTime(dto.getCloseTime())
                .minOrderPrice(dto.getMinOrderPrice())
                .deliveryTimeEstimate(dto.getDeliveryTimeEstimate())
                .status(StoreStatus.OPEN)
                .build();
    }
}
