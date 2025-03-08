package Spring.Visit.controllers;

import Spring.Visit.dto.LoginDTO;
import Spring.Visit.entities.User_Package.User;
import Spring.Visit.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO user) {
        return userService.authenticateUser(user);
    }
}
