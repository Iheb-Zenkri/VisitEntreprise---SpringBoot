package Spring.Visit.services;

import Spring.Visit.dto.ForgotPasswordDTO;
import Spring.Visit.dto.ResetPasswordDTO;
import Spring.Visit.entities.PasswordResetToken;
import Spring.Visit.entities.User;
import Spring.Visit.exceptions.UserNotFoundException;
import Spring.Visit.repositories.PasswordResetTokenRepository;
import Spring.Visit.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public String forgotPassword(ForgotPasswordDTO dto) throws MessagingException {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email " + dto.getEmail() + " not found"));

        tokenRepository.deleteByEmail(dto.getEmail());


        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(dto.getEmail());
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        tokenRepository.save(resetToken);

        String resetLink = "http://localhost:8080/reset-password?token=" + resetToken.getToken();
        try {
            emailService.sendEmail(dto.getEmail(), "Password Reset", "Click <a href='" + resetLink + "'>here</a> to reset your password.");
        } catch (Exception e) {
            System.err.println("Email sending failed: " + e.getMessage());
            throw new RuntimeException("Failed to send password reset email.");
        }

        return "Password reset email sent!";
    }

    @Transactional
    public String resetPassword(ResetPasswordDTO dto) {
        PasswordResetToken resetToken = tokenRepository.findByToken(dto.getToken())
                .orElseThrow(() -> new RuntimeException("Token not found"));


        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired.");
        }

        User user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email " + resetToken.getEmail() + " not found"));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        tokenRepository.delete(resetToken);

        return "Password updated successfully.";
    }
}
