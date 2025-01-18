package nl.fontys.s3.dinemasterbackend.controller;

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


    //Authenticated users only
    //As a MANAGER I can see all user's personal information
    //As a DELIVERY I can see only customer's personal information and my personal information
    //As a CUSTOMER I can see only my personal information
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


    //As a user I can update only my personal information
    @RolesAllowed({"CUSTOMER"})
    @PutMapping("/customers/{id}")
    public ResponseEntity<Void> updateCustomerAccountInfo(@RequestBody @Valid UpdateCustomerRequest updateCustomerRequest, @PathVariable("id") Long userId) {
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }
        updateCustomerRequest.setUserId(userId);
        updateUser.updateUser(updateCustomerRequest, accessToken);
        return ResponseEntity.noContent().build();
    }

    //As a user I can update only my personal information
    @RolesAllowed({"MANAGER", "DELIVERY", "COOK"})
    @PutMapping("/staff/{id}")
    public ResponseEntity<Void> updateStaffMemberAccountInfo(@RequestBody @Valid UpdateStaffMemberRequest updateStaffMemberRequest, @PathVariable("id") Long userId) {
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }

        updateStaffMemberRequest.setUserId(userId);
        updateUser.updateUser(updateStaffMemberRequest, accessToken);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password/reset")
    public ResponseEntity<Void> forgottenPassword(@RequestBody @Valid HandleForgottenPasswordRequest handleForgottenPasswordRequest) {
        forgottenPassword.handleForgottenPassword(handleForgottenPasswordRequest);
        return ResponseEntity.noContent().build();
    }

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


    @RolesAllowed({"MANAGER"})
    @GetMapping("/getByRoleAndLastName/{role}")
    public ResponseEntity<GetUserByRoleResponse> getUsersByRoleAndLastName(@PathVariable("role") String role,
                                                                           @RequestParam(value = "lastName", required = false)
                                                                           @Pattern(regexp = "\\S+", message = "Last name must not be blank.") String username,
                                                                           @RequestParam(value = "pageNumber", required = true)
                                                                           @Min(value = 0, message = "Page number must not be less than 0.") Integer pageNumber) {
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }
        GetUserByRoleRequest request = GetUserByRoleRequest.builder().role(role).username(username).pageNumber(pageNumber).build();
        GetUserByRoleResponse response = getUsersByRole.getUserByRoleAndLastName(request);
        return ResponseEntity.ok(response);
    }

    @RolesAllowed({"MANAGER"})
    @GetMapping("/getByRoleAndUsername/{role}")
    public ResponseEntity<GetUserByRoleResponse> getUsersByRoleAndUsername(@PathVariable("role") String role,
                                                                           @RequestParam(value = "username", required = false) String username,
                                                                           @RequestParam(value = "pageNumber", required = false) Integer pageNumber) {
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }
        GetUserByRoleRequest request = GetUserByRoleRequest.builder().role(role).username(username).pageNumber(pageNumber).build();
        GetUserByRoleResponse response = getUsersByRole.getUserByRoleAndUsername(request);
        return ResponseEntity.ok(response);
    }

    @RolesAllowed({"MANAGER"})
    @GetMapping("/{customerId}/orders")
    public ResponseEntity<GetOrdersResponse> getOrdersForCustomer(@PathVariable("customerId") Long customerId,
                                                                  @RequestParam(value = "pageNumber", required = true) Integer pageNumber
    ) {
        AccessToken accessToken = requestAuthenticatedUserProvider.getAuthenticatedUserInRequest();
        if (accessToken == null) {
            throw new Unauthorized(UNAUTHORIZED_MESSAGE);
        }
        GetOrdersForCustomerRequest request = GetOrdersForCustomerRequest.builder().customerId(customerId).pageNumber(pageNumber).build();
        GetOrdersResponse response = getOrdersForCustomer.getOrdersForCustomer(request);
        return ResponseEntity.ok(response);
    }
}
