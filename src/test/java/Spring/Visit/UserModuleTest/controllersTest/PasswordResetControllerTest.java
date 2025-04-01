package Spring.Visit.UserModuleTest.controllersTest;

import Spring.Visit.UserModule.controllers.PasswordResetController;
import Spring.Visit.UserModule.dto.ForgotPasswordDTO;
import Spring.Visit.UserModule.dto.ResetPasswordDTO;
import Spring.Visit.UserModule.services.PasswordResetService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordResetControllerTest {

    @Mock
    private PasswordResetService passwordResetService;

    @InjectMocks
    private PasswordResetController passwordResetController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void forgotPassword_ShouldReturnSuccessMessage() throws MessagingException {
        ForgotPasswordDTO dto = new ForgotPasswordDTO();
        String successMessage = "Password reset link sent!";
        when(passwordResetService.forgotPassword(dto)).thenReturn(successMessage);

        ResponseEntity<String> response = passwordResetController.forgotPassword(dto);

        assertNotNull(response);
        assertEquals(successMessage, response.getBody());
        verify(passwordResetService, times(1)).forgotPassword(dto);
    }

    @Test
    void resetPassword_ShouldReturnSuccessMessage() {
        ResetPasswordDTO dto = new ResetPasswordDTO();
        String successMessage = "Password has been reset successfully!";
        when(passwordResetService.resetPassword(dto)).thenReturn(successMessage);

        ResponseEntity<String> response = passwordResetController.resetPassword(dto);

        assertNotNull(response);
        assertEquals(successMessage, response.getBody());
        verify(passwordResetService, times(1)).resetPassword(dto);
    }
}

