package hello.matdil.domain.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class StoreUpdateRequestDto {

    @NotBlank(message = "가게 이름은 필수입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String phoneNumber;

    @NotBlank(message = "시/도는 필수입니다.")
    private String city;

    @NotBlank(message = "도로명 주소는 필수입니다.")
    private String street;

    @NotBlank(message = "상세 주소는 필수입니다.")
    private String detailAddress;

    @NotNull(message = "영업 시작 시간은 필수입니다.")
    private LocalTime openTime;

    @NotNull(message = "영업 종료 시간은 필수입니다.")
    private LocalTime closeTime;

    @NotNull(message = "최소 주문 금액은 필수입니다.")
    @Min(value = 0, message = "최소 주문 금액은 0원 이상이어야 합니다.")
    private Integer minOrderPrice;

    @NotNull(message = "배달 예상 시간은 필수입니다.")
    private Integer deliveryTimeEstimate;
}
