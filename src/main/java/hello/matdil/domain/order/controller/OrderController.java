package hello.matdil.domain.order.controller;

import hello.matdil.auth.annotation.LoginUser;
import hello.matdil.auth.model.AuthUser;
import hello.matdil.domain.order.dto.OrderCreateRequestDto;
import hello.matdil.domain.order.dto.OrderResponseDto;
import hello.matdil.domain.order.facade.OrderFacade;
import hello.matdil.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderFacade orderFacade;

    /**
     * 주문 생성
     */
    @PostMapping
    public ResponseEntity<SuccessResponse<OrderResponseDto>> createOrder(
            @LoginUser AuthUser authUser,
            @RequestBody @Valid OrderCreateRequestDto requestDto) {

        OrderResponseDto response = orderFacade.createOrder(authUser.getUserId(), authUser.getRole(), requestDto);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    // 이후 GET /me, GET /{orderId}, PATCH /{orderId}/status 도 이어서 만들 수 있어
}