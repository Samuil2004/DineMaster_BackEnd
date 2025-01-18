package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.update.users.LogOutUserRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.LogoutUser;
import nl.fontys.s3.dinemasterbackend.persistence.entity.RefreshTokenEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.RefreshTokenEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LogoutUserImpl implements LogoutUser {
    private final RefreshTokenEntityRepository refreshTokenEntityRepository;

    @Override
    public void logoutUser(LogOutUserRequest request) {
        if (!request.getAccessToken().getUserId().equals(request.getUserId())) {
            throw new AccessDenied("Users can only manage their data.");
        }
        //Delete all refresh tokens for the logged-out user so that malicious users can not use them anymore
        List<RefreshTokenEntity> foundRefreshTokenByUserId = refreshTokenEntityRepository.findByUser_UserId(request.getUserId());
        refreshTokenEntityRepository.deleteAll(foundRefreshTokenByUserId);

    }
}
