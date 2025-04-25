package Spring.Visit.VisitModule.entities;

import Spring.Visit.UserModule.entities.User;
import Spring.Visit.VisitModule.enums.FeedbackRating;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Enumerated(EnumType.STRING)
    private FeedbackRating rating;

    @NotNull(message = "Comment is required")
    private String comment;

    @NotNull(message = "Created at timestamp is required")
    private LocalDateTime createdAt;

    @NotNull(message = "Updated at timestamp is required")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "added_by", nullable = false)
    @JsonManagedReference
    private User addedBy;

    @ManyToOne
    @JoinColumn(name = "visit_id")
    @JsonBackReference
    private Visit visit;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        rating = FeedbackRating.ONE_STAR ;
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}