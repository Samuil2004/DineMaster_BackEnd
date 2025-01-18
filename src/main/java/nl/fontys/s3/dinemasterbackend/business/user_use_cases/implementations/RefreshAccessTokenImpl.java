package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.AccessTokenResponse;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.RefreshTokenRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.RefreshAccessToken;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.RefreshTokenEncoder;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.implementations.AccessTokenImpl;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.implementations.RefreshTokenImpl;
import nl.fontys.s3.dinemasterbackend.persistence.entity.RefreshTokenEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.RefreshTokenEntityRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class RefreshAccessTokenImpl implements RefreshAccessToken {
    private final RefreshTokenEntityRepository refreshTokenEntityRepository;
    private final AccessTokenEncoder accessTokenEncoder;
    private final RefreshTokenEncoder refreshTokenEncoder;


    @Override
    public synchronized AccessTokenResponse refreshAccessToken(RefreshTokenRequest refreshTokenRequest) {

        Optional<RefreshTokenEntity> oldRefreshTokenEntity = refreshTokenEntityRepository.findByToken(refreshTokenRequest.getRefreshToken());
        if (oldRefreshTokenEntity.isEmpty()) {
            throw new AccessDenied("Refresh token not found");
        }
        if (Boolean.TRUE.equals(oldRefreshTokenEntity.get().getUsed())) {
            List<RefreshTokenEntity> findAllTokensByUserId = refreshTokenEntityRepository.findByUser_UserId(oldRefreshTokenEntity.get().getUser().getUserId());
            refreshTokenEntityRepository.deleteAll(findAllTokensByUserId);
            throw new AccessDenied("Refresh is invalid");
        }
        if (oldRefreshTokenEntity.get().getExpiresAt().isBefore(Instant.now())) {
            throw new AccessDenied("Refresh token has expired");
        }

        Set<String> roleNames = new HashSet<>();
        roleNames.add(oldRefreshTokenEntity.get().getUser().getRole().getName());
        String newAccessToken = accessTokenEncoder.encode(new AccessTokenImpl(oldRefreshTokenEntity.get().getUser().getEmail(), oldRefreshTokenEntity.get().getUser().getUserId(), roleNames));
        String newRefreshToken = refreshTokenEncoder.encode(new RefreshTokenImpl(oldRefreshTokenEntity.get().getUser().getEmail(), oldRefreshTokenEntity.get().getUser().getUserId()));

        RefreshTokenEntity newRefreshTokenEntity = RefreshTokenEntity.builder()
                .token(newRefreshToken)
                .user(oldRefreshTokenEntity.get().getUser())
                .expiresAt(Instant.now().plus(60, ChronoUnit.MINUTES))
                .used(false)
                .build();

        //Update the used status of the old refresh token from not used to used
        oldRefreshTokenEntity.get().setUsed(true);
        refreshTokenEntityRepository.save(oldRefreshTokenEntity.get());

        Optional<RefreshTokenEntity> alreadyAddedTokenBecauseOfReact = refreshTokenEntityRepository.findByToken(newRefreshTokenEntity.getToken());
        if (alreadyAddedTokenBecauseOfReact.isEmpty()) {
            refreshTokenEntityRepository.save(newRefreshTokenEntity);
        }

        return AccessTokenResponse.builder().accessToken(newAccessToken).refreshToken(newRefreshTokenEntity.getToken()).build();
    }
}
