package nl.fontys.s3.dinemasterbackend.business.dtos.get.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class HandleForgottenPasswordRequest {

    @NotBlank(message = "Username cannot be blank.")
    @Email(message = "Invalid email format.")
    private String email;
}
