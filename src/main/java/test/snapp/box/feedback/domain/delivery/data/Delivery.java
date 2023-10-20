package test.snapp.box.feedback.domain.delivery.data;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "feedback_id")
    private Long feedbackId;

    @Column(name = "delivery_time", nullable = false)
    private Long deliveryTime;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "biker_id", nullable = false)
    private Long bikerId;
}
