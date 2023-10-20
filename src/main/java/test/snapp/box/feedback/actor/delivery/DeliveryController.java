package test.snapp.box.feedback.actor.delivery;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import test.snapp.box.feedback.actor.delivery.model.DeliveryCreateRequest;
import test.snapp.box.feedback.actor.delivery.model.DeliveryResponse;
import test.snapp.box.feedback.domain.delivery.data.Delivery;
import test.snapp.box.feedback.domain.delivery.DeliveryService;

@RestController
@RequestMapping("/api/deliveries")
@Validated
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('BIKER') or hasRole('MANAGER')")
    public ResponseEntity<DeliveryResponse> createDelivery(@Valid @RequestBody DeliveryCreateRequest deliveryCreateRequest) {
        Delivery delivery = deliveryService.createDelivery(deliveryCreateRequest);
        return new ResponseEntity<>(new DeliveryResponse(delivery), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('BIKER') or hasRole('MANAGER')")
    public ResponseEntity<DeliveryResponse> getDelivery(@RequestParam @NotNull Long id) {
        return new ResponseEntity<>(new DeliveryResponse(deliveryService.getDelivery(id)), HttpStatus.NOT_FOUND);
    }
}
