package Spring.Visit.UserModule.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}

