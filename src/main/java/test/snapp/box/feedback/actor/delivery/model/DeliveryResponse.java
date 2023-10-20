package test.snapp.box.feedback.actor.delivery.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import test.snapp.box.feedback.domain.delivery.data.Delivery;

import java.util.Date;

@Data @NoArgsConstructor
public class DeliveryResponse {

    private Long id;
    private Long customerId;
    private Long bikerId;
    private Long feedbackId;
    private Date deliveryTime;

    public DeliveryResponse(Delivery delivery) {
        this.id = delivery.getId();
        this.customerId = delivery.getCustomerId();
        this.bikerId = delivery.getBikerId();
        this.feedbackId = delivery.getFeedbackId();
        this.deliveryTime = new Date(delivery.getDeliveryTime());
    }
}

