package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import nl.fontys.s3.dinemasterbackend.business.converters.UserConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.GetUserByIdRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.users.GetUserByIdResponse;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.implementations.AccessTokenImpl;
import nl.fontys.s3.dinemasterbackend.domain.classes.Address;
import nl.fontys.s3.dinemasterbackend.domain.classes.Customer;
import nl.fontys.s3.dinemasterbackend.domain.classes.StaffMember;
import nl.fontys.s3.dinemasterbackend.domain.classes.User;
import nl.fontys.s3.dinemasterbackend.domain.enumerations.UserRole;
import nl.fontys.s3.dinemasterbackend.persistence.entity.CustomerEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.RoleEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.StaffMemberEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.UserEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserByIdImplTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private GetUserByIdImpl getUserByIdImpl;
    private RoleEntity roleEntityCustomer;
    private RoleEntity roleEntityManager;
    private GetUserByIdRequest validRequest;
    private GetUserByIdRequest deliveryRequest;
    private GetUserByIdRequest customerRequest;
    private GetUserByIdRequest managerRequest;

    private UserEntity customerEntity;
    private UserEntity staffMemberEntity;
    private User customer;
    private User staffMember;

    private Address address;

    @BeforeEach
    void setUp() {

        roleEntityCustomer = RoleEntity.builder().id(1L).name("CUSTOMER").build();
        roleEntityManager = RoleEntity.builder().id(1L).name("MANAGER").build();

        validRequest = GetUserByIdRequest.builder()
                .userId(1L)
                .accessToken(mockAccessToken("test@test", "CUSTOMER", 1L))
                .build();

        deliveryRequest = GetUserByIdRequest.builder()
                .userId(2L)
                .accessToken(mockAccessToken("test@test", "DELIVERY", 2L))
                .build();

        customerRequest = GetUserByIdRequest.builder()
                .userId(1L)
                .accessToken(mockAccessToken("test@test", "CUSTOMER", 1L))
                .build();
        managerRequest = GetUserByIdRequest.builder()
                .userId(4L)
                .accessToken(mockAccessToken("test@test", "MANAGER", 1L))
                .build();

        customerEntity = CustomerEntity.builder()
                .userId(1L)
                .email("customer@example.com")
                .role(roleEntityCustomer)
                .build();

        address = Address.builder()
                .country("Netherlands")
                .city("city")
                .street("street")
                .postalCode("3642DD")
                .build();

        customer = Customer.builder()
                .userId(1L)
                .phoneNumber("+319898765")
                .address(address)
                .firstName("FN")
                .lastName("LN")
                .email("customer@example.com")
                .userRole(UserRole.CUSTOMER)
                .build();

        staffMember = StaffMember.builder()
                .userId(2L)
                .firstName("FN")
                .lastName("LN")
                .email("staff@example.com")
                .userRole(UserRole.DELIVERY)
                .build();

        staffMemberEntity = StaffMemberEntity.builder()
                .userId(4L)
                .email("staff@example.com")
                .role(roleEntityManager)
                .build();

    }

    private AccessToken mockAccessToken(String username, String role, Long userId) {
        return new AccessTokenImpl(username, userId, Set.of(role));
    }


    @Test
    void givenDeliveryRole_whenAccessingOtherUserInfo_thenThrowsAccessDenied() {
        when(userEntityRepository.findById(2L)).thenReturn(Optional.of(staffMemberEntity)); // Customer's info is being accessed

        AccessDenied exception = assertThrows(AccessDenied.class, () -> {
            getUserByIdImpl.getUserById(deliveryRequest);
        });
        assertEquals("Delivery users can only access their own information or customer information.", exception.getReason());
    }

    @Test
    void givenCustomerRole_whenAccessingOtherCustomerInfo_thenThrowsAccessDenied() {
        customerRequest.setUserId(2L);

        AccessDenied exception = assertThrows(AccessDenied.class, () -> {
            getUserByIdImpl.getUserById(customerRequest);
        });
        assertEquals("Customers can only access their own information.", exception.getReason());
    }

    @Test
    void givenValidCustomerRequest_whenFetchingCustomerInfo_thenReturnsCorrectResponse() {
        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(customerEntity));
        when(userConverter.convertEntityToNormal(customerEntity)).thenReturn(customer);

        GetUserByIdResponse response = getUserByIdImpl.getUserById(validRequest);

        assertNotNull(response);
        assertEquals(customer.getUserId(), response.getUserId());
        assertEquals(customer.getEmail(), response.getUsername());
        assertEquals(customer.getFirstName(), response.getFirstName());
        assertEquals(customer.getLastName(), response.getLastName());
        assertEquals(customer.getUserRole(), response.getUserRole());
    }

    @Test
    void givenValidStaffRequest_whenFetchingStaffInfo_thenReturnsCorrectResponse() {
        when(userEntityRepository.findById(4L)).thenReturn(Optional.of(staffMemberEntity));
        when(userConverter.convertEntityToNormal(staffMemberEntity)).thenReturn(staffMember);

        GetUserByIdResponse response = getUserByIdImpl.getUserById(managerRequest);

        assertNotNull(response);
        assertEquals(staffMember.getUserId(), response.getUserId());
        assertEquals(staffMember.getEmail(), response.getUsername());
        assertEquals(staffMember.getFirstName(), response.getFirstName());
        assertEquals(staffMember.getLastName(), response.getLastName());
        assertEquals(staffMember.getUserRole(), response.getUserRole());
    }

    @Test
    void givenUserNotFound_whenFetchingUser_thenThrowsNotFound() {
        when(userEntityRepository.findById(1L)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> {
            getUserByIdImpl.getUserById(validRequest);
        });
        assertEquals("USER_NOT_FOUND", exception.getReason());
    }

    @Test
    void givenUserId_whenGettingUserById_thenReturnsUser() {
        when(userEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(customerEntity));
        when(userConverter.convertEntityToNormal(customerEntity)).thenReturn(customer);

        User response = getUserByIdImpl.getUserByIdService(1L);

        assertNotNull(response);
        assertEquals(customer, response);
    }

    @Test
    void givenNonExistingUserId_whenGettingUserById_thenThrowsNotFound() {
        when(userEntityRepository.findById(1L)).thenReturn(Optional.empty());
        NotFound exception = assertThrows(NotFound.class, () -> {
            getUserByIdImpl.getUserByIdService(1L);
            
        });
        assertEquals("USER_NOT_FOUND", exception.getReason());
    }
}