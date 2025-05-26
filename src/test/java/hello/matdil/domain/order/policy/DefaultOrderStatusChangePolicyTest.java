package hello.matdil.domain.order.policy;

import hello.matdil.domain.order.entity.OrderStatus;
import hello.matdil.domain.order.exception.OrderErrorCode;
import hello.matdil.domain.order.exception.OrderException;
import hello.matdil.domain.user.entity.UserRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultOrderStatusChangePolicyTest {
    private final OrderStatusChangePolicy policy = new DefaultOrderStatusChangePolicy();

    @Test
    void 동일한_상태로_변경하면_예외() {
        assertThatThrownBy(() ->
                policy.validateChange(OrderStatus.CREATED, OrderStatus.CREATED, UserRole.USER))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining(OrderErrorCode.ALREADY_IN_TARGET_STATUS.getErrorMessage());
    }

    @Test
    void 허용되지_않은_상태전이는_예외() {
        assertThatThrownBy(() ->
                policy.validateChange(OrderStatus.CREATED, OrderStatus.DELIVERING, UserRole.ADMIN))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining(OrderErrorCode.INVALID_STATUS.getErrorMessage());
    }

    @Test
    void 일반유저는_DELETED로_변경불가() {
        assertThatThrownBy(() ->
                policy.validateChange(OrderStatus.CREATED, OrderStatus.DELETED, UserRole.USER))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining(OrderErrorCode.NO_PERMISSION.getErrorMessage());
    }

    @Test
    void 관리자는_DELETED로_변경가능() {
        assertThatCode(() ->
                policy.validateChange(OrderStatus.CREATED, OrderStatus.DELETED, UserRole.ADMIN))
                .doesNotThrowAnyException();
    }

    @Test
    void 유저는_허용된_상태에서만_CANCELED_가능() {
        OrderStatus cancelable = OrderStatus.CREATED; // 예시: canBeCanceledByUser()가 true인 상태
        assertThatCode(() ->
                policy.validateChange(cancelable, OrderStatus.CANCELED, UserRole.USER))
                .doesNotThrowAnyException();
    }

}