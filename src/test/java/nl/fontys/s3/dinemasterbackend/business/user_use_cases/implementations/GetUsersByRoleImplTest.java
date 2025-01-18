package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import nl.fontys.s3.dinemasterpro.domain.classes.Customer;
import nl.fontys.s3.dinemasterpro.domain.enumerations.UserRole;
import nl.fontys.s3.dinemasterpro.persistence.entity.CustomerEntity;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import nl.fontys.s3.dinemasterpro.business.converters.UserConverter;
import nl.fontys.s3.dinemasterpro.business.dtos.get.users.GetUserByRoleRequest;
import nl.fontys.s3.dinemasterpro.business.dtos.get.users.GetUserByRoleResponse;
import nl.fontys.s3.dinemasterpro.persistence.entity.RoleEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.UserEntity;
import nl.fontys.s3.dinemasterpro.persistence.repositories.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class GetUsersByRoleImplTest {
    @Mock
    private UserEntityRepository userEntityRepository;
    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private GetUsersByRoleImpl getUsersByRole;

    private GetUserByRoleRequest request;
    private CustomerEntity userEntity;
    private Customer customerBase;

    private RoleEntity roleEntity;

    @BeforeEach
    void setUp() {
        roleEntity = RoleEntity.builder().id(1L).name("CUSTOMER").build();

        userEntity = CustomerEntity.builder()
                .userId(1L)
                .email("user@example.com")
                .lastName("Jack")
                .role(roleEntity)
                .build();

        customerBase = Customer.builder()
                .userId(1L)
                .email("user@example.com")
                .lastName("Jack")
                .userRole(UserRole.CUSTOMER)
                .build();

        request = GetUserByRoleRequest.builder()
                .role("CUSTOMER")
                .username("Rockie")
                .pageNumber(1)
                .build();
    }

    @Test
    void givenValidRoleAndLastName_whenGetUserByRoleAndLastName_thenReturnsUsers() {
        Pageable pageable = PageRequest.of(0, 10);

        List<UserEntity> userEntities = List.of(userEntity);
        when(userEntityRepository.findByRole_NameAndLastNameContainingIgnoreCase("CUSTOMER", "Rockie", pageable))
                .thenReturn(userEntities);
        when(userEntityRepository.countByRole_NameAndLastNameContainingIgnoreCase("CUSTOMER", "Rockie"))
                .thenReturn(1L);
        when(userConverter.convertEntityToNormal(userEntity)).thenReturn(customerBase);

        GetUserByRoleResponse response = getUsersByRole.getUserByRoleAndLastName(request);

        assertNotNull(response);
        assertEquals(1, response.getTotalResults());
        assertEquals(1, response.getUsers().size());
        verify(userEntityRepository).findByRole_NameAndLastNameContainingIgnoreCase("CUSTOMER", "Rockie", pageable);
        verify(userEntityRepository).countByRole_NameAndLastNameContainingIgnoreCase("CUSTOMER", "Rockie");
    }

    @Test
    void givenRoleWithoutLastName_whenGetUserByRoleAndLastName_thenReturnsAllUsersWithRole() {
        Pageable pageable = PageRequest.of(0, 10);
        request.setUsername(null);
        List<UserEntity> userEntities = List.of(userEntity);
        when(userEntityRepository.findByRoleName("CUSTOMER", pageable))
                .thenReturn(userEntities);
        when(userEntityRepository.countByRoleName("CUSTOMER"))
                .thenReturn(1L);
        when(userConverter.convertEntityToNormal(userEntity)).thenReturn(customerBase);

        GetUserByRoleResponse response = getUsersByRole.getUserByRoleAndLastName(request);

        assertNotNull(response);
        assertEquals(1, response.getTotalResults());
        assertEquals(1, response.getUsers().size());
        verify(userEntityRepository).findByRoleName("CUSTOMER", pageable);
        verify(userEntityRepository).countByRoleName("CUSTOMER");
    }

    @Test
    void givenValidRoleAndEmail_whenGetUserByRoleAndUsername_thenReturnsUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        List<UserEntity> userEntities = List.of(userEntity);
        when(userEntityRepository.findByRole_nameAndEmailContainingIgnoreCase("CUSTOMER", "Rockie", pageable))
                .thenReturn(userEntities);
        when(userEntityRepository.countByRole_nameAndEmailContainingIgnoreCase("CUSTOMER", "Rockie"))
                .thenReturn(1L);
        when(userConverter.convertEntityToNormal(userEntity)).thenReturn(customerBase);

        GetUserByRoleResponse response = getUsersByRole.getUserByRoleAndUsername(request);

        assertNotNull(response);
        assertEquals(1, response.getTotalResults());
        assertEquals(1, response.getUsers().size());
        verify(userEntityRepository).findByRole_nameAndEmailContainingIgnoreCase("CUSTOMER", "Rockie", pageable);
        verify(userEntityRepository).countByRole_nameAndEmailContainingIgnoreCase("CUSTOMER", ("Rockie"));
    }

    @Test
    void givenRoleWithoutEmail_whenGetUserByRoleAndUsername_thenReturnsAllUsersWithRole() {
        Pageable pageable = PageRequest.of(0, 10);
        request.setUsername(null);
        List<UserEntity> userEntities = List.of(userEntity);
        when(userEntityRepository.findByRoleName("CUSTOMER", pageable))
                .thenReturn(userEntities);
        when(userEntityRepository.countByRoleName("CUSTOMER"))
                .thenReturn(1L);
        when(userConverter.convertEntityToNormal(userEntity)).thenReturn(customerBase);

        GetUserByRoleResponse response = getUsersByRole.getUserByRoleAndUsername(request);

        assertNotNull(response);
        assertEquals(1, response.getTotalResults());
        assertEquals(1, response.getUsers().size());
        verify(userEntityRepository).findByRoleName("CUSTOMER", pageable);
        verify(userEntityRepository).countByRoleName("CUSTOMER");
    }

    @Test
    void givenInvalidRole_whenGetUserByRole_thenReturnsEmptyResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        when(userEntityRepository.findByRole_NameAndLastNameContainingIgnoreCase("INVALID_ROLE", "Rockie", pageable))
                .thenReturn(List.of());
        when(userEntityRepository.countByRole_NameAndLastNameContainingIgnoreCase("INVALID_ROLE", "Rockie"))
                .thenReturn(0L);

        request.setRole("INVALID_ROLE");

        GetUserByRoleResponse response = getUsersByRole.getUserByRoleAndLastName(request);

        assertNotNull(response);
        assertEquals(0, response.getTotalResults());
        assertTrue(response.getUsers().isEmpty());
        verify(userEntityRepository).findByRole_NameAndLastNameContainingIgnoreCase("INVALID_ROLE", "Rockie", pageable);
        verify(userEntityRepository).countByRole_NameAndLastNameContainingIgnoreCase("INVALID_ROLE", "Rockie");
    }
}