package Spring.Visit.VisitModule.entities;

import Spring.Visit.VisitModule.enums.FeedbackRating;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private FeedbackRating rating;

    private String comment;

    @NotNull(message = "Created at timestamp is required")
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "visit_id")
    private Visit visit;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}