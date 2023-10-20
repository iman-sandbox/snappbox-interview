package test.snapp.box.feedback;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import test.snapp.box.feedback.actor.feedback.model.FeedbackSubmitRequest;
import test.snapp.box.feedback.application.advice.exceptions.AlreadyExistException;
import test.snapp.box.feedback.application.advice.exceptions.ForbiddenResourceException;
import test.snapp.box.feedback.domain.delivery.DeliveryService;
import test.snapp.box.feedback.domain.delivery.data.Delivery;
import test.snapp.box.feedback.domain.feedback.FeedbackService;
import test.snapp.box.feedback.domain.feedback.data.Feedback;
import test.snapp.box.feedback.domain.feedback.data.FeedbackRepository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FeedbackServiceMockTest {

    @InjectMocks
    private FeedbackService feedbackService;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private DeliveryService deliveryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitFeedbackSuccessfully() {
        Long customerId = 1L;
        FeedbackSubmitRequest request = new FeedbackSubmitRequest();
        request.setDeliveryId(2L);
        request.setRating(5);
        request.setComments("Good service");

        Delivery delivery = new Delivery();
        delivery.setCustomerId(customerId);

        Feedback feedback = new Feedback();
        feedback.setId(1L);
        feedback.setDeliveryId(1L);
        feedback.setRating(5);
        feedback.setComments("Great service!");

        when(deliveryService.getDelivery(request.getDeliveryId())).thenReturn(delivery);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        Feedback submitFeedback = feedbackService.submitFeedback(customerId, request);

        assertNotNull(submitFeedback);
        assertEquals(feedback.getId(), feedback.getId());
        assertEquals(feedback.getDeliveryId(), feedback.getDeliveryId());
        assertEquals(feedback.getRating(), feedback.getRating());
        assertEquals(feedback.getComments(), feedback.getComments());    }

    @Test
    void testSubmitFeedbackWithMismatchedCustomerId() {
        Long customerId = 1L;
        FeedbackSubmitRequest request = new FeedbackSubmitRequest();
        request.setDeliveryId(1L);

        Delivery delivery = new Delivery();
        delivery.setCustomerId(2L);

        when(deliveryService.getDelivery(request.getDeliveryId())).thenReturn(delivery);

        assertThrows(ForbiddenResourceException.class, () -> feedbackService.submitFeedback(customerId, request));
    }

    @Test
    void testSubmitFeedbackWithExistingFeedbackId() {
        Long customerId = 1L;
        FeedbackSubmitRequest request = new FeedbackSubmitRequest();
        request.setDeliveryId(1L);

        Delivery delivery = new Delivery();
        delivery.setCustomerId(customerId);
        delivery.setFeedbackId(1L);


        when(deliveryService.getDelivery(request.getDeliveryId())).thenReturn(delivery);

        assertThrows(AlreadyExistException.class, () -> feedbackService.submitFeedback(customerId, request));
    }
}

