package nl.fontys.s3.dinemasterbackend.business.dtos.create.users;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateStaffMemberRequest extends CreateUserRequest {
    @NotNull(message = "User role cannot be empty")
    private String userRole;
}
