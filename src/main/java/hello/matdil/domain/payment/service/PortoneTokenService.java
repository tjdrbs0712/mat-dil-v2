package hello.matdil.domain.payment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class PortoneTokenService {

    private final String apiSecret;
    private final WebClient webClient;
    private final Map<String, Object> tokenCache = new ConcurrentHashMap<>();

    public PortoneTokenService(@Value("${portone.api-secret}") String apiSecret,
                               @Value("${portone.base-url}") String baseUrl) {
        this.apiSecret = apiSecret;
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public Mono<String> getAccessToken() {
        if (isTokenValid()) {
            log.info("Using cached PortOne access token.");
            return Mono.just((String) tokenCache.get("accessToken"));
        }
        return requestNewToken();
    }

    private boolean isTokenValid() {
        if (!tokenCache.containsKey("expiresAt")) {
            return false;
        }
        return Instant.now().isBefore(((Instant) tokenCache.get("expiresAt")).minusSeconds(60));
    }

    private Mono<String> requestNewToken() {
        log.info("Requesting new PortOne access token.");
        return webClient.post()
                .uri("/v2/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("apiSecret", this.apiSecret))
                .retrieve()
                .bodyToMono(Map.class)
                .doOnSuccess(responseMap -> {
                    String accessToken = (String) responseMap.get("accessToken");
                    Integer expiresIn = (Integer) responseMap.get("expiresIn");
                    tokenCache.put("accessToken", accessToken);
                    tokenCache.put("expiresAt", Instant.now().plusSeconds(expiresIn));
                    log.info("New PortOne access token issued.");
                })
                .map(responseMap -> (String) responseMap.get("accessToken"));
    }
}