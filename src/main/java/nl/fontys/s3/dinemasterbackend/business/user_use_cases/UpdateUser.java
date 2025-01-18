package nl.fontys.s3.dinemasterbackend.business.user_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.update.users.UpdateUserRequest;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;

public interface UpdateUser {
    void updateUser(UpdateUserRequest request, AccessToken accessToken);

}
