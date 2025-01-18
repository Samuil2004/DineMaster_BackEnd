package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import nl.fontys.s3.dinemasterpro.business.dtos.get.users.GetUserRolesResponse;
import nl.fontys.s3.dinemasterpro.persistence.entity.RoleEntity;
import nl.fontys.s3.dinemasterpro.persistence.repositories.UserRoleEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserRolesImplTest {

    @Mock
    private UserRoleEntityRepository userRoleEntityRepository;

    @InjectMocks
    private GetUserRolesImpl getUserRolesImpl;

    RoleEntity roleAdmin;
    RoleEntity roleManager;
    RoleEntity roleCustomer;
    List<RoleEntity> roles;

    @BeforeEach
    void setUp() {
        roleAdmin = new RoleEntity(1L, "ADMIN");
        roleManager = new RoleEntity(2L, "MANAGER");
        roleCustomer = new RoleEntity(3L, "CUSTOMER");
        roles = Arrays.asList(roleAdmin, roleManager, roleCustomer);
    }

    @Test
    void whenGetUserRoles_thenReturnsListOfRoleNames() {
        when(userRoleEntityRepository.findAll()).thenReturn(roles);

        GetUserRolesResponse response = getUserRolesImpl.getUserRoles();

        assertNotNull(response);
        assertEquals(3, response.getAllUserRolesNames().size());
        assertTrue(response.getAllUserRolesNames().contains("ADMIN"));
        assertTrue(response.getAllUserRolesNames().contains("MANAGER"));
        assertTrue(response.getAllUserRolesNames().contains("CUSTOMER"));
    }

    @Test
    void whenNoRolesExist_thenReturnsEmptyRoleList() {
        GetUserRolesResponse response = getUserRolesImpl.getUserRoles();

        assertNotNull(response);
        assertTrue(response.getAllUserRolesNames().isEmpty());
    }
}