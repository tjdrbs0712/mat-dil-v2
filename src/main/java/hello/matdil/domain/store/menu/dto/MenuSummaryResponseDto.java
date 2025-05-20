package hello.matdil.domain.store.menu.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuSummaryResponseDto {
    private List<MenuResponseDto> menuResponseDtoList;
}
