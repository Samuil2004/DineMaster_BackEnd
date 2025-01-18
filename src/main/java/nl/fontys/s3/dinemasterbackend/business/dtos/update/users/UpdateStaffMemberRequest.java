package nl.fontys.s3.dinemasterbackend.business.dtos.update.users;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStaffMemberRequest extends UpdateUserRequest {
    @NotNull(message = "Staff ID cannot be empty")
    private Long staffId;
}
