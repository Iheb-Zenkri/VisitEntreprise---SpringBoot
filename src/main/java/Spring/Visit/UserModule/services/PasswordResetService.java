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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);
    private static final long EXPIRATION_TIME_MINUTES = 30;
    private static final String RESET_PASSWORD_PAGE_LINK = "http://localhost:8080/reset-password.html?token=";

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public String forgotPassword(ForgotPasswordDTO dto) throws MessagingException {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> {
                    logger.error("User with email {} not found", dto.getEmail());
                    return new UserNotFoundException("Utilisateur avec l'email " + dto.getEmail() + " non trouvé");
                });

        PasswordResetToken existingToken = tokenRepository.findByEmail(dto.getEmail());

        if (existingToken != null && !existingToken.isExpired()) {
            try {
                emailService.sendEmail(dto.getEmail(), existingToken.getToken());
                logger.info("Password reset email resent to {}", dto.getEmail());
                return "Email de réinitialisation du mot de passe renvoyé avec le token existant à "+dto.getEmail();
            } catch (MessagingException e) {
                logger.error("Failed to resend password reset email to {}: {}", dto.getEmail(), e.getMessage());
                throw new BadRequestException("Échec de l'envoi de l'email de réinitialisation.");
            }
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
            emailService.sendEmail(dto.getEmail(), resetLink);
            logger.info("Password reset email sent to {}", dto.getEmail());
        } catch (Exception e) {
            logger.error("Email sending failed to {}: {}", dto.getEmail(), e.getMessage());
            throw new BadRequestException("Échec de l'envoi de l'email de réinitialisation.");
        }
        return "Email de réinitialisation du mot de passe envoyé à "+dto.getEmail();
    }

    @Transactional
    public String resetPassword(ResetPasswordDTO dto) {
        PasswordResetToken resetToken = tokenRepository.findByToken(dto.getToken())
                .orElseThrow(() -> {
                    logger.error("Token not found for reset: {}", dto.getToken());
                    return new ObjectNotFoundException("Token non trouvé");
                });

        if (resetToken.isExpired()) {
            logger.warn("Attempt to reset password with expired token: {}", dto.getToken());
            throw new BadRequestException("Le token a expiré.");
        }

        User user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> {
                    logger.error("User with email {} not found during password reset", resetToken.getEmail());
                    return new UserNotFoundException("Utilisateur avec l'email " + resetToken.getEmail() + " non trouvé.");
                });

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        tokenRepository.delete(resetToken);

        logger.info("Password successfully updated for user with email: {}", user.getEmail());
        return "Mot de passe mis à jour avec succès.";
    }
}
