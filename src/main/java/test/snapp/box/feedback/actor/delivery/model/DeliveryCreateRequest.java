package test.snapp.box.feedback.actor.delivery.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class DeliveryCreateRequest {

    @NotNull
    private Long customerId;

    @NotNull
    private Long bikerId;

    @NotNull
    private Date deliveryTime;
}

