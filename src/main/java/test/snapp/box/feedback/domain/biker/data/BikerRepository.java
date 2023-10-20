package test.snapp.box.feedback.domain.biker.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BikerRepository extends JpaRepository<Biker, Long> {
    Optional<Biker> findByUserId(Long userId);
}
