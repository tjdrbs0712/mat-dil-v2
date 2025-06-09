package hello.matdil.domain.payment.controller;

import hello.matdil.domain.payment.dto.PaymentConfirmationRequest;
import hello.matdil.domain.payment.dto.PaymentConfirmationResponse;
import hello.matdil.domain.payment.dto.PaymentPreparationRequest;
import hello.matdil.domain.payment.dto.PaymentPreparationResponse;
import hello.matdil.domain.payment.service.PaymentService;
import hello.matdil.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/payments")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제 준비 API
     */
    @PostMapping("/prepare")
    public ResponseEntity<SuccessResponse<PaymentPreparationResponse>> preparePayment(
            @RequestBody PaymentPreparationRequest request) {
        PaymentPreparationResponse response = paymentService.preparePayment(request);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    /**
     * 결제 승인 API
     */
    @PostMapping("/confirm")
    public ResponseEntity<SuccessResponse<PaymentConfirmationResponse>> confirmPayment(
            @RequestBody PaymentConfirmationRequest request) {
        PaymentConfirmationResponse response = paymentService.confirmPayment(request);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }
}