package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import nl.fontys.s3.dinemasterpro.business.dtos.update.users.LogOutUserRequest;
import nl.fontys.s3.dinemasterpro.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterpro.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterpro.configuration.security.token.implementations.AccessTokenImpl;
import nl.fontys.s3.dinemasterpro.persistence.entity.RefreshTokenEntity;
import nl.fontys.s3.dinemasterpro.persistence.repositories.RefreshTokenEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogoutUserImplTest {
    @Mock
    private RefreshTokenEntityRepository refreshTokenEntityRepository;

    @InjectMocks
    private LogoutUserImpl logoutUserImpl;

    private LogOutUserRequest validRequest;
    private LogOutUserRequest invalidRequest;
    private RefreshTokenEntity refreshToken1;
    private RefreshTokenEntity refreshToken2;

    @BeforeEach
    void setUp() {
        validRequest = LogOutUserRequest.builder()
                .userId(1L)
                .accessToken(mockAccessToken("test@test", "MANAGER", 1L))
                .build();

        invalidRequest = LogOutUserRequest.builder()
                .userId(1L)
                .accessToken(mockAccessToken("test@test", "MANAGER", 2L))
                .build();

        refreshToken1 = RefreshTokenEntity.builder()
                .token("token1")
                .id(1L)
                .build();

        refreshToken2 = RefreshTokenEntity.builder()
                .token("token2")
                .id(1L)
                .build();
    }

    private AccessToken mockAccessToken(String username, String role, Long userId) {
        return new AccessTokenImpl(username, userId, Set.of(role));
    }

    @Test
    void givenValidRequest_whenLoggingOutUser_thenDeletesRefreshTokens() {
        List<RefreshTokenEntity> refreshTokens = Arrays.asList(refreshToken1, refreshToken2);
        when(refreshTokenEntityRepository.findByUser_UserId(validRequest.getUserId())).thenReturn(refreshTokens);

        logoutUserImpl.logoutUser(validRequest);

        verify(refreshTokenEntityRepository).deleteAll(refreshTokens);
    }

    @Test
    void givenInvalidRequest_whenLoggingOutUser_thenThrowsAccessDenied() {
        AccessDenied exception = assertThrows(AccessDenied.class, () -> {
            logoutUserImpl.logoutUser(invalidRequest);
        });

        assertEquals("Users can only manage their data.", exception.getReason());
    }

    @Test
    void givenNoRefreshTokens_whenLoggingOutUser_thenDeletesNothing() {
        when(refreshTokenEntityRepository.findByUser_UserId(validRequest.getUserId())).thenReturn(Arrays.asList());

        logoutUserImpl.logoutUser(validRequest);

        verify(refreshTokenEntityRepository).deleteAll(Arrays.asList());
    }
}