package hello.matdil.domain.review.facade;

import hello.matdil.domain.order.entity.Order;
import hello.matdil.domain.order.exception.OrderErrorCode;
import hello.matdil.domain.order.exception.OrderException;
import hello.matdil.domain.order.reader.OrderReader;
import hello.matdil.domain.review.dto.ReviewCreateRequestDto;
import hello.matdil.domain.review.dto.ReviewResponseDto;
import hello.matdil.domain.review.service.ReviewCommandService;
import hello.matdil.domain.store.exception.StoreErrorCode;
import hello.matdil.domain.store.exception.StoreException;
import hello.matdil.domain.store.reader.StoreReader;
import hello.matdil.domain.user.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ReviewFacadeTest {

    @Mock
    private StoreReader storeReader;

    @Mock
    private OrderReader orderReader;

    @Mock
    private ReviewCommandService commandService;

    @InjectMocks
    private ReviewFacade reviewFacade;

    @Test
    void 리뷰_생성_성공() {
        // given
        Long userId = 1L;
        UserRole role = UserRole.USER;
        ReviewCreateRequestDto dto = new ReviewCreateRequestDto(1L, 1L, 5, "좋아요", List.of());

        Order order = mock(Order.class);
        given(orderReader.readWithUserPermission(dto.orderId(), userId, role)).willReturn(order);

        ReviewResponseDto expected = ReviewResponseDto.builder()
                .id(1L)
                .comment("좋아요")
                .build();
        given(commandService.create(userId, dto)).willReturn(expected);

        // when
        ReviewResponseDto response = reviewFacade.createReview(userId, role, dto);

        // then
        assertThat(response.getComment()).isEqualTo("좋아요");
    }

    @Test
    void 리뷰_생성_가게가_존재하지_않는_경우() {
        // given
        Long userId = 1L;
        UserRole role = UserRole.USER;
        ReviewCreateRequestDto dto = new ReviewCreateRequestDto(99L, 1L, 5, "내용", List.of());

        willThrow(new StoreException(StoreErrorCode.STORE_NOT_FOUND))
                .given(storeReader).readByIdWithPermission(userId, dto.storeId(), role);

        // when & then
        assertThatThrownBy(() -> reviewFacade.createReview(userId, role, dto))
                .isInstanceOf(StoreException.class)
                .hasMessageContaining(StoreErrorCode.STORE_NOT_FOUND.getErrorMessage());
    }

    @Test
    void 리뷰_생성_내_주문이_아닌_경우() {
        // given
        Long userId = 1L;
        UserRole role = UserRole.USER;
        ReviewCreateRequestDto dto = new ReviewCreateRequestDto(1L, 999L, 5, "내용", List.of());

        willThrow(new OrderException(OrderErrorCode.NO_PERMISSION))
                .given(orderReader).readWithUserPermission(dto.orderId(), userId, role);

        // when & then
        assertThatThrownBy(() -> reviewFacade.createReview(userId, role, dto))
                .isInstanceOf(OrderException.class)
                .hasMessageContaining(OrderErrorCode.NO_PERMISSION.getErrorMessage());
    }

}
