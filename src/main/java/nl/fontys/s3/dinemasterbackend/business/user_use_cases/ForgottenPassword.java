package nl.fontys.s3.dinemasterbackend.business.user_use_cases;

import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.HandleForgottenPasswordRequest;

public interface ForgottenPassword {
    void handleForgottenPassword(HandleForgottenPasswordRequest request);
}
