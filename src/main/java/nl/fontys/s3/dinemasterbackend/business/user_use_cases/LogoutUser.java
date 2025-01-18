package nl.fontys.s3.dinemasterbackend.business.user_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.update.users.LogOutUserRequest;

public interface LogoutUser {
    void logoutUser(LogOutUserRequest request);
}
