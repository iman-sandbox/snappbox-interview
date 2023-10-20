package test.snapp.box.feedback.actor.feedback.model;

import lombok.Data;
import test.snapp.box.feedback.domain.delivery.data.Delivery;
import test.snapp.box.feedback.domain.feedback.data.Feedback;

import java.util.Date;

@Data
public class FeedbackDisplay {
    private Long feedbackId;
    private Integer rating;
    private String comments;
    private Date deliveryTime;
    private Long bikerId;
    private Long customerId;

    public FeedbackDisplay(Feedback feedback, Delivery delivery) {
        this.feedbackId = feedback.getId();
        this.rating = feedback.getRating();
        this.comments = feedback.getComments();
        this.deliveryTime = new Date(delivery.getDeliveryTime());
        this.bikerId = delivery.getBikerId();
        this.customerId = delivery.getCustomerId();
    }
}

