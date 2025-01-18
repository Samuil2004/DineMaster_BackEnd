package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import nl.fontys.s3.dinemasterpro.business.dtos.get.orders_and_carts.CheckForCustomerActiveCartRequest;
import nl.fontys.s3.dinemasterpro.business.dtos.get.orders_and_carts.CheckForCustomerActiveCartResponse;
import nl.fontys.s3.dinemasterpro.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterpro.configuration.security.token.implementations.AccessTokenImpl;
import nl.fontys.s3.dinemasterpro.domain.classes.Cart;
import nl.fontys.s3.dinemasterpro.persistence.entity.CartEntity;
import nl.fontys.s3.dinemasterpro.persistence.repositories.CartEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CheckActiveCartForCustomerImplTest {
    @InjectMocks
    private CheckActiveCartForCustomerImpl checkActiveCartForCustomer;

    @Mock
    CartEntityRepository cartEntityRepository;

    CheckForCustomerActiveCartRequest checkForCustomerActiveCartRequest;
    CheckForCustomerActiveCartResponse checkForCustomerActiveCartResponse;
    CheckForCustomerActiveCartResponse checkForCustomerActiveCartResponse2;

    CartEntity cartEntity;
    Cart cartBase;
    AccessTokenImpl accessToken;
    AccessTokenImpl accessTokenFake;


    @BeforeEach
    void setUp() {
        checkForCustomerActiveCartRequest = CheckForCustomerActiveCartRequest.builder().customerId(1L).build();
        checkForCustomerActiveCartResponse = CheckForCustomerActiveCartResponse.builder().thereIsActiveCart(true).activeCartId(1L).build();
        checkForCustomerActiveCartResponse2 = CheckForCustomerActiveCartResponse.builder().thereIsActiveCart(false).build();

        cartEntity = CartEntity.builder()
                .cartId(1L)
                .customerId(1L)
                .price(1.23)
                .selectedItemEntities(new ArrayList<>())
                .isActive(true)
                .build();

        cartBase = Cart.builder()
                .cartId(1L)
                .customerId(1L)
                .price(1.23)
                .selectedItems(new ArrayList<>())
                .isActive(true)
                .build();

        accessToken = new AccessTokenImpl("test@test", 1L, Set.of("CUSTOMER"));
        accessTokenFake = new AccessTokenImpl("fake@fake", 2L, Set.of("CUSTOMER"));

    }

    @Test
    void givenCustomerId_whenCheckingIfCustomerHasActiveCart_returnsActiveCartAndCartId() {
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(checkForCustomerActiveCartRequest.getCustomerId())).thenReturn(Optional.ofNullable(cartEntity));

        CheckForCustomerActiveCartResponse response = checkActiveCartForCustomer.checkActiveCart(checkForCustomerActiveCartRequest, accessToken);

        assertEquals(response.getThereIsActiveCart(), checkForCustomerActiveCartResponse.getThereIsActiveCart());
        assertEquals(response.getActiveCartId(), checkForCustomerActiveCartResponse.getActiveCartId());

        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(checkForCustomerActiveCartRequest.getCustomerId());
    }

    @Test
    void givenCustomerId_whenCheckingIfCustomerHasActiveCart_returnsFalseThereIsActiveCart() {
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(checkForCustomerActiveCartRequest.getCustomerId())).thenReturn(Optional.empty());

        CheckForCustomerActiveCartResponse response = checkActiveCartForCustomer.checkActiveCart(checkForCustomerActiveCartRequest, accessToken);

        assertEquals(response.getThereIsActiveCart(), checkForCustomerActiveCartResponse2.getThereIsActiveCart());

        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(checkForCustomerActiveCartRequest.getCustomerId());
    }

    @Test
    void givenCustomerId_whenCheckingIfCustomerHasActiveCart_throwsErrorWhenAccessTokenBelongsToAnotherCustomer() {
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(checkForCustomerActiveCartRequest.getCustomerId())).thenReturn(Optional.ofNullable(cartEntity));

        AccessDenied exception = assertThrows(AccessDenied.class, () -> {
            checkActiveCartForCustomer.checkActiveCart(checkForCustomerActiveCartRequest, accessTokenFake);
        });

        assertEquals("Customers have access only to their own carts.", exception.getReason());
        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(checkForCustomerActiveCartRequest.getCustomerId());
    }
}