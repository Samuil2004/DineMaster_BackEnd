package nl.fontys.s3.dinemasterbackend.business.user_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.GetUserByRoleRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.GetUserByRoleResponse;

public interface GetUserByRole {
    GetUserByRoleResponse getUserByRoleAndLastName(GetUserByRoleRequest request);

    GetUserByRoleResponse getUserByRoleAndUsername(GetUserByRoleRequest request);
}
