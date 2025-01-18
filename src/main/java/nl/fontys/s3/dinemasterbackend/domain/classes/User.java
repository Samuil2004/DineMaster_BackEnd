package nl.fontys.s3.dinemasterbackend.domain.classes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import nl.fontys.s3.dinemasterbackend.domain.enumerations.UserRole;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class User {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole userRole;
    private String password;
}
