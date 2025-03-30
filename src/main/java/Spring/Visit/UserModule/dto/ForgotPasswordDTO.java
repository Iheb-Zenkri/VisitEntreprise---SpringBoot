package Spring.Visit.UserModule.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordDTO {
    @Email
    @NotBlank(message = "Email is required")
    private String email;
}
