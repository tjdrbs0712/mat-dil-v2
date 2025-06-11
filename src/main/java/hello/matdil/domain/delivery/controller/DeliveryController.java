package hello.matdil.domain.delivery.controller;

import hello.matdil.domain.delivery.dto.DeliverySearchResponseDto;
import hello.matdil.domain.delivery.service.DeliverySearchService;
import hello.matdil.domain.delivery.service.DeliveryService;
import hello.matdil.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/deliveries")
@PreAuthorize("hasRole('RIDER')")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final DeliverySearchService deliverySearchService;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<DeliverySearchResponseDto>>> findNearbyDeliveries(
            @RequestParam("lat") double latitude,
            @RequestParam("lon") double longitude,
            @RequestParam(value = "radius", defaultValue = "3.0") double radiusKm
    ) {
        List<DeliverySearchResponseDto> response = deliverySearchService.findNearbyDeliveries(
                latitude, longitude, radiusKm
        );

        return ResponseEntity.ok(SuccessResponse.success(response));
    }


}
