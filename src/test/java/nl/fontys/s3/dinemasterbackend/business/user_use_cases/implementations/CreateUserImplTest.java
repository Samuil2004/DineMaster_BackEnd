package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import nl.fontys.s3.dinemasterpro.business.converters.UserConverter;
import nl.fontys.s3.dinemasterpro.business.dtos.create.users.CreateCustomerRequest;
import nl.fontys.s3.dinemasterpro.business.dtos.create.users.CreateStaffMemberRequest;
import nl.fontys.s3.dinemasterpro.business.dtos.create.users.CreateUserRequest;
import nl.fontys.s3.dinemasterpro.business.dtos.create.users.CreateUserResponse;
import nl.fontys.s3.dinemasterpro.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterpro.business.exceptions.OperationNotPossible;
import nl.fontys.s3.dinemasterpro.domain.classes.Customer;
import nl.fontys.s3.dinemasterpro.domain.classes.StaffMember;
import nl.fontys.s3.dinemasterpro.domain.classes.User;
import nl.fontys.s3.dinemasterpro.persistence.entity.CustomerEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.RoleEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.StaffMemberEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.UserEntity;
import nl.fontys.s3.dinemasterpro.persistence.repositories.UserEntityRepository;
import nl.fontys.s3.dinemasterpro.persistence.repositories.UserRoleEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserImplTest {
    @Mock
    private UserEntityRepository userEntityRepository;
    @Mock
    private UserRoleEntityRepository userRoleEntityRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private CreateUserImpl createUserImpl;

    private CreateCustomerRequest createCustomerRequest;
    private CreateStaffMemberRequest createStaffMemberRequest;
    private UserEntity existingUserEntity;
    private RoleEntity roleEntity;
    private RoleEntity roleEntityCustomer;
    private UserEntity userEntity;
    private UserEntity staffMemberEntity;
    private User user;
    private CreateUserRequest createUserRequest;


    @BeforeEach
    void setUp() {


        roleEntityCustomer = RoleEntity.builder().id(1L).name("CUSTOMER").build();
        roleEntity = RoleEntity.builder().id(1L).name("MANAGER").build();

        createUserRequest = CreateUserRequest.builder()
                .email("test@example.com")
                .password("password123")
                .confirmPassword("password123")
                .build();

        createCustomerRequest = CreateCustomerRequest.builder()
                .email("customer@example.com")
                .password("password123")
                .confirmPassword("password123")
                .build();

        userEntity = CustomerEntity.builder()
                .email("customer@example.com")
                .password("password123")
                .userId(1L)
                .role(roleEntityCustomer)
                .build();

        staffMemberEntity = StaffMemberEntity.builder()
                .email("staff@example.com")
                .password("password123")
                .userId(1L)
                .role(roleEntity)
                .build();

        user = User.builder()
                .userId(1L)
                .build();


        createStaffMemberRequest = CreateStaffMemberRequest.builder()
                .email("staff@example.com")
                .password("staffPassword123")
                .confirmPassword("staffPassword123")
                .userRole("MANAGER")
                .build();

        existingUserEntity = CustomerEntity.builder()
                .email("existing@example.com")
                .password("password123")
                .build();


    }

    @Test
    void givenPasswordDoesNotMatch_whenCreatingUser_thenThrowsOperationNotPossible() {
        createCustomerRequest.setConfirmPassword("differentPassword");

        OperationNotPossible exception = assertThrows(OperationNotPossible.class, () -> {
            createUserImpl.createUser(createCustomerRequest);
        });
        assertEquals("PASSWORDS DO NOT MATCH", exception.getReason());
    }

    @Test
    void givenUserWithEmailAlreadyExists_whenCreatingUser_thenThrowsOperationNotPossible() {
        when(userEntityRepository.findByEmail(createCustomerRequest.getEmail())).thenReturn(Optional.of(existingUserEntity));

        OperationNotPossible exception = assertThrows(OperationNotPossible.class, () -> {
            createUserImpl.createUser(createCustomerRequest);
        });
        assertEquals("USER WITH PROVIDED EMAIL ALREADY EXISTS", exception.getReason());
    }

    @Test
    void givenValidCustomerRequest_whenCreatingCustomer_thenCreatesUserSuccessfully() {
        when(userEntityRepository.findByEmail(createCustomerRequest.getEmail())).thenReturn(Optional.empty());
        when(userConverter.convertNormalToEntity(any(Customer.class))).thenReturn(userEntity);
        when(userEntityRepository.save(userEntity)).thenReturn(userEntity);
        when(userConverter.convertEntityToNormal(userEntity)).thenReturn(user);

        CreateUserResponse response = createUserImpl.createUser(createCustomerRequest);

        assertNotNull(response);
        assertNotNull(response.getUserId());
        verify(userEntityRepository).findByEmail(createCustomerRequest.getEmail());
        verify(userConverter).convertEntityToNormal(userEntity);
        verify(userConverter).convertNormalToEntity(any(Customer.class));
        verify(userEntityRepository).save(any(UserEntity.class));
    }

    @Test
    void givenValidStaffMemberRequest_whenCreatingStaffMember_thenCreatesStaffMemberSuccessfully() {
        when(userEntityRepository.findByEmail(createStaffMemberRequest.getEmail())).thenReturn(Optional.empty());
        when(userConverter.convertNormalToEntity(any(StaffMember.class))).thenReturn(staffMemberEntity);
        when(userEntityRepository.save(staffMemberEntity)).thenReturn(staffMemberEntity);
        when(userConverter.convertEntityToNormal(staffMemberEntity)).thenReturn(user);

        CreateUserResponse response = createUserImpl.createUser(createStaffMemberRequest);

        assertNotNull(response);
        assertNotNull(response.getUserId());
        verify(userEntityRepository).findByEmail(createStaffMemberRequest.getEmail());
    }

    @Test
    void givenInvalidRole_whenCreatingStaffMember_thenThrowsNotFound() {
        createStaffMemberRequest.setUserRole("INVALID_ROLE");

        when(userEntityRepository.findByEmail("staff@example.com")).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> {
            createUserImpl.createUser(createStaffMemberRequest);
        });
        assertEquals("USER ROLE NOT FOUND", exception.getReason());
    }

    @Test
    void givenUnknownUserType_whenCreatingUser_thenThrowsNotFound() {
        when(userEntityRepository.findByEmail(createUserRequest.getEmail())).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> {
            createUserImpl.createUser(createUserRequest);
        });
        assertEquals("USER_TYPE_NOT_FOUND", exception.getReason());
    }
}