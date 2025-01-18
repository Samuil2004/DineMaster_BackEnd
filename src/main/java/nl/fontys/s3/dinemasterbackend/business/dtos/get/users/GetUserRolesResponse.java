package nl.fontys.s3.dinemasterbackend.business.dtos.get.users;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class GetUserRolesResponse {
    List<String> allUserRolesNames;
}
