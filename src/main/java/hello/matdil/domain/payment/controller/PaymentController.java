package hello.matdil.domain.payment.controller;

import hello.matdil.auth.annotation.LoginUser;
import hello.matdil.auth.model.AuthUser;
import hello.matdil.domain.payment.dto.PaymentConfirmationRequest;
import hello.matdil.domain.payment.dto.PaymentConfirmationResponse;
import hello.matdil.domain.payment.dto.PaymentPreparationRequest;
import hello.matdil.domain.payment.dto.PaymentPreparationResponse;
import hello.matdil.domain.payment.facade.PaymentFacade;
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

    private final PaymentFacade paymentFacade;

    /**
     * 결제 준비 API
     */
    @PostMapping("/prepare")
    public ResponseEntity<SuccessResponse<PaymentPreparationResponse>> preparePayment(
            @LoginUser AuthUser authUser,
            @RequestBody PaymentPreparationRequest request) {
        PaymentPreparationResponse response = paymentFacade.preparePayment(authUser.getUserId(), authUser.getRole(), request);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    /**
     * 결제 승인 API
     */
    @PostMapping("/confirm")
    public ResponseEntity<SuccessResponse<PaymentConfirmationResponse>> confirmPayment(
            @LoginUser AuthUser authUser,
            @RequestBody PaymentConfirmationRequest request) {
        PaymentConfirmationResponse response = paymentFacade.confirmPayment(authUser.getUserId(), request);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }
}