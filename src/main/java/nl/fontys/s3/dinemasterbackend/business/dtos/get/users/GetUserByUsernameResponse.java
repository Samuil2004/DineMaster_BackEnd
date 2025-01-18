package nl.fontys.s3.dinemasterbackend.business.dtos.get.users;

import lombok.Builder;
import lombok.Getter;
import nl.fontys.s3.dinemasterbackend.domain.classes.User;

@Builder
@Getter
public class GetUserByUsernameResponse {
    private User user;
}
