package Spring.Visit.VisitModule.entities;

import Spring.Visit.VisitModule.enums.VisitStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime visitDate;
    private String location;

    @Enumerated(EnumType.STRING)
    private VisitStatus status;

    private String notes;

    /*@OneToMany(mappedBy = "visit", cascade = CascadeType.ALL)
    private List<Documents> documents;*/

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDateTime visitDate) { this.visitDate = visitDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public VisitStatus getStatus() { return status; }
    public void setStatus(VisitStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
