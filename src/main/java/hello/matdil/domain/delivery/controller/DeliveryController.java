package hello.matdil.domain.delivery.controller;

import hello.matdil.domain.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

}
