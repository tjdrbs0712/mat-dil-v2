package hello.matdil.domain.payment.controller;

import hello.matdil.domain.payment.dto.PaymentRequestDto;
import hello.matdil.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/prepare")
    public Mono<ResponseEntity<Map<String, Object>>> preparePayment() {
        // 실제로는 인증된 사용자 정보와 주문 ID를 받아 처리
        return paymentService.preparePayment("some-order-id")
                .map(ResponseEntity::ok);
    }

    @PostMapping("/validate")
    public Mono<ResponseEntity<String>> validatePayment(@RequestBody PaymentRequestDto request) {
        return paymentService.validatePayment(request.paymentId())
                .map(isValid -> {
                    if (isValid) {
                        return ResponseEntity.ok("결제 검증 성공");
                    } else {
                        return ResponseEntity.badRequest().body("결제 검증 실패");
                    }
                });
    }
}