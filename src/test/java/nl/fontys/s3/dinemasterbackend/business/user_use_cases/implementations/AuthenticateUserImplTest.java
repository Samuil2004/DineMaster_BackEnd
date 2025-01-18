package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import nl.fontys.s3.dinemasterpro.business.dtos.get.AccessTokenResponse;
import nl.fontys.s3.dinemasterpro.business.dtos.get.users.AuthenticateUserRequest;
import nl.fontys.s3.dinemasterpro.business.exceptions.Unauthorized;
import nl.fontys.s3.dinemasterpro.configuration.security.token.AccessTokenEncoder;
import nl.fontys.s3.dinemasterpro.configuration.security.token.RefreshTokenEncoder;
import nl.fontys.s3.dinemasterpro.configuration.security.token.implementations.AccessTokenImpl;
import nl.fontys.s3.dinemasterpro.configuration.security.token.implementations.RefreshTokenImpl;
import nl.fontys.s3.dinemasterpro.persistence.entity.CustomerEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.RefreshTokenEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.RoleEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.UserEntity;
import nl.fontys.s3.dinemasterpro.persistence.repositories.RefreshTokenEntityRepository;
import nl.fontys.s3.dinemasterpro.persistence.repositories.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserImplTest {

    @InjectMocks
    private AuthenticateUserImpl authenticateUserImpl;

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @Mock
    private RefreshTokenEncoder refreshTokenEncoder;

    @Mock
    private RefreshTokenEntityRepository refreshTokenEntityRepository;

    private AuthenticateUserRequest request;
    private UserEntity userEntity;
    private RefreshTokenEntity refreshTokenEntity;
    private RoleEntity roleEntity;
    private AccessTokenImpl accessToken;
    private RefreshTokenImpl refreshToken;

    @BeforeEach
    void setUp() {
        request = AuthenticateUserRequest
                .builder()
                .username("test@test.com")
                .password("password123$")
                .build();

        roleEntity = RoleEntity.builder().id(1L).name("CUSTOMER").build();

        userEntity = CustomerEntity.builder()
                .email("test@test.com")
                .password("encodedPassword")
                .userId(1L)
                .role(roleEntity)
                .build();

        refreshTokenEntity = RefreshTokenEntity.builder()
                .token("refreshToken")
                .user(userEntity)
                .expiresAt(Instant.now().plus(60, ChronoUnit.MINUTES))
                .used(false).build();

        Set<String> roleNames = new HashSet<>();
        roleNames.add(
                "CUSTOMER"
        );
        accessToken = new AccessTokenImpl("test@test.com", 1L, roleNames);
        refreshToken = new RefreshTokenImpl("test@test.com", 1L);
    }

    @Test
    void givenValidCredentials_whenAuthenticateUser_thenReturnAccessTokenAndRefreshToken() {
        when(userEntityRepository.findByEmail(request.getUsername())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(request.getPassword(), userEntity.getPassword())).thenReturn(true);
        when(accessTokenEncoder.encode(accessToken)).thenReturn("accessToken");
        when(refreshTokenEncoder.encode(refreshToken)).thenReturn("refreshToken");
        when(refreshTokenEntityRepository.findByUser_UserId(1L)).thenReturn(List.of());
        when(refreshTokenEntityRepository.save(any(RefreshTokenEntity.class))).thenReturn(refreshTokenEntity);

        AccessTokenResponse response = authenticateUserImpl.authenticateUser(request);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(userEntityRepository).findByEmail(request.getUsername());
        verify(passwordEncoder).matches(request.getPassword(), userEntity.getPassword());
        verify(accessTokenEncoder).encode(accessToken);
        verify(refreshTokenEncoder).encode(refreshToken);
        verify(refreshTokenEntityRepository).findByUser_UserId(1L);
        verify(refreshTokenEntityRepository).save(any(RefreshTokenEntity.class));
    }

    @Test
    void givenInvalidUsername_whenAuthenticateUser_thenThrowUnauthorized() {
        when(userEntityRepository.findByEmail(request.getUsername())).thenReturn(Optional.empty());

        Unauthorized exception = assertThrows(Unauthorized.class, () -> {
            authenticateUserImpl.authenticateUser(request);
        });
        assertEquals("Invalid email or password.", exception.getReason());
        verify(userEntityRepository).findByEmail(request.getUsername());
    }

    @Test
    void givenInvalidPassword_whenAuthenticateUser_thenThrowUnauthorized() {
        when(userEntityRepository.findByEmail(request.getUsername())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(request.getPassword(), userEntity.getPassword())).thenReturn(false);

        Unauthorized exception = assertThrows(Unauthorized.class, () -> {
            authenticateUserImpl.authenticateUser(request);
        });
        assertEquals("Invalid email or password.", exception.getReason());
        verify(userEntityRepository).findByEmail(request.getUsername());
        verify(passwordEncoder).matches(request.getPassword(), userEntity.getPassword());
    }

    @Test
    void givenValidCredentials_whenRefreshTokensExist_thenDeleteOldTokensAndSaveNew() {
        when(userEntityRepository.findByEmail(request.getUsername())).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(request.getPassword(), userEntity.getPassword())).thenReturn(true);
        when(accessTokenEncoder.encode(any(AccessTokenImpl.class))).thenReturn("accessToken");
        when(refreshTokenEncoder.encode(any(RefreshTokenImpl.class))).thenReturn("refreshToken");
        when(refreshTokenEntityRepository.findByUser_UserId(1L)).thenReturn(List.of(refreshTokenEntity));
        when(refreshTokenEntityRepository.save(any(RefreshTokenEntity.class))).thenReturn(refreshTokenEntity);

        AccessTokenResponse response = authenticateUserImpl.authenticateUser(request);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(refreshTokenEntityRepository).deleteAll(any());
        verify(refreshTokenEntityRepository).save(any(RefreshTokenEntity.class));
    }


}