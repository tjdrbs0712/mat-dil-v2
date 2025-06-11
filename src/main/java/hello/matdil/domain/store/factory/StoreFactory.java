package hello.matdil.domain.store.factory;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.address.AddressFactory;
import hello.matdil.domain.store.dto.StoreCreateRequestDto;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.entity.StoreStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreFactory {

    private final AddressFactory addressFactory;

    public Store createStore(Long ownerId, StoreCreateRequestDto dto) {
        Address address = addressFactory.create(dto.getCity(), dto.getStreet(), dto.getDetailAddress());

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

    public Address update(String city, String street, String detailAddress) {
        return addressFactory.create(city, street, detailAddress);
    }
}
