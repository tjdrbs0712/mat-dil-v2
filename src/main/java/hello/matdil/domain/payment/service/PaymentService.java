package hello.matdil.domain.payment.service;

import hello.matdil.config.PortOneProperties;
import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.repository.OrderRepository;
import hello.matdil.domain.payment.dto.PaymentConfirmationRequest;
import hello.matdil.domain.payment.dto.PaymentConfirmationResponse;
import hello.matdil.domain.payment.dto.PaymentPreparationRequest;
import hello.matdil.domain.payment.dto.PaymentPreparationResponse;
import hello.matdil.domain.payment.dto.portone.PortonePaymentData;
import hello.matdil.domain.payment.dto.portone.PortonePaymentResponseWrapper;
import hello.matdil.domain.payment.dto.portone.PortoneTokenRequest;
import hello.matdil.domain.payment.dto.portone.PortoneTokenResponse;
import hello.matdil.domain.payment.entity.Payment;
import hello.matdil.domain.payment.entity.PaymentMethod;
import hello.matdil.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PortOneProperties portOneProperties;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final WebClient webClient;

    /**
     * 결제 준비
     */
    @Transactional
    public PaymentPreparationResponse preparePayment(PaymentPreparationRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

//        if (paymentRepository.existsByOrderIdAndStatus(orderId, PaymentStatus.COMPLETED)) {
//            throw new IllegalStateException("이미 결제 완료된 주문입니다.");
//        }

        // ✨ 클라이언트에서 받은 문자열(예: "kakao_pay")을 Enum으로 변환하여 저장
        PaymentMethod paymentMethod = PaymentMethod.valueOf(request.method().toUpperCase(Locale.ROOT));

        Payment payment = Payment.builder()
                .orderId(order.getId())
                .amount(BigDecimal.valueOf(order.getTotalPrice()))
                .method(paymentMethod)
                .build();

        paymentRepository.save(payment);

        // pgProvider를 동적으로 생성하여 반환
        String pgProvider = determinePgProvider(paymentMethod);

        return PaymentPreparationResponse.from(payment, pgProvider, portOneProperties);
    }

    private String determinePgProvider(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case KAKAO_PAY -> "tosspayments.kakaopay";
            case NAVER_PAY -> "tosspayments.naverpay";
            case TOSS_PAY -> "tosspayments.tosspay";
            default -> "tosspayments.card"; // 기본값
        };
    }

    /**
     * 결제 승인 (검증)
     */
    @Transactional
    public PaymentConfirmationResponse confirmPayment(PaymentConfirmationRequest request) {
        log.error("{}, @@@@@@ {}", request.impUid(), request.merchantUid());
        // 1. 포트원 서버로부터 실제 결제 정보를 조회합니다.
        PortonePaymentData portonePaymentData = getPaymentInfoFromPortone(request.impUid())
                .block();

        if (portonePaymentData == null) {
            throw new IllegalStateException("포트원에서 결제 정보를 조회할 수 없습니다.");
        }

        // 2. 우리 DB에서 결제 정보를 조회합니다.
        Payment payment = paymentRepository.findById(request.merchantUid())
                .orElseThrow(() -> new IllegalArgumentException("결제 정보를 찾을 수 없습니다."));

        // 3. 금액 위변조 검증: 포트원 서버에서 조회한 금액과 우리 DB에 저장된 금액을 비교합니다.
        if (payment.getAmount().compareTo(portonePaymentData.amount()) != 0) {
            // 금액이 일치하지 않으면, 해킹 시도일 수 있으므로 결제를 취소하고 예외를 발생시킵니다.
            // TODO: 포트원 결제 취소 API 호출
            throw new IllegalStateException("결제 금액이 일치하지 않습니다. 위변조 시도가 의심됩니다.");
        }

        // 4. 결제 상태 업데이트
        payment.completePayment(request.impUid()); // 상태를 COMPLETED로 변경하고 paymentKey 저장

        // 5. 후속 처리 (예: 주문 상태 변경, 이벤트 발행)
        Order order = orderRepository.findById(payment.getOrderId()).orElseThrow();
//        order.markAsPaid();

        return PaymentConfirmationResponse.from(payment);
    }

    /**
     * 포트원 서버에서 결제 정보 조회 (imp_uid 사용)
     */
    private Mono<PortonePaymentData> getPaymentInfoFromPortone(String impUid) {
        return getAccessToken()
                .flatMap(accessToken -> webClient.get()
                        .uri("https://api.iamport.kr/payments/" + impUid)
                        .header("Authorization", "Bearer " + accessToken)
                        .retrieve()
                        .bodyToMono(PortonePaymentResponseWrapper.class)
                        .map(PortonePaymentResponseWrapper::response)
                );
    }

    /**
     * 포트원 엑세스 토큰 발급
     */
    private Mono<String> getAccessToken() {
        return webClient.post()
                .uri("https://api.iamport.kr/users/getToken")
                .bodyValue(new PortoneTokenRequest(portOneProperties.getChannelKey(), portOneProperties.getSecretKey()))
                .retrieve()
                .bodyToMono(PortoneTokenResponse.class)
                .map(response -> response.response().access_token());
    }
}