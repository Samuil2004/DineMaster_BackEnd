package nl.fontys.s3.dinemasterbackend.business.dtos.get.users;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh token cannot be blank")
    private String refreshToken;
}
