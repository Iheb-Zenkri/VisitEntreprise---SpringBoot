package Spring.Visit.UserPackage.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Unavailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Start Time is required")
    private LocalDateTime startTime;

    @NotNull(message = "Start Time is required")
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    @JsonBackReference
    private Teacher teacher;

}

