package nl.fontys.s3.dinemasterbackend.business.dtos.get.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class AuthenticateUserRequest {

    @NotBlank(message = "Username cannot be blank.")
    @Email(message = "Invalid email format.")
    private String username;

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters.")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.*\\s).{8,16}$",
            message = "Password must include at least one lowercase letter, one uppercase letter, one number, one special character, and no spaces."
    )
    private String password;
}
