package Spring.Visit.VisitModule.services;

import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import Spring.Visit.VisitModule.entities.Visit;
import Spring.Visit.VisitModule.repositories.VisitRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return visitRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Visit not found"));
    }

    public List<Visit> getAllVisits() {
        return visitRepository.findAll();
    }

    public Visit updateVisit(Long id, Visit updatedVisit) {
        Visit visit = getVisitById(id);
        if(updatedVisit.getVisitDate() != null) visit.setVisitDate(updatedVisit.getVisitDate());
        if(updatedVisit.getLocation() != null) visit.setLocation(updatedVisit.getLocation());
        if(updatedVisit.getStatus() != null) visit.setStatus(updatedVisit.getStatus());
        if(updatedVisit.getNotes() != null) visit.setNotes(updatedVisit.getNotes());
        return visitRepository.save(visit);
    }

    public Map<String,String> deleteVisit(Long id) {
        visitRepository.deleteById(id);
        Map<String,String> message = new HashMap<>();

        // after developping the company entity and link it
        // to Visit entity , replace ... with Company.name
        message.put("message","La visite à ... a été supprimée avec succées.");

        return message ;
    }
}

