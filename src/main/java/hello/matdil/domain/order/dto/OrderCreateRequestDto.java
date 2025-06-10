package hello.matdil.domain.order.dto;

import hello.matdil.domain.address.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;


public record OrderCreateRequestDto(
        @NotNull(message = "storeId는 필수입니다.")
        Long storeId,

        @NotNull(message = "배달 예상 시간은 필수입니다")
        LocalDateTime expectedDeliveryTime,

        String requestNote,

        @NotEmpty(message = "메뉴는 필수입니다.")
        List<OrderItemRequestDto> orderItems,

        @Valid
        @NotNull(message = "주소 정보는 필수입니다.")
        AddressDto address
) {
    public record AddressDto(
            @NotNull String city,
            @NotNull String street,
            @NotNull String detailAddress
    ) {
        public Address toEntity() {
            return new Address(city, street, detailAddress);
        }
    }
}