package hello.matdil.domain.delivery.controller;

import hello.matdil.auth.annotation.LoginUser;
import hello.matdil.auth.model.AuthUser;
import hello.matdil.domain.delivery.dto.DeliveryAssignmentResponseDto;
import hello.matdil.domain.delivery.dto.DeliverySearchResponseDto;
import hello.matdil.domain.delivery.facade.DeliveryFacade;
import hello.matdil.domain.delivery.facade.DeliverySearchFacade;
import hello.matdil.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/deliveries")
@PreAuthorize("hasRole('RIDER')")
public class DeliveryController {

    private final DeliveryFacade deliveryFacade;
    private final DeliverySearchFacade deliverySearchFacade;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<DeliverySearchResponseDto>>> findNearbyDeliveries(
            @RequestParam("lat") double latitude,
            @RequestParam("lon") double longitude,
            @RequestParam(value = "radius", defaultValue = "3.0") double radiusKm
    ) {
        List<DeliverySearchResponseDto> response = deliverySearchFacade.getDeliveries(
                latitude, longitude, radiusKm
        );

        return ResponseEntity.ok(SuccessResponse.success(response));
    }

    @PostMapping("/{deliveryId}/assignment")
    public ResponseEntity<SuccessResponse<DeliveryAssignmentResponseDto>> assignDelivery(
            @LoginUser AuthUser authUser,
            @PathVariable Long deliveryId
    ) {
        DeliveryAssignmentResponseDto response = deliveryFacade.assignDeliveryToRider(deliveryId, authUser.getUserId());
        return ResponseEntity.ok(SuccessResponse.success(response));
    }


}
