package Spring.Visit.UserPackage.controllers;

import Spring.Visit.UserPackage.dto.LoginDTO;
import Spring.Visit.UserPackage.dto.CreateUserDTO;
import Spring.Visit.UserPackage.dto.UpdateUserDTO;
import Spring.Visit.UserPackage.dto.UserDTO;
import Spring.Visit.SharedPackage.enums.UserRole;
import Spring.Visit.UserPackage.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody CreateUserDTO dto) {
        return ResponseEntity.ok(userService.registerUser(dto));
    }

    @PostMapping("/auth/login")
    public String login(@RequestBody LoginDTO user) {
        return userService.authenticateUser(user);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> getAllUsers(@RequestParam(required = false) UserRole role, Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(role, pageable));
    }
    @GetMapping("/users/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO dto) {
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

}
