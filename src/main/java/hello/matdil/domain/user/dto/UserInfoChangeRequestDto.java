package hello.matdil.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserInfoChangeRequestDto {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotBlank(message = "주소는 필수입니다.")
    private String city;

    @NotBlank(message = "주소는 필수입니다.")
    private String street;

    @NotBlank(message = "주소는 필수입니다.")
    private String detailAddress;
}
