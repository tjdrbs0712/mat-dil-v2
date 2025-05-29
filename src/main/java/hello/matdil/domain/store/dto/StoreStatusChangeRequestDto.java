package hello.matdil.domain.store.dto;

import hello.matdil.domain.store.entity.StoreStatus;
import hello.matdil.global.validator.EnumValid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreStatusChangeRequestDto {

    @EnumValid(message = "가게 상태를 제대로 입력해주세요.", enumClass = StoreStatus.class)
    private String storeStatus;

    public StoreStatus toEnum() {
        return StoreStatus.valueOf(storeStatus.toUpperCase());
    }
}
