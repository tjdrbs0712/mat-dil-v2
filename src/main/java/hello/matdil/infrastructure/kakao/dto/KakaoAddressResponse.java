package hello.matdil.infrastructure.kakao.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 이 클래스에 정의되지 않은 최상위 필드 무시
public class KakaoAddressResponse {

    @JsonProperty("documents")
    private List<Document> documents;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Document {

        @JsonProperty("y")
        private double latitude;

        @JsonProperty("x")
        private double longitude;
    }
}