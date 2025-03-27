package Spring.Visit.UserPackage.controllers;

import Spring.Visit.UserPackage.dto.ResetPasswordDTO;
import Spring.Visit.UserPackage.services.PasswordResetService;
import Spring.Visit.UserPackage.dto.ForgotPasswordDTO;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDTO dto) throws MessagingException {
        String response = passwordResetService.forgotPassword(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO dto) {
        String response = passwordResetService.resetPassword(dto);
        return ResponseEntity.ok(response);
    }
}
