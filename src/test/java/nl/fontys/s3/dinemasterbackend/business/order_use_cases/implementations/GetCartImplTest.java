package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import nl.fontys.s3.dinemasterpro.business.converters.CartConverter;
import nl.fontys.s3.dinemasterpro.business.dtos.get.orders_and_carts.GetCartRequest;
import nl.fontys.s3.dinemasterpro.business.dtos.get.orders_and_carts.GetCartResponse;
import nl.fontys.s3.dinemasterpro.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterpro.business.exceptions.NotFound;
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
class GetCartImplTest {

    @InjectMocks
    private GetCartImpl getCartImpl;

    @Mock
    CartEntityRepository cartEntityRepository;
    @Mock
    CartConverter cartConverter;

    GetCartRequest getCartRequest;
    CartEntity cartEntity;
    Cart cartBase;
    AccessTokenImpl accessToken;
    AccessTokenImpl fakeAccessToken;


    @BeforeEach
    void setUp() {
        getCartRequest = GetCartRequest.builder().customerId(1L).build();
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
        fakeAccessToken = new AccessTokenImpl("fake@fake", 2L, Set.of("CUSTOMER"));

    }

    @Test
    void givenCustomerId_whenLookingForACartForCustomer_thenReturnCart() {

        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(getCartRequest.getCustomerId())).thenReturn(Optional.ofNullable(cartEntity));
        when(cartConverter.convertEntityToNormal(cartEntity)).thenReturn(cartBase);

        GetCartResponse response = getCartImpl.getCart(getCartRequest, accessToken);

        assertEquals(cartBase, response.getCart());
        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(getCartRequest.getCustomerId());
        verify(cartConverter).convertEntityToNormal(cartEntity);
    }

    @Test
    void givenCustomerId_whenLookingForACartForCustomerThatDoesNotExist_thenThrowsAnErrorForNonExistingCart() {

        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(getCartRequest.getCustomerId())).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> {
            getCartImpl.getCart(getCartRequest, accessToken);
        });
        assertEquals("CART_NOT_FOUND", exception.getReason());

        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(getCartRequest.getCustomerId());
    }

    @Test
    void givenCustomerIdDifferentFromTheOneTheCartBelongsTo_whenLookingForACartForCustomerThatDoesNotExist_thenThrowsAccessDenied() {

        AccessDenied exception = assertThrows(AccessDenied.class, () -> {
            getCartImpl.getCart(getCartRequest, fakeAccessToken);
        });
        assertEquals("Customers can only access their own carts.", exception.getReason());

    }


}