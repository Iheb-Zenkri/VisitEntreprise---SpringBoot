package Spring.Visit.VisitModule.services;

import Spring.Visit.SharedModule.exceptions.BadRequestException;
import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import Spring.Visit.SharedModule.exceptions.UserNotFoundException;
import Spring.Visit.UserModule.entities.Teacher;
import Spring.Visit.UserModule.repositories.TeacherRepository;
import Spring.Visit.VisitModule.Dtos.VisitCreationDTO;
import Spring.Visit.VisitModule.Dtos.VisitDTO;
import Spring.Visit.VisitModule.entities.Company;
import Spring.Visit.VisitModule.entities.Visit;
import Spring.Visit.VisitModule.repositories.CompanyRepository;
import Spring.Visit.VisitModule.repositories.VisitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VisitService {
    private final VisitRepository visitRepository;
    private final TeacherRepository teacherRepository;

    private final CompanyRepository companyRepository;

    public VisitService(VisitRepository visitRepository, TeacherRepository teacherRepository, CompanyRepository companyRepository) {
        this.visitRepository = visitRepository;
        this.teacherRepository = teacherRepository;
        this.companyRepository = companyRepository;
    }

    public VisitDTO createVisit(VisitCreationDTO visitCreationDTO) {
        Company company = companyRepository.findById(visitCreationDTO.getCompanyId())
                .orElseThrow(() -> new ObjectNotFoundException("Company not found"));
        if(visitCreationDTO.getVisitDate().isBefore(LocalDateTime.now())){
            throw new BadRequestException("Le date est incorrect");
        }
        Visit visit = new Visit();
        visit.setVisitDate(visitCreationDTO.getVisitDate());
        visit.setStatus(visitCreationDTO.getStatus());
        visit.setNotes(visitCreationDTO.getNotes());
        visit.setLocation(company.getAddress());
        visit.setCompany(company);
        return VisitDTO.toVisitDTO(visitRepository.save(visit));
    }

    public VisitDTO getVisitById(Long id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Visit not found with id: " + id));
        return VisitDTO.toVisitDTO(visit);
    }

    public List<VisitDTO> getfinishedVisits() {
        return visitRepository.findAll()
                .stream().map(VisitDTO::toVisitDTO)
                .filter(visit -> visit.getVisitDate().isBefore(LocalDate.now().atStartOfDay()))
                .sorted(Comparator.comparing(VisitDTO::getVisitDate).reversed()) // Newest first
                .toList();
    }

    public List<VisitDTO> getUnfinishedVisits() {
        return visitRepository.findAll()
                .stream()
                .map(VisitDTO::toVisitDTO)
                .filter(visit -> !visit.getVisitDate().isBefore(LocalDate.now().atStartOfDay()))
                .sorted(Comparator.comparing(VisitDTO::getVisitDate))
                .toList();
    }

    public VisitDTO updateVisit(Long id, Visit updatedVisit) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Visit not found with id: " + id));
        if(updatedVisit.getVisitDate() != null) visit.setVisitDate(updatedVisit.getVisitDate());
        if(updatedVisit.getLocation() != null) visit.setLocation(updatedVisit.getLocation());
        if(updatedVisit.getStatus() != null) visit.setStatus(updatedVisit.getStatus());
        if(updatedVisit.getNotes() != null) visit.setNotes(updatedVisit.getNotes());
        if(updatedVisit.getCompany() != null) visit.setCompany(updatedVisit.getCompany());
        return VisitDTO.toVisitDTO(visitRepository.save(visit));
    }

    public Map<String, String> deleteVisit(Long id) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Visit not found with id: " + id));
        String companyName = visit.getCompany() != null ? visit.getCompany().getName() : "unknown";
        visitRepository.deleteById(id);

        Map<String, String> message = new HashMap<>();
        message.put("message", "La visite à " + companyName + " a été supprimée avec succès.");
        return message;
    }

    public VisitDTO addResponsibleToVisit(Long visitId, Long teacherId){
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ObjectNotFoundException("Visit not found"));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new UserNotFoundException("Teacher not found"));

        if(teacher.getVisits().contains(visit)) {
            visit.setResponsible(teacher);
            return VisitDTO.toVisitDTO(visitRepository.save(visit));
        }
        teacher.getVisits().add(visit);
        visit.setResponsible(teacher);
        teacherRepository.save(teacher);
        return VisitDTO.toVisitDTO(visitRepository.save(visit));
    }

    public VisitDTO removeResponsibleFromVisit(Long visitId,Long teacherId){
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new ObjectNotFoundException("Visit not found"));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new UserNotFoundException("Teacher not found"));

        if(visit.getResponsible() != teacher){
            if(teacher.getVisits().contains(visit)){
                teacher.getVisits().remove(visit);
                teacherRepository.save(teacher);
            }
            throw new BadRequestException("Ce professeur n'est pas un responsable de cet Visit");
        }
        if(!teacher.getVisits().contains(visit)){
                visit.setResponsible(null);
                visitRepository.save(visit);
            throw new BadRequestException("Ce professeur n'est pas un responsable de cet Visit");
        }
        visit.setResponsible(null);
        teacher.getVisits().remove(visit);
        teacherRepository.save(teacher);
        return VisitDTO.toVisitDTO(visitRepository.save(visit));
        }
}