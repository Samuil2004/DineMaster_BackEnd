package nl.fontys.s3.dinemasterbackend.business.dtos.get.users;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nl.fontys.s3.dinemasterbackend.domain.classes.User;

import java.util.List;

@Builder
@Getter
@Setter
public class GetUserByRoleResponse {
    private List<User> users;
    private Long totalResults;
}
