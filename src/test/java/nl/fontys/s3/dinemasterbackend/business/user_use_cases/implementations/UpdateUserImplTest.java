package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import nl.fontys.s3.dinemasterpro.business.converters.UserConverter;
import nl.fontys.s3.dinemasterpro.business.dtos.update.users.UpdateCustomerRequest;
import nl.fontys.s3.dinemasterpro.business.dtos.update.users.UpdateStaffMemberRequest;
import nl.fontys.s3.dinemasterpro.business.dtos.update.users.UpdateUserRequest;
import nl.fontys.s3.dinemasterpro.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterpro.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterpro.business.exceptions.OperationNotPossible;
import nl.fontys.s3.dinemasterpro.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterpro.configuration.security.token.implementations.AccessTokenImpl;
import nl.fontys.s3.dinemasterpro.domain.classes.Customer;
import nl.fontys.s3.dinemasterpro.domain.classes.StaffMember;
import nl.fontys.s3.dinemasterpro.domain.enumerations.UserRole;
import nl.fontys.s3.dinemasterpro.persistence.entity.*;
import nl.fontys.s3.dinemasterpro.persistence.repositories.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserImplTest {
    @Mock
    private UserConverter userConverter;
    @Mock
    private UserEntityRepository userEntityRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UpdateUserImpl updateUser;

    private AccessToken accessToken;

    private UpdateCustomerRequest customerRequest;
    private UpdateStaffMemberRequest staffRequest;
    private UserEntity userEntity;
    private Customer customer;
    private StaffMember staffMember;
    UpdateCustomerRequest updateCustomerRequestValid;
    UpdateCustomerRequest updateCustomerRequestInValid;
    UserEntity duplicateUser;
    CustomerEntity customerEntity;
    StaffMemberEntity staffMemberEntity;
    AddressEntity addressEntity;
    RoleEntity roleEntityManager;
    UpdateUserRequest invalidRequest;

    @BeforeEach
    void setUp() {
        roleEntityManager = RoleEntity.builder().id(1L).name("MANAGER").build();
        updateCustomerRequestValid = UpdateCustomerRequest.builder().userId(1L).build();
        updateCustomerRequestInValid = UpdateCustomerRequest.builder().userId(2L).build();


        accessToken = mockAccessToken("test@test.com", "CUSTOMER", 1L);

        userEntity = CustomerEntity.builder()
                .userId(1L)
                .email("existing@example.com")
                .build();

        customer = Customer.builder()
                .userRole(UserRole.CUSTOMER)
                .build();

        staffMember = StaffMember.builder()
                .userRole(UserRole.MANAGER)
                .build();

        duplicateUser = CustomerEntity.builder()
                .userId(2L)
                .build();

        customerRequest = UpdateCustomerRequest.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .confirmPassword("password")
                .country("Netherlands")
                .city("Eindhoven")
                .postalCode("1234AB")
                .street("Main Street")
                .phoneNumber("1234567890")
                .build();

        addressEntity = AddressEntity.builder()
                .country("Netherlands")
                .city("Eindhoven")
                .postalCode("1234AB")
                .street("Main Street")
                .build();

        customerEntity = CustomerEntity.builder()
                .userId(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .address(addressEntity)
                .phoneNumber("1234567890")
                .build();

        staffMemberEntity = StaffMemberEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .staffId(1L)
                .role(roleEntityManager)
                .build();

        // Common UpdateStaffMemberRequest for StaffMember updates
        staffRequest = UpdateStaffMemberRequest.builder()
                .userId(1L)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .password("password")
                .confirmPassword("password")
                .staffId(1L)
                .build();

        invalidRequest = UpdateUserRequest.builder()
                .userId(1L)
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .password("password")
                .confirmPassword("password")
                .build();

    }

    private AccessToken mockAccessToken(String username, String role, Long userId) {
        return new AccessTokenImpl(username, userId, Set.of(role));
    }

    @Test
    void givenInvalidAccessToken_whenUpdateUser_thenThrowsAccessDenied() {
        customerRequest.setUserId(2L);

        AccessDenied exception = assertThrows(AccessDenied.class,
                () -> updateUser.updateUser(customerRequest, accessToken));
        assertEquals("Users can only update their own information.", exception.getReason());
    }

    @Test
    void givenMismatchedPasswords_whenUpdateUser_thenThrowsOperationNotPossible() {
        customerRequest.setPassword("password");
        customerRequest.setConfirmPassword("differentPassword");

        OperationNotPossible exception = assertThrows(OperationNotPossible.class,
                () -> updateUser.updateUser(customerRequest, accessToken));
        assertEquals("PASSWORDS DO NOT MATCH", exception.getReason());
    }

    @Test
    void givenUserNotFound_whenUpdateUser_thenThrowsNotFound() {
        when(userEntityRepository.findById(1L)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class,
                () -> updateUser.updateUser(customerRequest, accessToken));
        assertEquals("USER_NOT_FOUND", exception.getReason());
    }

    @Test
    void givenDuplicateEmail_whenUpdateUser_thenThrowsOperationNotPossible() {
        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        when(userEntityRepository.findByEmail(customerRequest.getEmail())).thenReturn(Optional.of(duplicateUser));

        OperationNotPossible exception = assertThrows(OperationNotPossible.class,
                () -> updateUser.updateUser(customerRequest, accessToken));
        assertEquals("USER WITH PROVIDED EMAIL ALREADY EXISTS", exception.getReason());
    }

    @Test
    void givenValidCustomerUpdateRequest_whenUpdateUser_thenUpdatesCustomer() {
        when(userEntityRepository.findById(customerRequest.getUserId())).thenReturn(Optional.of(userEntity));
        when(userEntityRepository.findByEmail(customerRequest.getEmail())).thenReturn(Optional.ofNullable(null));

        when(userConverter.convertEntityToNormal(userEntity)).thenReturn(customer);
        when(passwordEncoder.encode(customerRequest.getPassword())).thenReturn("encodedPassword");
        when(userConverter.convertNormalToEntity(any(Customer.class))).thenReturn(customerEntity);

        updateUser.updateUser(customerRequest, accessToken);

        verify(userEntityRepository).save(customerEntity);
    }

    @Test
    void givenValidStaffUpdateRequest_whenUpdateUser_thenUpdatesStaffMember() {
        when(userEntityRepository.findById(staffRequest.getUserId())).thenReturn(Optional.of(userEntity));
        when(userEntityRepository.findByEmail(staffRequest.getEmail())).thenReturn(Optional.ofNullable(null));

        when(userConverter.convertEntityToNormal(userEntity)).thenReturn(staffMember);
        when(passwordEncoder.encode(staffRequest.getPassword())).thenReturn("encodedPassword");
        when(userConverter.convertNormalToEntity(any(StaffMember.class))).thenReturn(staffMemberEntity);

        updateUser.updateUser(staffRequest, accessToken);
        verify(userEntityRepository).save(staffMemberEntity);

    }

    @Test
    void givenValidInvalidRequestWithNoRole_whenUpdateUser_thenThrowsNotFoundError() {
        when(userEntityRepository.findById(staffRequest.getUserId())).thenReturn(Optional.of(userEntity));
        when(userEntityRepository.findByEmail(staffRequest.getEmail())).thenReturn(Optional.ofNullable(null));

        when(userConverter.convertEntityToNormal(userEntity)).thenReturn(staffMember);


        NotFound exception = assertThrows(NotFound.class,
                () -> updateUser.updateUser(invalidRequest, accessToken));

        assertEquals("USER_NOT_FOUND", exception.getReason());
    }


}