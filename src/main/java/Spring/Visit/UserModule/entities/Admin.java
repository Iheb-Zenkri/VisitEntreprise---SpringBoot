package Spring.Visit.UserModule.entities;

import Spring.Visit.UserModule.dto.CreateUserDTO;
import Spring.Visit.UserModule.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Admin extends User {
    public Admin(Long id, String firstName, String lastName, String email, String password, UserRole role, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lastLogin) {
        super(id, firstName, lastName, email, password, role, createdAt, updatedAt, lastLogin);
    }

    public Admin(CreateUserDTO user){
        super(user);
    }
}
