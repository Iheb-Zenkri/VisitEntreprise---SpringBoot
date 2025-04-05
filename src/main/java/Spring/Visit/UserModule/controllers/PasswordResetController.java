package Spring.Visit.UserModule.controllers;

import Spring.Visit.UserModule.dto.ResetPasswordDTO;
import Spring.Visit.UserModule.services.PasswordResetService;
import Spring.Visit.UserModule.dto.ForgotPasswordDTO;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String,String>> forgotPassword(@RequestBody ForgotPasswordDTO dto) throws MessagingException {
        String response = passwordResetService.forgotPassword(dto);
        Map<String, String> responseJSON = new HashMap<>();
        responseJSON.put("message",response);
        return ResponseEntity.ok(responseJSON);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String,String>> resetPassword(@RequestBody ResetPasswordDTO dto) {
        String response = passwordResetService.resetPassword(dto);
        Map<String, String> responseJSON = new HashMap<>();
        responseJSON.put("message",response);
        return ResponseEntity.ok(responseJSON);
    }
}
