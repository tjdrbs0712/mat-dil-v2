package hello.matdil.domain.favorite.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hello.matdil.domain.store.dto.StoreSummaryResponseDto;
import hello.matdil.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteStoreSummaryDto {

    private StoreSummaryResponseDto summary;

    private Integer orderCount;
    private LocalDateTime lastOrderedAt;

    @JsonIgnore
    public Long getStoreId() {
        return summary.getId();
    }

    public static FavoriteStoreSummaryDto from(Store store, Integer orderCount, LocalDateTime lastOrderedAt) {
        return FavoriteStoreSummaryDto.builder()
                .summary(StoreSummaryResponseDto.from(store))
                .orderCount(orderCount)
                .lastOrderedAt(lastOrderedAt)
                .build();
    }
}
