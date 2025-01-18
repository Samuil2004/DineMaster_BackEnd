package nl.fontys.s3.dinemasterbackend.business.user_use_cases;

import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.domain.classes.Address;

public interface UpdateUserOrderDetails {
    void updateUserOrderDetails(Long userId, String phoneNumber, Address updatedAddress, AccessToken accessToken);

}
