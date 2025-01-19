package nl.fontys.s3.dinemasterbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrdersForCustomerRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.orders_and_carts.GetOrdersResponse;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.*;
import nl.fontys.s3.dinemasterbackend.business.dtos.update.users.UpdateCustomerRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.update.users.UpdateStaffMemberRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.Unauthorized;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.GetOrdersForCustomer;
import nl.fontys.s3.dinemasterbackend.business.user_use_cases.*;
import nl.fontys.s3.dinemasterbackend.configuration.security.auth.RequestAuthenticatedUserProvider;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final GetUserById getUserById;
    private final UpdateUser updateUser;
    private RequestAuthenticatedUserProvider requestAuthenticatedUserProvider;
    private final GetUserRoles getUserRoles;
    private final GetUserByRole getUsersByRole;
    private final GetOrdersForCustomer getOrdersForCustomer;
    private final ForgottenPassword forgottenPassword;
    private static final String UNAUTHORIZED_MESSAGE = "User not authenticated";


    /**
     * Endpoint for retrieving user details by user ID (Authenticated users only).
     *
     * @param userId The user ID to retrieve the details for.
     * @return The user details for the specified ID.
     */
    @Operation(
            summary = "Get user details",
            description = "This endpoint retrieves user details based on the provided user ID. " +
                    "Role-based access is enforced: MANAGER can view all users' information, " +
                    "DELIVERY can view customer and their own data, and CUSTOMER can view only their own data.",
            tags = {"User Management"}
    )
    @RolesAllowed({"MANAGER", "DELIVERY", "CUSTOMER", "COOK"})
    @GetMapping("/{id}")
    public ResponseEntity<GetUserByIdResponse> getUserById(@PathVariable("id") Long userId) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }

        GetUserByIdRequest request = GetUserByIdRequest.builder().userId(userId).accessToken(accessToken).build();
        GetUserByIdResponse response = getUserById.getUserById(request);
        return ResponseEntity.ok(response);
    }


    /**
     * Endpoint for updating customer account information (Customer only).
     *
     * @param updateCustomerRequest The updated customer information.
     * @param userId The customer ID to update.
     * @return A 204 No Content response indicating successful update.
     */
    @Operation(
            summary = "Update customer account information",
            description = "This endpoint allows customers to update their own personal information. " +
                    "Only the customer can update their information.",
            tags = {"User Management"}
    )
    @RolesAllowed({"CUSTOMER"})
    @PutMapping("/customers/{id}")
    public ResponseEntity<Void> updateCustomerAccountInfo(
            @Parameter(description = "The updated customer information")
            @RequestBody @Valid UpdateCustomerRequest updateCustomerRequest,

            @Parameter(description = "The customer ID to update")
            @PathVariable("id") Long userId) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }

        updateCustomerRequest.setUserId(userId);
        updateUser.updateUser(updateCustomerRequest, accessToken);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for updating staff member account information (Manager, Delivery, and Cook).
     *
     * @param updateStaffMemberRequest The updated staff member information.
     * @param userId The staff member ID to update.
     * @return A 204 No Content response indicating successful update.
     */
    @Operation(
            summary = "Update staff member account information",
            description = "This endpoint allows managers, delivery staff, and cooks to update their own or other staff members' information.",
            tags = {"User Management"}
    )
    @RolesAllowed({"MANAGER", "DELIVERY", "COOK"})
    @PutMapping("/staff/{id}")
    public ResponseEntity<Void> updateStaffMemberAccountInfo(
            @Parameter(description = "The updated staff member information")
            @RequestBody @Valid UpdateStaffMemberRequest updateStaffMemberRequest,

            @Parameter(description = "The staff member ID to update")
            @PathVariable("id") Long userId) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }

        updateStaffMemberRequest.setUserId(userId);
        updateUser.updateUser(updateStaffMemberRequest, accessToken);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for handling forgotten password (Public).
     *
     * @param handleForgottenPasswordRequest The request containing the email to send password reset instructions.
     * @return A 204 No Content response indicating successful password reset request.
     */
    @Operation(
            summary = "Forgotten password",
            description = "This endpoint handles forgotten password requests by sending a new password to the user's email.",
            tags = {"User Management"}
    )
    @PostMapping("/password/reset")
    public ResponseEntity<Void> forgottenPassword(
            @Parameter(description = "The email associated with the account to reset password")
            @RequestBody @Valid HandleForgottenPasswordRequest handleForgottenPasswordRequest) {

        forgottenPassword.handleForgottenPassword(handleForgottenPasswordRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for retrieving all user roles (Manager only).
     *
     * @return A list of all user roles.
     */
    @Operation(
            summary = "Get all user roles (Manager only)",
            description = "This endpoint allows managers to retrieve all available user roles.",
            tags = {"User Management"}
    )
    @RolesAllowed({"MANAGER"})
    @GetMapping("/roles")
    public ResponseEntity<GetUserRolesResponse> getAllUserRoles() {
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }
        GetUserRolesResponse response = getUserRoles.getUserRoles();
        return ResponseEntity.ok(response);
    }


    /**
     * Endpoint for retrieving users by role and last name (Manager only).
     *
     * @param role The role to filter users by.
     * @param username The last name to filter users by (optional).
     * @param pageNumber The page number for pagination.
     * @return A list of users filtered by role and last name.
     */
    @Operation(
            summary = "Get users by role and last name (Manager only)",
            description = "This endpoint allows managers to retrieve users by their role and last name.",
            tags = {"User Management"}
    )
    @RolesAllowed({"MANAGER"})
    @GetMapping("/getByRoleAndLastName/{role}")
    public ResponseEntity<GetUserByRoleResponse> getUsersByRoleAndLastName(
            @Parameter(description = "The role of the user to filter by")
            @PathVariable("role") String role,

            @Parameter(description = "The last name of the user to filter by")
            @RequestParam(value = "lastName", required = false) @Pattern(regexp = "\\S+", message = "Last name must not be blank.") String username,

            @Parameter(description = "The page number for pagination")
            @RequestParam(value = "pageNumber", required = true) @Min(value = 0, message = "Page number must not be less than 0.") Integer pageNumber) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }
        GetUserByRoleRequest request = GetUserByRoleRequest.builder().role(role).username(username).pageNumber(pageNumber).build();
        GetUserByRoleResponse response = getUsersByRole.getUserByRoleAndLastName(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for retrieving users by role and username (Manager only).
     *
     * @param role The role to filter users by.
     * @param username The username to filter users by (optional).
     * @param pageNumber The page number for pagination.
     * @return A list of users filtered by role and username.
     */
    @Operation(
            summary = "Get users by role and username (Manager only)",
            description = "This endpoint allows managers to retrieve users by their role and username.",
            tags = {"User Management"}
    )
    @RolesAllowed({"MANAGER"})
    @GetMapping("/getByRoleAndUsername/{role}")
    public ResponseEntity<GetUserByRoleResponse> getUsersByRoleAndUsername(
            @Parameter(description = "The role of the user to filter by")
            @PathVariable("role") String role,

            @Parameter(description = "The username of the user to filter by")
            @RequestParam(value = "username", required = false) String username,

            @Parameter(description = "The page number for pagination")
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }
        GetUserByRoleRequest request = GetUserByRoleRequest.builder().role(role).username(username).pageNumber(pageNumber).build();
        GetUserByRoleResponse response = getUsersByRole.getUserByRoleAndUsername(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for retrieving orders for a specific customer (Manager only).
     *
     * @param customerId The ID of the customer to retrieve orders for.
     * @param pageNumber The page number for pagination.
     * @return A list of orders for the specified customer.
     */
    @Operation(
            summary = "Get orders for customer (Manager only)",
            description = "This endpoint allows managers to retrieve all orders for a specific customer. The results are paginated.",
            tags = {"Order Management"}
    )
    @RolesAllowed({"MANAGER"})
    @GetMapping("/{customerId}/orders")
    public ResponseEntity<GetOrdersResponse> getOrdersForCustomer(
            @Parameter(description = "The ID of the customer to retrieve orders for")
            @PathVariable("customerId") Long customerId,

            @Parameter(description = "The page number for pagination")
            @RequestParam(value = "pageNumber", required = true) Integer pageNumber) {

        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }

        GetOrdersForCustomerRequest request = GetOrdersForCustomerRequest.builder()
                .customerId(customerId)
                .pageNumber(pageNumber)
                .build();
        GetOrdersResponse response = getOrdersForCustomer.getOrdersForCustomer(request);
        return ResponseEntity.ok(response);
    }

}
