package nl.fontys.s3.dinemasterbackend.business.user_use_cases.implementations;

import nl.fontys.s3.dinemasterbackend.business.converters.AddressConverter;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.implementations.AccessTokenImpl;
import nl.fontys.s3.dinemasterbackend.domain.classes.Address;
import nl.fontys.s3.dinemasterbackend.persistence.entity.AddressEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.CustomerEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.StaffMemberEntity;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserOrderDetailsImplTest {
    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private AddressConverter addressConverter;

    @InjectMocks
    private UpdateUserOrderDetailsImpl updateUserOrderDetails;

    private CustomerEntity customerEntity;
    private Address updatedAddress;
    private AddressEntity addressEntity;
    private AccessToken validAccessToken;
    AccessToken invalidAccessToken;
    StaffMemberEntity staffMemberEntity;

    @BeforeEach
    void setUp() {
        customerEntity = CustomerEntity.builder()
                .userId(1L)
                .phoneNumber("123456789")
                .build();

        updatedAddress = Address.builder()
                .country("Netherlands")
                .city("Eindhoven")
                .street("Main Street")
                .postalCode("1234AB")
                .build();

        addressEntity = AddressEntity.builder()
                .country("Netherlands")
                .city("Eindhoven")
                .street("Main Street")
                .postalCode("1234AB")
                .build();
        validAccessToken = mockAccessToken("test@test.com", "MANAGER", 1L);
        invalidAccessToken = mockAccessToken("test@test.com", "MANAGER", 2L);

        staffMemberEntity = StaffMemberEntity.builder().userId(10L).build();
    }

    private AccessToken mockAccessToken(String username, String role, Long userId) {
        return new AccessTokenImpl(username, userId, Set.of(role));
    }


    @Test
    void givenValidRequest_whenUpdatingOrderDetails_thenDetailsAreUpdatedSuccessfully() {
        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(customerEntity));
        when(addressConverter.convertNormalToEntity(updatedAddress)).thenReturn(addressEntity);

        updateUserOrderDetails.updateUserOrderDetails(1L, "987654321", updatedAddress, validAccessToken);

        assertEquals("987654321", customerEntity.getPhoneNumber());
        assertNotNull(customerEntity.getAddress());
        assertEquals(addressEntity, customerEntity.getAddress());
        verify(userEntityRepository).save(customerEntity);
    }

    @Test
    void givenMismatchedUserId_whenUpdatingOrderDetails_thenThrowsAccessDenied() {
        AccessDenied exception = assertThrows(AccessDenied.class, () ->
                updateUserOrderDetails.updateUserOrderDetails(1L, "987654321", updatedAddress, invalidAccessToken)
        );
        assertEquals("Users can only update their own information.", exception.getReason());
        verifyNoInteractions(userEntityRepository);
    }

    @Test
    void givenNonExistentUser_whenUpdatingOrderDetails_thenThrowsNotFound() {
        when(userEntityRepository.findById(1L)).thenReturn(Optional.empty()); // User not found

        NotFound exception = assertThrows(NotFound.class, () ->
                updateUserOrderDetails.updateUserOrderDetails(1L, "987654321", updatedAddress, validAccessToken)
        );
        assertEquals("USER_NOT_FOUND", exception.getReason());
        verifyNoMoreInteractions(userEntityRepository);
    }

    @Test
    void givenNonCustomerUser_whenUpdatingOrderDetails_thenThrowsNotFound() {
        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(staffMemberEntity)); // Non-customer entity

        NotFound exception = assertThrows(NotFound.class, () ->
                updateUserOrderDetails.updateUserOrderDetails(1L, "987654321", updatedAddress, validAccessToken)
        );
        assertEquals("USER_NOT_FOUND", exception.getReason());
    }

    @Test
    void givenAddressConversionFails_whenUpdatingOrderDetails_thenThrowsRuntimeException() {
        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(customerEntity));
        when(addressConverter.convertNormalToEntity(updatedAddress)).thenThrow(new RuntimeException("Conversion error"));
        
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                updateUserOrderDetails.updateUserOrderDetails(1L, "987654321", updatedAddress, validAccessToken)
        );
        assertEquals("Conversion error", exception.getMessage());
        verifyNoMoreInteractions(userEntityRepository);
    }
}