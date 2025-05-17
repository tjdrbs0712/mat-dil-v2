package hello.matdil.domain.store.dto;

import hello.matdil.domain.menu.dto.MenuResponseDto;
import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.entity.StoreStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreResponseDto {

    private Long id;
    private String name;
    private String phoneNumber;
    private String address;
    private LocalTime openTime;
    private LocalTime closeTime;
    private int minOrderPrice;
    private int deliveryTimeEstimate;
    private double rating;
    private int reviewCount;
    private StoreStatus status;
    private List<MenuResponseDto> menus;

    public static StoreResponseDto from(Store store) {
        return StoreResponseDto.builder()
                .id(store.getId())
                .name(store.getName())
                .phoneNumber(store.getPhoneNumber())
                .address(store.getAddress().toString())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .minOrderPrice(store.getMinOrderPrice())
                .deliveryTimeEstimate(store.getDeliveryTimeEstimate())
                .rating(store.getRating())
                .reviewCount(store.getReviewCount())
                .status(store.getStatus())
                .menus(store.getMenus().stream()
                        .map(MenuResponseDto::from)
                        .toList())
                .build();
    }
}

