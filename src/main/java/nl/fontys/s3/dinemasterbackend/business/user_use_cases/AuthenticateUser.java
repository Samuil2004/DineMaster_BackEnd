package nl.fontys.s3.dinemasterbackend.business.user_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.get.AccessTokenResponse;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.AuthenticateUserRequest;

public interface AuthenticateUser {
    AccessTokenResponse authenticateUser(AuthenticateUserRequest request);
}
