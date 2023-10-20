package test.snapp.box.feedback.actor.biker;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import test.snapp.box.feedback.actor.biker.model.RatingOverviewResponse;
import test.snapp.box.feedback.domain.biker.BikerService;
import test.snapp.box.feedback.domain.feedback.data.Feedback;
import test.snapp.box.feedback.domain.feedback.FeedbackService;

import java.util.List;

@RestController
@RequestMapping("/api/bikers")
@Validated
public class BikerController {

    private final FeedbackService feedbackService;
    private final BikerService bikerService;

    public BikerController(FeedbackService feedbackService, BikerService bikerService) {
        this.feedbackService = feedbackService;
        this.bikerService = bikerService;
    }

    @GetMapping("/average-rating")
    @PreAuthorize("hasRole('BIKER')")
    public ResponseEntity<RatingOverviewResponse> getBikerAverageRating(
            @RequestHeader(name="Authorization") String token
    ) {
        Long bikerId = bikerService.getByBearerToken(token).getId();
        return ResponseEntity.ok(
                new RatingOverviewResponse(feedbackService.getBikerAverageRating(bikerId))
        );
    }

    @GetMapping("/feedback")
    @PreAuthorize("hasRole('BIKER')")
    public ResponseEntity<List<Feedback>> getBikerFeedbacks(
            @RequestHeader(name="Authorization") String token
    ) {
        Long bikerId = bikerService.getByBearerToken(token).getId();
        List<Feedback> feedbacks = feedbackService.getBikerFeedbacks(bikerId);

        return ResponseEntity.ok(feedbacks);
    }
}
