package Spring.Visit.DocumentModule.entities;

import Spring.Visit.UserModule.entities.User;
import Spring.Visit.VisitModule.entities.Visit;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VisitGallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "visit_id")
    private Visit visit;

    @ManyToOne
    @JoinColumn(name = "added_by", nullable = false)
    private User addedBy;

    @ManyToMany
    @JoinTable(
            name = "visit_gallery_documents",
            joinColumns = @JoinColumn(name = "visit_gallery_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )    private List<Document> gallery;

    public void addNewPictureToGallery(Document document){
        if (!this.gallery.contains(document)) {
            this.gallery.add(document);
        }
    }

    public void removePictureFromGallery(Document document){
        this.gallery.remove(document);
    }
}
