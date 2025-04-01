package Spring.Visit.UserModuleTest.servicesTest;

import Spring.Visit.SharedModule.exceptions.BadRequestException;
import Spring.Visit.SharedModule.exceptions.ObjectNotFoundException;
import Spring.Visit.SharedModule.exceptions.UserNotFoundException;
import Spring.Visit.UserModule.dto.ForgotPasswordDTO;
import Spring.Visit.UserModule.dto.ResetPasswordDTO;
import Spring.Visit.UserModule.entities.PasswordResetToken;
import Spring.Visit.UserModule.entities.User;
import Spring.Visit.UserModule.repositories.PasswordResetTokenRepository;
import Spring.Visit.UserModule.repositories.UserRepository;
import Spring.Visit.UserModule.services.EmailService;
import Spring.Visit.UserModule.services.PasswordResetService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private PasswordResetService passwordResetService;

    private ForgotPasswordDTO forgotPasswordDTO;
    private ResetPasswordDTO resetPasswordDTO;
    private User user;
    private PasswordResetToken resetToken;

    @BeforeEach
    void setUp() {
        forgotPasswordDTO = new ForgotPasswordDTO("user@example.com");
        resetPasswordDTO = new ResetPasswordDTO("reset-token", "newSecurePassword123");
        user = new User();
        user.setEmail(forgotPasswordDTO.getEmail());

        resetToken = new PasswordResetToken();
        resetToken.setToken("reset-token");
        resetToken.setEmail(user.getEmail());
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
    }

    @Test
    void forgotPassword_ShouldSendResetEmail_WhenUserExists() throws MessagingException {
        when(userRepository.findByEmail(forgotPasswordDTO.getEmail())).thenReturn(Optional.of(user));
        when(tokenRepository.findByEmail(forgotPasswordDTO.getEmail())).thenReturn(null);
        when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(resetToken);
        doNothing().when(emailService).sendEmail(anyString(), anyString());

        String response = passwordResetService.forgotPassword(forgotPasswordDTO);

        assertEquals("Password reset email sent!", response);
        verify(emailService, times(1)).sendEmail(eq(forgotPasswordDTO.getEmail()), anyString());
    }

    @Test
    void forgotPassword_ShouldResendToken_WhenTokenExistsAndValid() throws MessagingException {
        when(userRepository.findByEmail(forgotPasswordDTO.getEmail())).thenReturn(Optional.of(user));
        when(tokenRepository.findByEmail(forgotPasswordDTO.getEmail())).thenReturn(resetToken);
        doNothing().when(emailService).sendEmail(anyString(), anyString());

        String response = passwordResetService.forgotPassword(forgotPasswordDTO);

        assertEquals("Password reset email resent with the existing token!", response);
        verify(emailService, times(1)).sendEmail(eq(forgotPasswordDTO.getEmail()), eq(resetToken.getToken()));
        verify(tokenRepository, never()).deleteByEmail(anyString()); // Ensure no new token is created
    }

    @Test
    void forgotPassword_ShouldThrowException_WhenUserNotFound() throws MessagingException {
        when(userRepository.findByEmail(forgotPasswordDTO.getEmail())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> passwordResetService.forgotPassword(forgotPasswordDTO));

        assertEquals("User with email user@example.com not found", exception.getMessage());
        verify(emailService, never()).sendEmail(anyString(), anyString());
    }

    @Test
    void resetPassword_ShouldUpdatePassword_WhenTokenIsValid() {
        when(tokenRepository.findByToken(resetPasswordDTO.getToken())).thenReturn(Optional.of(resetToken));
        when(userRepository.findByEmail(resetToken.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(resetPasswordDTO.getNewPassword())).thenReturn("encodedPassword");

        String response = passwordResetService.resetPassword(resetPasswordDTO);

        assertEquals("Password updated successfully.", response);
        verify(userRepository, times(1)).save(user);
        verify(tokenRepository, times(1)).delete(resetToken);
    }

    @Test
    void resetPassword_ShouldThrowException_WhenTokenIsExpired() {
        resetToken.setExpiryDate(LocalDateTime.now().minusMinutes(1));
        when(tokenRepository.findByToken(resetPasswordDTO.getToken())).thenReturn(Optional.of(resetToken));

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> passwordResetService.resetPassword(resetPasswordDTO));

        assertEquals("Token has expired.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void resetPassword_ShouldThrowException_WhenTokenNotFound() {
        when(tokenRepository.findByToken(resetPasswordDTO.getToken())).thenReturn(Optional.empty());

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> passwordResetService.resetPassword(resetPasswordDTO));

        assertEquals("Token not found", exception.getMessage());
    }

}

