package nl.fontys.s3.dinemasterbackend.business.converters;

import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.domain.classes.Address;
import nl.fontys.s3.dinemasterbackend.domain.classes.Customer;
import nl.fontys.s3.dinemasterbackend.domain.classes.StaffMember;
import nl.fontys.s3.dinemasterbackend.domain.classes.User;
import nl.fontys.s3.dinemasterbackend.domain.enumerations.UserRole;
import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.UserRoleEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserConverterTest {
    @Mock
    private AddressConverter addressConverter;

    @Mock
    private UserRoleEntityRepository userRoleEntityRepository;

    @InjectMocks
    private UserConverter userConverter;

    @Test
    void convertEntityToNormal_shouldConvertCustomerEntityToCustomer() {
        AddressEntity addressEntity = mock(AddressEntity.class);
        Address address = mock(Address.class);
        when(addressConverter.convertEntityToNormal(addressEntity)).thenReturn(address);

        RoleEntity roleEntity = RoleEntity.builder().name("CUSTOMER").build();
        CustomerEntity customerEntity = CustomerEntity.builder()
                .userId(1L)
                .firstName("Michael")
                .lastName("Jackson")
                .email("michael.jackson@example.com")
                .phoneNumber("987654321")
                .role(roleEntity)
                .address(addressEntity)
                .password("password123")
                .build();

        User result = userConverter.convertEntityToNormal(customerEntity);

        assertNotNull(result);
        assertTrue(result instanceof Customer);
        Customer customer = (Customer) result;
        assertEquals(1L, customer.getUserId());
        assertEquals("Michael", customer.getFirstName());
        assertEquals("Jackson", customer.getLastName());
        assertEquals("michael.jackson@example.com", customer.getEmail());
        assertEquals("987654321", customer.getPhoneNumber());
        assertEquals(UserRole.CUSTOMER, customer.getUserRole());
        assertEquals(address, customer.getAddress());
        assertEquals("password123", customer.getPassword());
    }

    @Test
    void convertEntityToNormal_shouldConvertStaffMemberEntityToStaffMember() {
        RoleEntity roleEntity = RoleEntity.builder().name("MANAGER").build();
        StaffMemberEntity staffEntity = StaffMemberEntity.builder()
                .userId(2L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .password("password456")
                .staffId(100L)
                .role(roleEntity)
                .build();

        User result = userConverter.convertEntityToNormal(staffEntity);

        assertNotNull(result);
        assertTrue(result instanceof StaffMember);
        StaffMember staff = (StaffMember) result;
        assertEquals(2L, staff.getUserId());
        assertEquals("Jane", staff.getFirstName());
        assertEquals("Smith", staff.getLastName());
        assertEquals("jane.smith@example.com", staff.getEmail());
        assertEquals("password456", staff.getPassword());
        assertEquals(100L, staff.getStaffId());
        assertEquals(UserRole.MANAGER, staff.getUserRole());
    }

    @Test
    void convertEntityToNormal_shouldReturnNullForUnknownEntityType() {
        UserEntity unknownEntity = mock(UserEntity.class);

        User result = userConverter.convertEntityToNormal(unknownEntity);

        assertNull(result);
    }

    @Test
    void convertNormalToEntity_shouldConvertCustomerToCustomerEntity() {
        Address address = mock(Address.class);
        AddressEntity addressEntity = mock(AddressEntity.class);
        when(addressConverter.convertNormalToEntity(address)).thenReturn(addressEntity);

        RoleEntity roleEntity = RoleEntity.builder().name("CUSTOMER").build();
        when(userRoleEntityRepository.findByName("CUSTOMER")).thenReturn(Optional.of(roleEntity));

        Customer customer = Customer.builder()
                .userId(1L)
                .firstName("Michael")
                .lastName("Jackson")
                .email("michael.jackson@example.com")
                .phoneNumber("987654321")
                .userRole(UserRole.CUSTOMER)
                .address(address)
                .password("password123")
                .build();

        UserEntity result = userConverter.convertNormalToEntity(customer);

        assertNotNull(result);
        assertTrue(result instanceof CustomerEntity);
        CustomerEntity customerEntity = (CustomerEntity) result;
        assertEquals(1L, customerEntity.getUserId());
        assertEquals("Michael", customerEntity.getFirstName());
        assertEquals("Jackson", customerEntity.getLastName());
        assertEquals("michael.jackson@example.com", customerEntity.getEmail());
        assertEquals("987654321", customerEntity.getPhoneNumber());
        assertEquals(roleEntity, customerEntity.getRole());
        assertEquals(addressEntity, customerEntity.getAddress());
        assertEquals("password123", customerEntity.getPassword());
    }

    @Test
    void convertNormalToEntity_shouldConvertStaffMemberToStaffMemberEntity() {
        RoleEntity roleEntity = RoleEntity.builder().name("MANAGER").build();
        when(userRoleEntityRepository.findByName("MANAGER")).thenReturn(Optional.of(roleEntity));

        StaffMember staff = StaffMember.builder()
                .userId(2L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .password("password456")
                .staffId(100L)
                .userRole(UserRole.MANAGER)
                .build();

        UserEntity result = userConverter.convertNormalToEntity(staff);

        assertNotNull(result);
        assertTrue(result instanceof StaffMemberEntity);
        StaffMemberEntity staffEntity = (StaffMemberEntity) result;
        assertEquals(2L, staffEntity.getUserId());
        assertEquals("Jane", staffEntity.getFirstName());
        assertEquals("Smith", staffEntity.getLastName());
        assertEquals("jane.smith@example.com", staffEntity.getEmail());
        assertEquals("password456", staffEntity.getPassword());
        assertEquals(100L, staffEntity.getStaffId());
        assertEquals(roleEntity, staffEntity.getRole());
    }

    @Test
    void convertNormalToEntity_shouldThrowExceptionWhenRoleNotFound() {
        when(userRoleEntityRepository.findByName("CUSTOMER")).thenReturn(Optional.empty());

        Customer customer = Customer.builder()
                .userId(1L)
                .firstName("Michael")
                .lastName("Jackson")
                .email("michael.jackson@example.com")
                .phoneNumber("987654321")
                .userRole(UserRole.CUSTOMER)
                .build();

        NotFound exception = assertThrows(NotFound.class, () -> userConverter.convertNormalToEntity(customer));
        assertEquals("USER ROLE NOT FOUND", exception.getReason());
    }
}