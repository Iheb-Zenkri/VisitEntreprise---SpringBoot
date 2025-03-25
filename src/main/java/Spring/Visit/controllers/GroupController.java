package Spring.Visit.controllers;

import Spring.Visit.dto.StudentGroupDTO;
import Spring.Visit.entities.Group;
import Spring.Visit.services.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestParam String name) {
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

