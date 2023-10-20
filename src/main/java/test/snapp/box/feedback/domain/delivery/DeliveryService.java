package test.snapp.box.feedback.domain.delivery;

import org.springframework.stereotype.Service;
import test.snapp.box.feedback.actor.delivery.model.DeliveryCreateRequest;
import test.snapp.box.feedback.application.advice.exceptions.NotFoundException;
import test.snapp.box.feedback.domain.delivery.data.Delivery;
import test.snapp.box.feedback.domain.delivery.data.DeliveryRepository;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public Delivery createDelivery(DeliveryCreateRequest deliveryCreateRequest) {
        Delivery delivery = new Delivery();
        delivery.setCustomerId(deliveryCreateRequest.getCustomerId()); // Todo: Check customer is existed
        delivery.setBikerId(deliveryCreateRequest.getBikerId());
        delivery.setDeliveryTime(deliveryCreateRequest.getDeliveryTime().getTime()); // Todo: Check customer is existed

        return deliveryRepository.save(delivery);
    }

    public Delivery getDelivery(Long id) {
        return deliveryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Delivery")
        );
    }

    public Delivery saveDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }
}

