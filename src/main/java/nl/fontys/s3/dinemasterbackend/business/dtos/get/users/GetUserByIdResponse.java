package nl.fontys.s3.dinemasterbackend.business.dtos.get.users;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nl.fontys.s3.dinemasterbackend.domain.classes.Address;
import nl.fontys.s3.dinemasterbackend.domain.enumerations.UserRole;

@Builder
@Getter
@Setter
public class GetUserByIdResponse {
    private Long userId;
    private Long staffId;
    private String username;
    private String firstName;
    private String lastName;
    private UserRole userRole;
    private String phoneNumber;
    private Address address;
}
