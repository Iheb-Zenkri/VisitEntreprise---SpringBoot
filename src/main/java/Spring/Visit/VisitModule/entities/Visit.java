package Spring.Visit.VisitModule.entities;

import Spring.Visit.VisitModule.enums.VisitStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Visit's Date is required")
    private LocalDateTime visitDate;

    @NotNull(message = "Visit's Location is required")
    private String location;

    @Enumerated(EnumType.STRING)
    private VisitStatus status;

    private String notes;

    @NotNull(message = "Created at timestamp is required")
    private LocalDateTime createdAt;

    @NotNull(message = "Updated at timestamp is required")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonBackReference
    private Company company;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Feedback> feedbacks;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        status = VisitStatus.PLANNED;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}