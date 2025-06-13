package hello.matdil.domain.payment.repository;

import hello.matdil.domain.payment.entity.Payment;
import hello.matdil.domain.payment.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Boolean existsByOrderIdAndStatus(Long paymentId, PaymentStatus status);
    Optional<Payment> findByPaymentKey(String paymentKey);
    Optional<Payment> findByUserIdAndPaymentKeyAndStatus(Long userId, String paymentKey, PaymentStatus status);
}
