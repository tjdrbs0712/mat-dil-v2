package hello.matdil.domain.order.controller;

import hello.matdil.auth.annotation.LoginUser;
import hello.matdil.auth.model.AuthUser;
import hello.matdil.domain.order.dto.*;
import hello.matdil.domain.order.facade.OrderFacade;
import hello.matdil.global.response.SliceResponse;
import hello.matdil.global.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<SliceResponse<OrderSummaryDto, OrderCursorResponseDto>>> getOrders(
            @LoginUser AuthUser authUser,
            @ModelAttribute OrderCursorRequestDto cursor
            ) {

        SliceResponse<OrderSummaryDto, OrderCursorResponseDto> response =
                orderFacade.getOrders(authUser.getUserId(), cursor);

        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<SuccessResponse<OrderResponseDto>> getOrder(
            @LoginUser AuthUser authUser,
            @PathVariable Long orderId
    ) {
        OrderResponseDto response = orderFacade.getOrder(authUser.getUserId(), authUser.getRole(), orderId);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @GetMapping("/owner/store/{storeId}")
    public ResponseEntity<SuccessResponse<SliceResponse<OrderSummaryDto, OrderCursorResponseDto>>> getOwnerOrders(
            @LoginUser AuthUser authUser,
            @ModelAttribute OrderCursorRequestDto cursor,
            @PathVariable Long storeId
    ) {

        SliceResponse<OrderSummaryDto, OrderCursorResponseDto> response =
                orderFacade.getOwnerOrders(authUser.getUserId(), authUser.getRole(), cursor ,storeId);

        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @GetMapping("/owner/{orderId}")
    public ResponseEntity<SuccessResponse<OrderResponseDto>> getOwnerOrder(
            @LoginUser AuthUser authUser,
            @PathVariable Long orderId
    ) {
        OrderResponseDto response = orderFacade.getOwnerOrder(authUser.getUserId(), authUser.getRole(), orderId);
        return ResponseEntity.ok(SuccessResponse.success(response));
    }

}