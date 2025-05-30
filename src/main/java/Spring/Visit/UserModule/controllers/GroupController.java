package Spring.Visit.UserModule.controllers;

import Spring.Visit.UserModule.dto.StudentGroupDTO;
import Spring.Visit.UserModule.entities.Group;
import Spring.Visit.UserModule.services.GroupService;
import Spring.Visit.VisitModule.Dtos.VisitDTO;
import Spring.Visit.VisitModule.entities.Visit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public ResponseEntity<List<StudentGroupDTO>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentGroupDTO> getGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @GetMapping("/student-visits/{studentId}")
    public ResponseEntity<List<VisitDTO>> getStudentVisits(@PathVariable Long studentId) {
        return ResponseEntity.ok(groupService.getStudentVisits(studentId));
    }
    @GetMapping("/student-stats/{studentId}")
    public ResponseEntity<Map<String, Object>> getStudentStats(@PathVariable Long studentId) {
        return ResponseEntity.ok(groupService.getStudentStats(studentId));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<StudentGroupDTO> getStudentGroup(@PathVariable Long studentId) {
        return ResponseEntity.ok(groupService.getStudentGroup(studentId));
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<StudentGroupDTO> createGroup(@RequestParam String name) {
        return ResponseEntity.ok(groupService.createGroup(name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<StudentGroupDTO> updateGroup(@PathVariable Long id,@RequestParam String name) {
        return ResponseEntity.ok(groupService.updateGroup(id,name));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{groupId}/add-student/{studentId}")
    public ResponseEntity<StudentGroupDTO> addStudentToGroup(@PathVariable Long groupId, @PathVariable Long studentId) {
        return ResponseEntity.ok(groupService.addStudentToGroup(groupId, studentId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{groupId}/remove-student/{studentId}")
    public ResponseEntity<StudentGroupDTO> removeStudentFromGroup(@PathVariable Long groupId, @PathVariable Long studentId) {
        return ResponseEntity.ok(groupService.removeStudentFromGroup(groupId, studentId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}

