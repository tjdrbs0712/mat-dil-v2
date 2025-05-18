package hello.matdil.domain.store.mapper;

import hello.matdil.domain.address.Address;
import hello.matdil.domain.store.dto.StoreUpdateRequestDto;
import hello.matdil.domain.store.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {

    public void update(Store store, StoreUpdateRequestDto dto) {
        store.updateInfo(
                dto.getName(),
                dto.getPhoneNumber(),
                new Address(dto.getCity(), dto.getStreet(), dto.getDetailAddress()),
                dto.getOpenTime(),
                dto.getCloseTime(),
                dto.getMinOrderPrice(),
                dto.getDeliveryTimeEstimate()
        );
    }
}
