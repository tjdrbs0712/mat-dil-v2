package hello.matdil.domain.store.dto;

import hello.matdil.domain.store.entity.StoreStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreStatusChangeRequestDto {
    private StoreStatus storeStatus;
}
