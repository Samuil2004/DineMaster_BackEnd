package nl.fontys.s3.dinemasterbackend.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.users.CreateCustomerRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.users.CreateStaffMemberRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.users.CreateUserResponse;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.AccessTokenResponse;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.AuthenticateUserRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.RefreshTokenRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.update.users.LogOutUserRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.Unauthorized;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.AuthenticateUser;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.CreateUser;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.RefreshAccessToken;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations.LogoutUserImpl;
import nl.fontys.s3.dinemasterbackend.configuration.security.auth.RequestAuthenticatedUserProvider;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final CreateUser createUser;
    private final AuthenticateUser authenticateUser;
    private RefreshAccessToken refreshAccessToken;
    private RequestAuthenticatedUserProvider requestAuthenticatedUserProvider;
    private final LogoutUserImpl logoutUser;
    private static final String UNAUTHORIZED_MESSAGE = "User not authenticated";



    @PostMapping("/customers")
    public ResponseEntity<CreateUserResponse> createCustomer(@RequestBody @Valid CreateCustomerRequest customerRequest) {
        CreateUserResponse createUserResponse = createUser.createUser(customerRequest);
        return ResponseEntity.ok(createUserResponse);
    }

    @RolesAllowed({"MANAGER"})
    @PostMapping("/staff")
    public ResponseEntity<CreateUserResponse> createStaff(@RequestBody @Valid CreateStaffMemberRequest staffMemberRequest) {
        CreateUserResponse createUserResponse = createUser.createUser(staffMemberRequest);
        return ResponseEntity.ok(createUserResponse);
    }

    @PostMapping("/logIn")
    public ResponseEntity<AccessTokenResponse> logIn(@RequestBody @Valid AuthenticateUserRequest request) {
        AccessTokenResponse accessTokenResponse = authenticateUser.authenticateUser(request);
        return ResponseEntity.ok(accessTokenResponse);
    }

    @PostMapping("/tokens/refresh")
    public ResponseEntity<AccessTokenResponse> refreshToken(@RequestParam(value = "refreshToken", required = false) String refreshToken) {
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder().refreshToken(refreshToken).build();
        AccessTokenResponse response = refreshAccessToken.refreshAccessToken(refreshTokenRequest);
        return ResponseEntity.ok(response);
    }

    @RolesAllowed({"MANAGER", "DELIVERY", "COOK", "CUSTOMER"})
    @PostMapping("/logout/{id}")
    public ResponseEntity<Void> logoutUser(@PathVariable("id") Long userId) {
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }
        LogOutUserRequest request = LogOutUserRequest.builder().userId(userId).accessToken(accessToken).build();
        logoutUser.logoutUser(request);
        return ResponseEntity.noContent().build();
    }
}
