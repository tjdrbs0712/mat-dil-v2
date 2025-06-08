package hello.matdil.domain.payment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class PaymentService {

    private final PortoneTokenService tokenService;
    private final WebClient webClient;

    public PaymentService(PortoneTokenService tokenService, @Value("${portone.base-url}") String baseUrl) {
        this.tokenService = tokenService;
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    // 1. 사전 검증: 결제 준비
    public Mono<Map<String, Object>> preparePayment(String orderId) {
        // 실제로는 DB에서 주문 ID로 주문 정보를 조회해야 합니다.
        // 여기서는 포트폴리오용으로 고정된 값을 사용합니다.
        BigDecimal amountToPay = new BigDecimal("1000"); // DB에서 조회한 실제 결제해야 할 금액
        String merchantUid = UUID.randomUUID().toString();

        log.info("주문번호 {} 에 대한 결제를 준비합니다. 결제금액: {}", merchantUid, amountToPay);
        // TODO: 실제 프로젝트에서는 이 merchantUid와 금액을 DB의 주문 정보에 저장해두어야 합니다.

        return tokenService.getAccessToken().flatMap(token ->
                webClient.post()
                        .uri("/v2/payments/prepare")
                        .header("Authorization", "Bearer " + token)
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of(
                                "merchant_uid", merchantUid,
                                "amount", amountToPay
                        ))
                        .retrieve()
                        .bodyToMono(Map.class) // 포트원 응답은 현재 비어있음. 성공 여부만 확인.
                        .doOnSuccess(response -> log.info("포트원 사전 등록 성공: {}", response))
                        .thenReturn(Map.of( // 프론트엔드에 전달할 정보
                                "merchantUid", merchantUid,
                                "amount", amountToPay
                        ))
        );
    }

    // 2. 사후 검증: 결제 검증
    public Mono<Boolean> validatePayment(String paymentId) {
        return tokenService.getAccessToken().flatMap(token ->
                webClient.get()
                        .uri("/v2/payments/" + paymentId)
                        .header("Authorization", "Bearer " + token)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .flatMap(response -> {
                            String status = (String) response.get("status");
                            if (!"PAID".equalsIgnoreCase(status)) {
                                log.warn("결제가 완료되지 않았습니다. 상태: {}", status);
                                return Mono.just(false);
                            }

                            Map<String, Object> amountInfo = (Map<String, Object>) response.get("amount");
                            BigDecimal actualAmount = new BigDecimal(amountInfo.get("total").toString());
                            String merchantUid = (String) response.get("merchant_uid");

                            // TODO: DB에서 merchantUid로 주문 정보를 조회하여 저장된 금액(expectedAmount)을 가져와야 합니다.
                            BigDecimal expectedAmount = new BigDecimal("1000"); // 예시 금액

                            if (expectedAmount.compareTo(actualAmount) == 0) {
                                log.info("금액 검증 성공. paymentId: {}", paymentId);
                                // TODO: DB에 주문 상태를 '결제 완료'로 최종 업데이트
                                return Mono.just(true);
                            } else {
                                log.warn("금액 위변조 시도 감지! paymentId: {}. Expected: {}, Actual: {}", paymentId, expectedAmount, actualAmount);
                                // TODO: 금액이 다를 경우, 자동으로 결제 취소 API를 호출하는 로직이 여기에 들어가야 합니다.
                                return Mono.just(false);
                            }
                        })
        );
    }
}