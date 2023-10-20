package test.snapp.box.feedback.actor.feedback;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import test.snapp.box.feedback.actor.feedback.model.FeedbackDisplay;
import test.snapp.box.feedback.actor.feedback.model.FeedbackSubmitRequest;
import test.snapp.box.feedback.domain.customer.CustomerService;
import test.snapp.box.feedback.domain.feedback.data.Feedback;
import test.snapp.box.feedback.domain.feedback.FeedbackService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@Validated
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final CustomerService customerService;

    public FeedbackController(FeedbackService feedbackService, CustomerService customerService) {
        this.feedbackService = feedbackService;
        this.customerService = customerService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Feedback> submitFeedback(
            @RequestHeader(name="Authorization") String token,
            @Valid @RequestBody FeedbackSubmitRequest feedbackSubmitRequest
    ) {
        Long customerId = customerService.getByBearerToken(token).getId();
        Feedback feedback = feedbackService.submitFeedback(customerId, feedbackSubmitRequest);
        return new ResponseEntity<>(feedback, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/date-range")
    public ResponseEntity<List<FeedbackDisplay>> getFeedbackByDateRange(
            @NotNull @RequestParam Long startDate, @NotNull @RequestParam Long endDate) {
        List<FeedbackDisplay> feedbackDisplays = feedbackService.getFeedbackByDateRange(new Date(startDate), new Date(endDate));
        return new ResponseEntity<>(feedbackDisplays, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/by-biker")
    public ResponseEntity<List<FeedbackDisplay>> getFeedbackByBiker(@NotNull @RequestParam Long bikerId) {
        List<FeedbackDisplay> feedbackDisplays = feedbackService.getFeedbackByBiker(bikerId);
        return new ResponseEntity<>(feedbackDisplays, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/by-rating")
    public ResponseEntity<List<FeedbackDisplay>> getFeedbackByRating(@RequestParam Integer rating) {
        List<FeedbackDisplay> feedbackDisplays = feedbackService.getFeedbackByRating(rating);
        return new ResponseEntity<>(feedbackDisplays, HttpStatus.OK);
    }
}
