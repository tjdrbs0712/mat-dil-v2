package hello.matdil.domain.store.dto;

import hello.matdil.domain.store.entity.Store;
import hello.matdil.domain.store.entity.StoreStatus;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreSummaryResponseDto {

    private Long id;
    private String name;
    private String phoneNumber;
    private String imageUrl;
    private String address; // ex) "서울시 강남구 테헤란로 123"
    private double rating;
    private int reviewCount;
    private int deliveryTimeEstimate;
    private int minOrderPrice;
    private StoreStatus status;

    public static StoreSummaryResponseDto from(Store store) {
        return StoreSummaryResponseDto.builder()
                .id(store.getId())
                .name(store.getName())
                .phoneNumber(store.getPhoneNumber())
                .imageUrl(store.getImageUrl())
                .address(store.getAddress().toString())
                .rating(store.getRating())
                .reviewCount(store.getReviewCount())
                .deliveryTimeEstimate(store.getDeliveryTimeEstimate())
                .minOrderPrice(store.getMinOrderPrice())
                .status(store.getStatus())
                .build();
    }
}
