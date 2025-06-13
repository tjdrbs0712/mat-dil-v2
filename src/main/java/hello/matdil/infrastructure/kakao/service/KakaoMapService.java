package hello.matdil.infrastructure.kakao.service;

import hello.matdil.infrastructure.kakao.dto.KakaoAddressResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class KakaoMapService {

    private final WebClient webClient;
    private final String kakaoApiKey;

    public KakaoMapService(WebClient.Builder webClientBuilder, @Value("${kakao.api.key}") String kakaoApiKey) {
        this.webClient = webClientBuilder
                .baseUrl("https://dapi.kakao.com") // 카카오 API 기본 URL
                .build();
        this.kakaoApiKey = "KakaoAK " + kakaoApiKey;
    }

    public Mono<KakaoAddressResponse.Document> getCoordinates(String addressString) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/address.json")
                        .queryParam("query", addressString)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, kakaoApiKey)
                .retrieve() // 응답 받아오기
                .bodyToMono(KakaoAddressResponse.class) // 응답 바디를 DTO로 변환
                .handle((response, sink) -> {
                    if (response.getDocuments() == null || response.getDocuments().isEmpty()) {
                        sink.error(new IllegalArgumentException("주소에 해당하는 좌표를 찾을 수 없습니다: " + addressString));
                        return;
                    }
                    // 첫 번째 검색 결과를 위도/경도로 사용
                    sink.next(response.getDocuments().get(0));
                });
    }
}
