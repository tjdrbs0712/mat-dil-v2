package hello.matdil.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserRegisterRequestDto(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, message = "비밀번호는 8자리 이상이어야 합니다.")
        String password,

        @NotBlank(message = "이름은 필수입니다.")
        String name,

        @NotBlank(message = "전화번호는 필수입니다.")
        @Pattern(
                regexp = "^010\\d{8}$",
                message = "전화번호는 010으로 시작하고 숫자 11자리여야 합니다."
        )
        String phoneNumber,

        @NotBlank(message = "주소는 필수입니다.")
        String city,

        @NotBlank(message = "주소는 필수입니다.")
        String street,

        @NotBlank(message = "주소는 필수입니다.")
        String detailAddress
) {
}
