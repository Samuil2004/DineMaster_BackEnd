package nl.fontys.s3.dinemasterbackend.business.user_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.create.users.CreateUserRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.users.CreateUserResponse;

public interface CreateUser {
    CreateUserResponse createUser(CreateUserRequest createUserRequest);
}
