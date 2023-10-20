package test.snapp.box.feedback.domain.feedback;

import test.snapp.box.feedback.actor.feedback.model.FeedbackDisplay;
import test.snapp.box.feedback.actor.feedback.model.FeedbackSubmitRequest;
import test.snapp.box.feedback.application.advice.exceptions.AlreadyExistException;
import test.snapp.box.feedback.application.advice.exceptions.ForbiddenResourceException;
import test.snapp.box.feedback.domain.delivery.DeliveryService;
import test.snapp.box.feedback.domain.delivery.data.Delivery;
import test.snapp.box.feedback.domain.feedback.data.Feedback;
import test.snapp.box.feedback.domain.feedback.data.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final DeliveryService deliveryService;
    public FeedbackService(FeedbackRepository feedbackRepository, DeliveryService deliveryService) {
        this.feedbackRepository = feedbackRepository;
        this.deliveryService = deliveryService;
    }

    public Feedback submitFeedback(Long customerId, FeedbackSubmitRequest feedbackSubmitRequest) {
        Delivery delivery = deliveryService.getDelivery(feedbackSubmitRequest.getDeliveryId());

        if (!customerId.equals(delivery.getCustomerId()))
            throw new ForbiddenResourceException("Deliver", feedbackSubmitRequest.getDeliveryId());

        if (delivery.getFeedbackId() != null)
            throw new AlreadyExistException("Feedback");

        Feedback feedback = new Feedback();
        feedback.setDeliveryId(feedbackSubmitRequest.getDeliveryId());
        feedback.setRating(feedbackSubmitRequest.getRating());
        feedback.setComments(feedbackSubmitRequest.getComments());
        Feedback savedFeedback = feedbackRepository.save(feedback);
        delivery.setFeedbackId(savedFeedback.getId());
        deliveryService.saveDelivery(delivery);

        return savedFeedback;
    }

    public List<FeedbackDisplay> getFeedbackByDateRange(Date startDate, Date endDate) {
        List<Feedback> feedbacks = feedbackRepository.findByDeliveryTimeBetween(startDate.getTime(), endDate.getTime());
        return feedbacks.stream().map(feedback -> {
            Delivery delivery = deliveryService.getDelivery(feedback.getDeliveryId());
            return new FeedbackDisplay(feedback, delivery);
        }).collect(Collectors.toList());
    }

    public List<FeedbackDisplay> getFeedbackByBiker(Long bikerId) {
        List<Feedback> feedbacks = feedbackRepository.findByBikerId(bikerId);
        return feedbacks.stream().map(feedback -> {
            Delivery delivery = deliveryService.getDelivery(feedback.getDeliveryId());
            return new FeedbackDisplay(feedback, delivery);
        }).collect(Collectors.toList());
    }

    public List<FeedbackDisplay> getFeedbackByRating(Integer rating) {
        List<Feedback> feedbacks = feedbackRepository.findByRating(rating);
        return feedbacks.stream().map(feedback -> {
            Delivery delivery = deliveryService.getDelivery(feedback.getDeliveryId());
            return new FeedbackDisplay(feedback, delivery);
        }).collect(Collectors.toList());
    }

    public Double getBikerAverageRating(Long bikerId) {
        return feedbackRepository.findAverageRatingByBikerId(bikerId);
    }

    public List<Feedback> getBikerFeedbacks(Long bikerId) {
        return feedbackRepository.findFeedbacksByBikerId(bikerId);
    }
}
