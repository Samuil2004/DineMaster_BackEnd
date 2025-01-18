package nl.fontys.s3.dinemasterbackend.business.dtos.create.users;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateUserResponse {
    private Long userId;
}
