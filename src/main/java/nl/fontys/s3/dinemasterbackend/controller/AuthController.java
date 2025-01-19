package nl.fontys.s3.dinemasterbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import io.swagger.v3.oas.annotations.Parameter;
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


    /**
     * Endpoint to create a new customer user.
     *
     * @param customerRequest The customer details required for account creation.
     * @return The created user response containing the user's details.
     */
    @Operation(
            summary = "Create a new customer user",
            description = "This endpoint allows for the creation of a new customer account. " +
                    "The user will be registered and an account will be created with the provided details. Newly created user ID will be returned",
            tags = {"User Management"}
    )
    @PostMapping("/customers")
    public ResponseEntity<CreateUserResponse> createCustomer(
            @Parameter(description = "The customer details required for account creation")
            @RequestBody @Valid CreateCustomerRequest customerRequest) {
        CreateUserResponse createUserResponse = createUser.createUser(customerRequest);
        return ResponseEntity.ok(createUserResponse);
    }


    /**
     * Endpoint to create a new staff member (Manager only).
     *
     * @param staffMemberRequest The staff member details required for account creation.
     * @return The created user response containing the staff member's details.
     */
    @Operation(
            summary = "Create a new staff member (Manager only)",
            description = "This endpoint allows Managers to create new staff members. Only available for users with the 'MANAGER' role.",
            tags = {"User Management"}
    )
    @RolesAllowed({"MANAGER"})
    @PostMapping("/staff")
    public ResponseEntity<CreateUserResponse> createStaff(
            @Parameter(description = "The staff member details required for account creation")
            @RequestBody @Valid CreateStaffMemberRequest staffMemberRequest) {
        CreateUserResponse createUserResponse = createUser.createUser(staffMemberRequest);
        return ResponseEntity.ok(createUserResponse);
    }

    /**
     * Endpoint for user login, returning an access token.
     *
     * @param request The user's login credentials (username and password).
     * @return The access token response containing the JWT token.
     */
    @Operation(
            summary = "User login",
            description = "Authenticates the user with their credentials and returns an access token for further API requests.",
            tags = {"Authentication"}
    )
    @PostMapping("/logIn")
    public ResponseEntity<AccessTokenResponse> logIn(
            @Parameter(description = "The credentials required for user authentication (username and password)")
            @RequestBody @Valid AuthenticateUserRequest request) {
        AccessTokenResponse accessTokenResponse = authenticateUser.authenticateUser(request);
        return ResponseEntity.ok(accessTokenResponse);
    }

    /**
     * Endpoint to refresh the access token using a valid refresh token.
     *
     * @param refreshToken The refresh token used to generate a new access token.
     * @return A new access token response.
     */
    @Operation(
            summary = "Refresh access token",
            description = "This endpoint allows the user to refresh their access token using a valid refresh token. " +
                    "The new access token will be returned to the user.",
            tags = {"Authentication"}
    )
    @PostMapping("/tokens/refresh")
    public ResponseEntity<AccessTokenResponse> refreshToken(
            @Parameter(description = "The refresh token to generate a new access token")
            @RequestParam(value = "refreshToken", required = false) String refreshToken) {
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder().refreshToken(refreshToken).build();
        AccessTokenResponse response = refreshAccessToken.refreshAccessToken(refreshTokenRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for logging out a user (Manager, Delivery, Cook, or Customer).
     *
     * @param userId The ID of the user logging out.
     * @return A 204 No Content response indicating successful logout.
     * @throws Unauthorized If the user is not authenticated.
     */
    @Operation(
            summary = "Logout user",
            description = "Logs out a user from the system by invalidating their access token. Available for roles: Manager, Delivery, Cook, and Customer.",
            tags = {"Authentication"}
    )
    @RolesAllowed({"MANAGER", "DELIVERY", "COOK", "CUSTOMER"})
    @PostMapping("/logout/{id}")
    public ResponseEntity<Void> logoutUser(
            @Parameter(description = "The ID of the user to log out")
            @PathVariable("id") Long userId) {
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }
        LogOutUserRequest request = LogOutUserRequest.builder().userId(userId).accessToken(accessToken).build();
        logoutUser.logoutUser(request);
        return ResponseEntity.noContent().build();
    }
}
