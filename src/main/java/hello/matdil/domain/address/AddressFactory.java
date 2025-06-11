package hello.matdil.domain.address;

import hello.matdil.infrastructure.kakao.dto.KakaoAddressResponse;
import hello.matdil.infrastructure.kakao.service.KakaoMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AddressFactory {

    private final KakaoMapService kakaoMapService;

    public Address create(String city, String street, String detailAddress){

        String fullAddress = String.format("%s %s %s", city, street, detailAddress);

        KakaoAddressResponse.Document coordinates = kakaoMapService.getCoordinates(fullAddress).block();
        Objects.requireNonNull(coordinates, "주소에 대한 좌표를 찾을 수 없습니다: " + fullAddress);

        return Address.create(
                city,
                street,
                detailAddress,
                coordinates.getLatitude(),
                coordinates.getLongitude()
        );
    }

}
