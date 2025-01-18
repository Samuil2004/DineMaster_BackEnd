package nl.fontys.s3.dinemasterbackend.business.user_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.GetUserByIdRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.GetUserByIdResponse;
import nl.fontys.s3.dinemasterbackend.domain.classes.User;


public interface GetUserById {
    //method to be used in a communication with the controllers
    GetUserByIdResponse getUserById(GetUserByIdRequest request);

    // method meant to be used only within the service classes
    User getUserByIdService(Long userId);
}
