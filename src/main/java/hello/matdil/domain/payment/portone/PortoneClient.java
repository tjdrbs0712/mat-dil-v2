package hello.matdil.domain.payment.portone;

import hello.matdil.config.PortOneProperties;
import hello.matdil.domain.payment.dto.portone.PortonePaymentData;
import hello.matdil.domain.payment.dto.portone.PortonePaymentResponseWrapper;
import hello.matdil.domain.payment.dto.portone.PortoneTokenRequest;
import hello.matdil.domain.payment.dto.portone.PortoneTokenResponse;
import hello.matdil.domain.payment.exception.PaymentErrorCode;
import hello.matdil.domain.payment.exception.PaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PortoneClient {

    private final WebClient webClient;
    private final PortOneProperties portOneProperties;

    public PortonePaymentData getPaymentInfo(String impUid) {
        PortonePaymentResponseWrapper response = getAccessToken()
                .flatMap(accessToken -> fetchPaymentData(impUid, accessToken))
                .block();

        if (response == null || response.response() == null) {
            throw new PaymentException(PaymentErrorCode.PORTONE_NOT_FOUND);
        }
        return response.response();
    }

    private Mono<PortonePaymentResponseWrapper> fetchPaymentData(String impUid, String accessToken) {
        return webClient.get()
                .uri("https://api.iamport.kr/payments/" + impUid)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(PortonePaymentResponseWrapper.class);
    }

    private Mono<String> getAccessToken() {
        return webClient.post()
                .uri("https://api.iamport.kr/users/getToken")
                .bodyValue(new PortoneTokenRequest(portOneProperties.getApiKey(), portOneProperties.getApiSecret()))
                .retrieve()
                .bodyToMono(PortoneTokenResponse.class)
                .map(response -> response.response().access_token());
    }

    public void cancelPayment(String impUid) {

    }

}
