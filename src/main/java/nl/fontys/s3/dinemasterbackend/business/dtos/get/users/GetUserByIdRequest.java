package nl.fontys.s3.dinemasterbackend.business.dtos.get.users;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;

@Builder
@Getter
@Setter
public class GetUserByIdRequest {
    @NotNull
    @Min(1)
    private Long userId;
    private AccessToken accessToken;
}
