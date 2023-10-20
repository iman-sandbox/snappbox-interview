package test.snapp.box.feedback.domain.biker.data;

import jakarta.persistence.*;
import lombok.Data;

@Entity @Data
public class Biker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
