package test.snapp.box.feedback.domain.feedback.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    @Query("SELECT f FROM Feedback f JOIN Delivery d ON f.id = d.feedbackId WHERE d.deliveryTime BETWEEN :startDate AND :endDate")
    List<Feedback> findByDeliveryTimeBetween(Long startDate, Long endDate);

    @Query("SELECT f FROM Feedback f JOIN Delivery d ON f.id = d.feedbackId WHERE d.bikerId = :bikerId")
    List<Feedback> findByBikerId(Long bikerId);

    List<Feedback> findByRating(Integer rating);

    @Query("SELECT AVG(f.rating) FROM Feedback f JOIN Delivery d ON f.deliveryId = d.id WHERE d.bikerId = :bikerId")
    Double findAverageRatingByBikerId(Long bikerId);

    @Query("SELECT f FROM Feedback f WHERE f.deliveryId IN (SELECT d.id FROM Delivery d WHERE d.bikerId = :bikerId)")
    List<Feedback> findFeedbacksByBikerId(Long bikerId);
}
