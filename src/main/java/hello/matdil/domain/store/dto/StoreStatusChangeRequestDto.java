package hello.matdil.domain.store.dto;

import hello.matdil.domain.store.entity.StoreStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreStatusChangeRequestDto {

    @NotNull(message = "가게 상태를 입력해주세요.")
    private StoreStatus storeStatus;
}
