package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import nl.fontys.s3.dinemasterpro.business.dtos.get.AccessTokenResponse;
import nl.fontys.s3.dinemasterpro.business.dtos.get.users.RefreshTokenRequest;
import nl.fontys.s3.dinemasterpro.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterpro.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.dinemasterpro.configuration.security.token.RefreshTokenEncoder;
import nl.fontys.s3.dinemasterpro.configuration.security.token.implementations.AccessTokenImpl;
import nl.fontys.s3.dinemasterpro.configuration.security.token.implementations.RefreshTokenImpl;
import nl.fontys.s3.dinemasterpro.persistence.entity.RefreshTokenEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.RoleEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.StaffMemberEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.UserEntity;
import nl.fontys.s3.dinemasterpro.persistence.repositories.RefreshTokenEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshAccessTokenImplTest {
    @Mock
    private RefreshTokenEntityRepository refreshTokenEntityRepository;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @Mock
    private RefreshTokenEncoder refreshTokenEncoder;

    @InjectMocks
    private RefreshAccessTokenImpl refreshAccessToken;

    private RefreshTokenEntity oldRefreshTokenEntity;
    private UserEntity userEntity;
    private RoleEntity roleEntity;

    @BeforeEach
    void setUp() {
        roleEntity = RoleEntity.builder()
                .id(1L)
                .name("MANAGER")
                .build();

        userEntity = StaffMemberEntity.builder()
                .userId(1L)
                .email("test@example.com")
                .role(roleEntity)
                .build();

        oldRefreshTokenEntity = RefreshTokenEntity.builder()
                .token("old_refresh_token")
                .user(userEntity)
                .expiresAt(Instant.now().plusSeconds(3600))
                .used(false)
                .build();
    }

    @Test
    void givenValidRefreshToken_whenRefreshingAccessToken_thenReturnsNewAccessToken() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("old_refresh_token");
        when(refreshTokenEntityRepository.findByToken("old_refresh_token")).thenReturn(Optional.of(oldRefreshTokenEntity));
        when(accessTokenEncoder.encode(any(AccessTokenImpl.class))).thenReturn("new_access_token");
        when(refreshTokenEncoder.encode(any(RefreshTokenImpl.class))).thenReturn("new_refresh_token");

        AccessTokenResponse response = refreshAccessToken.refreshAccessToken(refreshTokenRequest);

        assertEquals("new_access_token", response.getAccessToken());
        assertEquals("new_refresh_token", response.getRefreshToken());
        assertTrue(oldRefreshTokenEntity.getUsed());

        //We have two savings in the database, so I have to check both of them and that each
        //is saving the right object
        verify(refreshTokenEntityRepository).save(argThat(token ->
                token.equals(oldRefreshTokenEntity) && token.getUsed()
        ));

        verify(refreshTokenEntityRepository).save(argThat(token ->
                "new_refresh_token".equals(token.getToken()) && !token.getUsed()
        ));
    }

    @Test
    void givenNonexistentRefreshToken_whenRefreshingAccessToken_thenThrowsAccessDenied() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("nonexistent_token");
        when(refreshTokenEntityRepository.findByToken("nonexistent_token")).thenReturn(Optional.empty());

        AccessDenied exception = assertThrows(AccessDenied.class, () ->
                refreshAccessToken.refreshAccessToken(refreshTokenRequest)
        );
        assertEquals("Refresh token not found", exception.getReason());
    }

    @Test
    void givenExpiredRefreshToken_whenRefreshingAccessToken_thenThrowsAccessDenied() {
        oldRefreshTokenEntity.setExpiresAt(Instant.now().minusSeconds(1));
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("old_refresh_token");
        when(refreshTokenEntityRepository.findByToken("old_refresh_token")).thenReturn(Optional.of(oldRefreshTokenEntity));

        AccessDenied exception = assertThrows(AccessDenied.class, () ->
                refreshAccessToken.refreshAccessToken(refreshTokenRequest)
        );
        assertEquals("Refresh token has expired", exception.getReason());
    }

    @Test
    void givenUsedRefreshToken_whenRefreshingAccessToken_thenThrowsAccessDeniedAndDeletesAllTokens() {
        oldRefreshTokenEntity.setUsed(true);
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("old_refresh_token");
        when(refreshTokenEntityRepository.findByToken("old_refresh_token")).thenReturn(Optional.of(oldRefreshTokenEntity));
        when(refreshTokenEntityRepository.findByUser_UserId(1L)).thenReturn(List.of(oldRefreshTokenEntity));

        AccessDenied exception = assertThrows(AccessDenied.class, () ->
                refreshAccessToken.refreshAccessToken(refreshTokenRequest)
        );
        assertEquals("Refresh is invalid", exception.getReason());
        verify(refreshTokenEntityRepository).deleteAll(List.of(oldRefreshTokenEntity));
    }

    @Test
    void givenDuplicateNewRefreshToken_whenRefreshingAccessToken_thenSkipsSavingDuplicate() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("old_refresh_token");
        RefreshTokenEntity newRefreshTokenEntity = RefreshTokenEntity.builder()
                .token("new_refresh_token")
                .build();

        when(refreshTokenEntityRepository.findByToken("old_refresh_token")).thenReturn(Optional.of(oldRefreshTokenEntity));
        when(accessTokenEncoder.encode(any(AccessTokenImpl.class))).thenReturn("new_access_token");
        when(refreshTokenEncoder.encode(any(RefreshTokenImpl.class))).thenReturn("new_refresh_token");
        when(refreshTokenEntityRepository.findByToken("new_refresh_token")).thenReturn(Optional.of(newRefreshTokenEntity));

        AccessTokenResponse response = refreshAccessToken.refreshAccessToken(refreshTokenRequest);

        assertEquals("new_access_token", response.getAccessToken());
        assertEquals("new_refresh_token", response.getRefreshToken());
    }
}