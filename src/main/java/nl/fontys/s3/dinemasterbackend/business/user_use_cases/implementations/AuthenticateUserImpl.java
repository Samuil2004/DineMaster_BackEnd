package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.AccessTokenResponse;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.AuthenticateUserRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.Unauthorized;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.AuthenticateUser;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.RefreshTokenEncoder;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.implementations.AccessTokenImpl;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.implementations.RefreshTokenImpl;
import nl.fontys.s3.dinemasterbackend.persistence.entity.RefreshTokenEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.UserEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.RefreshTokenEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.UserEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthenticateUserImpl implements AuthenticateUser {
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;
    private final RefreshTokenEncoder refreshTokenEncoder;
    private final RefreshTokenEntityRepository refreshTokenEntityRepository;

    @Override
    public AccessTokenResponse authenticateUser(AuthenticateUserRequest request) {
        Optional<UserEntity> foundByUsername = userEntityRepository.findByEmail(request.getUsername());
        if (foundByUsername.isEmpty()) {
            throw new Unauthorized("Invalid email or password.");
        }
        if (!passwordEncoder.matches(request.getPassword(), foundByUsername.get().getPassword())) {
            throw new Unauthorized("Invalid email or password.");
        }

        Set<String> roleNames = new HashSet<>();
        roleNames.add(foundByUsername.get().getRole().getName());
        String accessToken = accessTokenEncoder.encode(new AccessTokenImpl(request.getUsername(), foundByUsername.get().getUserId(), roleNames));
        String refreshToken = refreshTokenEncoder.encode(new RefreshTokenImpl(request.getUsername(), foundByUsername.get().getUserId()));


        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .token(refreshToken)
                .user(foundByUsername.get())
                .expiresAt(Instant.now().plus(60, ChronoUnit.MINUTES))
                .used(false)
                .build();

        //Delete all previously issued refresh tokens for the user, so that the new ones can be saved
        List<RefreshTokenEntity> foundRefreshTokenByUserId = refreshTokenEntityRepository.findByUser_UserId(foundByUsername.get().getUserId());
        if (!foundRefreshTokenByUserId.isEmpty()) {
            refreshTokenEntityRepository.deleteAll(foundRefreshTokenByUserId);
        }


        refreshTokenEntityRepository.save(refreshTokenEntity);

        return AccessTokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }
}
