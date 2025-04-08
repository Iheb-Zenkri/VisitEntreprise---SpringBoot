package Spring.Visit.VisitModule.services;

import Spring.Visit.VisitModule.entities.Visit;
import Spring.Visit.VisitModule.repositories.VisitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitService {
    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public Visit createVisit(Visit visit) {
        return visitRepository.save(visit);
    }

    public Visit getVisitById(Long id) {
        return visitRepository.findById(id).orElseThrow(() -> new RuntimeException("Visit not found"));
    }

    public List<Visit> getAllVisits() {
        return visitRepository.findAll();
    }

    public Visit updateVisit(Long id, Visit updatedVisit) {
        Visit visit = getVisitById(id);
        visit.setVisitDate(updatedVisit.getVisitDate());
        visit.setLocation(updatedVisit.getLocation());
        visit.setStatus(updatedVisit.getStatus());
        visit.setNotes(updatedVisit.getNotes());
        return visitRepository.save(visit);
    }

    public void deleteVisit(Long id) {
        visitRepository.deleteById(id);
    }
}

