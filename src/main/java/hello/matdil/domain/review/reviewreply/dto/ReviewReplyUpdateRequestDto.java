package hello.matdil.domain.review.reviewreply.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReviewReplyUpdateRequestDto(
        @NotBlank(message = "답글은 공백일 수 없습니다.")
        @Size(max = 1000, message = "답글은 1000자 이하로 작성해주세요.")
        String replyText
) {
}
