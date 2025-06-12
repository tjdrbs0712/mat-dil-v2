package hello.matdil.domain.payment.facade;

import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.reader.OrderReader;
import hello.matdil.domain.payment.dto.PaymentConfirmationRequest;
import hello.matdil.domain.payment.dto.PaymentConfirmationResponse;
import hello.matdil.domain.payment.dto.PaymentPreparationRequest;
import hello.matdil.domain.payment.dto.PaymentPreparationV1Response;
import hello.matdil.domain.payment.dto.portone.PortonePaymentData;
import hello.matdil.domain.payment.entity.Payment;
import hello.matdil.domain.payment.entity.PaymentStatus;
import hello.matdil.domain.payment.event.PaymentCompletedEvent;
import hello.matdil.domain.payment.event.PaymentEventProducer;
import hello.matdil.domain.payment.pg.PgProviderSelector;
import hello.matdil.domain.payment.portone.PortoneClient;
import hello.matdil.domain.payment.service.PaymentService;
import hello.matdil.domain.payment.validator.PaymentValidator;
import hello.matdil.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentFacade {

    private final OrderReader orderReader;
    private final PaymentValidator paymentValidator;
    private final PaymentService paymentService;
    private final PgProviderSelector pgProviderSelector;
    private final PortoneClient portoneClient;
    private final PaymentEventProducer paymentEventProducer;


    public PaymentPreparationV1Response preparePayment(Long userId, UserRole role, PaymentPreparationRequest request) {
        Order order = orderReader.readWithUserPermission(request.orderId(), userId, role);
        paymentValidator.existsByIdAndStatusComplete(order.getId(), PaymentStatus.COMPLETED);
        Payment payment = paymentService.createPendingPayment(order, request.getMethod());
        String pgProvider = pgProviderSelector.determinePgProvider(request.getMethod());
        return PaymentPreparationV1Response.from(payment, pgProvider);
    }

    public PaymentConfirmationResponse confirmPayment(Long userId, PaymentConfirmationRequest request) {
        PortonePaymentData portonePaymentData = portoneClient.getPaymentInfo(request.impUid());
        Payment completedPayment = paymentService.verifyAndCompletePayment(userId, request.merchantUid(), portonePaymentData);
        paymentEventProducer.sendPaymentCompletedEvent(PaymentCompletedEvent.from(completedPayment));
        return PaymentConfirmationResponse.from(completedPayment);
    }
}