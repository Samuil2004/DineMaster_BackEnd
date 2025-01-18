package nl.fontys.s3.dinemasterbackend.business.dtos.get.users;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GetUserByRoleRequest {

    @NotBlank(message = "Role must not be empty")
    private String role;

    @NotBlank(message = "Username must not be empty")
    private String username;

    @NotNull(message = "Page number must not be null")
    @Min(value = 1, message = "Page number can not be less than 1")
    private Integer pageNumber;
}
