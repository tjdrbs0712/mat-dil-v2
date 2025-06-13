package hello.matdil.domain.payment.service;

import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.reader.OrderReader;
import hello.matdil.domain.payment.dto.portone.PortonePaymentData;
import hello.matdil.domain.payment.entity.Payment;
import hello.matdil.domain.payment.entity.PaymentMethod;
import hello.matdil.domain.payment.exception.PaymentErrorCode;
import hello.matdil.domain.payment.exception.PaymentException;
import hello.matdil.domain.payment.portone.PortoneClient;
import hello.matdil.domain.payment.repository.PaymentRepository;
import hello.matdil.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PortoneClient portoneClient;
    private final OrderReader orderReader;

    @Transactional
    public Payment createPendingPayment(Order order, PaymentMethod paymentMethod) {
        Payment payment = Payment.create(
                order.getUserId(),
                order.getId(),
                paymentMethod,
                BigDecimal.valueOf(order.getTotalPrice()));

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment verifyAndCompletePayment(Long userId, Long merchantUid, PortonePaymentData portoneData) {
        Payment payment = paymentRepository.findById(merchantUid)
                .orElseThrow(() -> new PaymentException(PaymentErrorCode.PAYMENT_NOT_FOUND));

        orderReader.readWithUserPermission(payment.getOrderId(), userId, UserRole.USER);

        if (payment.validateAmountCompare(portoneData.amount())) {
            portoneClient.cancelPayment(portoneData.impUid(), "결제 금액이 다릅니다.").block();
            throw new PaymentException(PaymentErrorCode.INVALID_AMOUNT);
        }
        payment.completePayment(portoneData.impUid());

        return payment;
    }

    @Transactional
    public void markAsCanceled(Payment payment) {
        payment.markAsCanceled();
    }
}