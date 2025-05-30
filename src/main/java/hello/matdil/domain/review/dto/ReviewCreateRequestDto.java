package hello.matdil.domain.review.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public record ReviewCreateRequestDto(
        @NotNull(message = "주문 ID는 필수입니다.")
        Long orderId,

        @NotNull(message = "가게 ID는 필수입니다.")
        Long storeId,

        @Min(value = 1, message = "별점은 1 이상이어야 합니다.")
        @Max(value = 5, message = "별점은 5 이하여야 합니다.")
        int rating,

        @Size(max = 1000, message = "리뷰 내용은 1000자 이하로 작성해주세요.")
        String comment,

        List<String> imageUrls
) {}
