package Spring.Visit.UserModule.services;

import Spring.Visit.SharedModule.exceptions.BadRequestException;
import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import Spring.Visit.UserModule.dto.ForgotPasswordDTO;
import Spring.Visit.UserModule.dto.ResetPasswordDTO;
import Spring.Visit.UserModule.entities.PasswordResetToken;
import Spring.Visit.UserModule.entities.User;
import Spring.Visit.SharedModule.exceptions.UserNotFoundException;
import Spring.Visit.UserModule.repositories.PasswordResetTokenRepository;
import Spring.Visit.UserModule.repositories.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    private static final long EXPIRATION_TIME_MINUTES = 30;
    private static final String RESET_PASSWORD_PAGE_LINK = "http://localhost:8080/reset-password.html?token=";

    @Transactional
    public String forgotPassword(ForgotPasswordDTO dto) throws MessagingException {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email " + dto.getEmail() + " not found"));

        PasswordResetToken existingToken = tokenRepository.findByEmail(dto.getEmail());

        if (existingToken != null && !existingToken.isExpired()) {
            String resetLink = RESET_PASSWORD_PAGE_LINK + existingToken.getToken();
            emailService.sendEmail(dto.getEmail(), resetLink);
            return "Password reset email resent with the existing token!";
        }else{
            tokenRepository.deleteByEmail(dto.getEmail());
        }


        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(dto.getEmail());
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(EXPIRATION_TIME_MINUTES));
        tokenRepository.save(resetToken);

        String resetLink = RESET_PASSWORD_PAGE_LINK + resetToken.getToken();
        try {
            emailService.sendEmail(dto.getEmail(),resetLink );
        } catch (Exception e) {
            System.err.println("Email sending failed: " + e.getMessage());
            throw new BadRequestException("Failed to send password reset email.");
        }

        return "Password reset email sent!";
    }

    @Transactional
    public String resetPassword(ResetPasswordDTO dto) {
        PasswordResetToken resetToken = tokenRepository.findByToken(dto.getToken())
                .orElseThrow(() -> new ObjectNotFoundException("Token not found"));


        if (resetToken.isExpired()) {
            throw new BadRequestException("Token has expired.");
        }

        User user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with email "+resetToken.getEmail()+" not Found."));
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        tokenRepository.delete(resetToken);

        return "Password updated successfully.";
    }
}
