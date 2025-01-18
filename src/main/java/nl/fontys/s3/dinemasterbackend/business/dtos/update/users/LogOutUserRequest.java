package nl.fontys.s3.dinemasterbackend.business.dtos.update.users;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;

@Builder
@Getter
@AllArgsConstructor
public class LogOutUserRequest {
    @NotNull(message = "User Id cannot be null")
    @Min(message = "User Id cannot be negative", value = 1L)
    private Long userId;

    AccessToken accessToken;
}
