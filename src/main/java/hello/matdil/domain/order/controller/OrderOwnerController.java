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
@RequestMapping("/api/v1/owner")
public class OrderOwnerController {

    private final OrderFacade orderFacade;

    @GetMapping("/stores/{storeId}/orders")
    public ResponseEntity<SuccessResponse<SliceResponse<OrderSummaryDto, OrderCursorResponseDto>>> getStoreOwnerOrders(
            @LoginUser AuthUser authUser,
            @ModelAttribute OrderCursorRequestDto cursor,
            @PathVariable Long storeId
    ) {
        SliceResponse<OrderSummaryDto, OrderCursorResponseDto> response =
                orderFacade.getStoreOwnerOrders(authUser.getUserId(), authUser.getRole(), cursor, storeId);

        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<SuccessResponse<OrderResponseDto>> getStoreOwnerOrder(
            @LoginUser AuthUser authUser,
            @PathVariable Long orderId
    ) {
        OrderResponseDto response =
                orderFacade.getStoreOwnerOrder(authUser.getUserId(), authUser.getRole(), orderId);

        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @PatchMapping("/orders/{orderId}/status")
    public ResponseEntity<SuccessResponse<Void>> changeStoreOwnerOrderStatus(
            @LoginUser AuthUser authUser,
            @PathVariable Long orderId,
            @RequestBody @Valid OrderStatusUpdateRequestDto request
    ) {
        orderFacade.changeStoreOwnerOrderStatus(authUser.getUserId(), authUser.getRole(), orderId, request.toEnum());
        return ResponseEntity.ok(SuccessResponse.success(null));
    }
}
