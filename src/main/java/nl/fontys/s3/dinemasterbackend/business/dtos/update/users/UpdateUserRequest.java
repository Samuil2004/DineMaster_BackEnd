package nl.fontys.s3.dinemasterbackend.business.dtos.update.users;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequest {
    @NotNull(message = "User Id cannot be null")
    @Min(message = "User Id cannot be negative", value = 1L)
    private Long userId;

    @NotBlank(message = "First name is required")
    @Pattern(
            regexp = "^[a-zA-Z]+$",
            message = "First name must contain only alphabetical characters"
    )
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Pattern(
            regexp = "^[a-zA-Z]+$",
            message = "Last name must contain only alphabetical characters"
    )
    private String lastName;

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters.")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.*\\s).{8,16}$",
            message = "Password must include at least one lowercase letter, one uppercase letter, one number, one special character, and no spaces."
    )
    private String password;

    @NotBlank(message = "Confirmed password cannot be blank.")
    @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters.")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.*\\s).{8,16}$",
            message = "Password must include at least one lowercase letter, one uppercase letter, one number, one special character, and no spaces."
    )
    private String confirmPassword;


    @NotBlank(message = "Username cannot be blank.")
    @Email(message = "Invalid email format.")
    private String email;

}

