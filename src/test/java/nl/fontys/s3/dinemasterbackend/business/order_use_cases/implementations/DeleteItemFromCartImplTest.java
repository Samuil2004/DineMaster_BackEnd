package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import nl.fontys.s3.dinemasterbackend.business.dtos.delete.DeleteItemFromCartRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.implementations.AccessTokenImpl;
import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.CartEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.SelectedItemEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteItemFromCartImplTest {

    @InjectMocks
    private DeleteItemFromCartImpl deleteItemFromCartImpl;

    @Mock
    private CartEntityRepository cartEntityRepository;

    @Mock
    private SelectedItemEntityRepository selectedItemEntityRepository;

    private DeleteItemFromCartRequest request;
    private AccessToken accessToken;
    private CartEntity cartEntity;
    private SelectedPizzaEntity pizzaItem;
    private SelectedItemDifferentFromPizzaEntity otherItem;

    @BeforeEach
    void setUp() {
        request = DeleteItemFromCartRequest.builder()
                .cartId(1L)
                .selectedItemId(101L)
                .build();

        accessToken = new AccessTokenImpl("test@test.com", 1L, Set.of("CUSTOMER"));

        pizzaItem = SelectedPizzaEntity.builder()
                .selectedItemId(101L)
                .amount(2)
                .itemPrice(10.0)
                .build();

        otherItem = SelectedItemDifferentFromPizzaEntity.builder()
                .selectedItemId(102L)
                .amount(1)
                .itemFromMenu(AppetizerEntity.builder()
                        .itemPrice(5.0)
                        .build())
                .build();

        cartEntity = CartEntity.builder()
                .cartId(1L)
                .customerId(1L)
                .price(25.0)
                .selectedItemEntities(new ArrayList<>(List.of(pizzaItem, otherItem)))
                .build();
    }

    @Test
    void givenCartDoesNotExist_whenDeletingItem_thenThrowNotFound() {
        when(cartEntityRepository.findById(request.getCartId())).thenReturn(Optional.empty());

        assertThrows(NotFound.class, () -> deleteItemFromCartImpl.deleteItemFromCart(request, accessToken));

        verify(cartEntityRepository).findById(request.getCartId());
        verifyNoInteractions(selectedItemEntityRepository);
    }

    @Test
    void givenCartBelongsToAnotherCustomer_whenDeletingItem_thenThrowAccessDenied() {
        cartEntity.setCustomerId(2L);
        when(cartEntityRepository.findById(request.getCartId())).thenReturn(Optional.of(cartEntity));

        assertThrows(AccessDenied.class, () -> deleteItemFromCartImpl.deleteItemFromCart(request, accessToken));

        verify(cartEntityRepository).findById(request.getCartId());
        verifyNoInteractions(selectedItemEntityRepository);
    }

    @Test
    void givenItemDoesNotExistInCart_whenDeletingItem_thenThrowNotFound() {
        cartEntity.setSelectedItemEntities(new ArrayList<>());
        when(cartEntityRepository.findById(request.getCartId())).thenReturn(Optional.of(cartEntity));

        assertThrows(NotFound.class, () -> deleteItemFromCartImpl.deleteItemFromCart(request, accessToken));

        verify(cartEntityRepository).findById(request.getCartId());
        verifyNoInteractions(selectedItemEntityRepository);
    }

    @Test
    void givenPizzaItem_whenDeletingItem_thenUpdateCartPriceAndSave() {
        when(cartEntityRepository.findById(request.getCartId())).thenReturn(Optional.of(cartEntity));

        deleteItemFromCartImpl.deleteItemFromCart(request, accessToken);

        assertEquals(5.0, cartEntity.getPrice());
        assertEquals(1, cartEntity.getSelectedItemEntities().size());
        verify(cartEntityRepository).save(cartEntity);
        verify(selectedItemEntityRepository).deleteBySelectedItemId(request.getSelectedItemId());
    }

    @Test
    void givenLastItemInCart_whenDeletingItem_thenDeleteCart() {
        cartEntity.setSelectedItemEntities(new ArrayList<>(List.of(pizzaItem)));
        when(cartEntityRepository.findById(request.getCartId())).thenReturn(Optional.of(cartEntity));

        deleteItemFromCartImpl.deleteItemFromCart(request, accessToken);

        verify(cartEntityRepository).delete(cartEntity);
        verify(selectedItemEntityRepository).deleteBySelectedItemId(request.getSelectedItemId());
        verifyNoMoreInteractions(cartEntityRepository);
    }

    @Test
    void givenNonPizzaItem_whenDeletingItem_thenUpdateCartPriceAndSave() {
        request.setSelectedItemId(102L);
        when(cartEntityRepository.findById(request.getCartId())).thenReturn(Optional.of(cartEntity));

        deleteItemFromCartImpl.deleteItemFromCart(request, accessToken);

        assertEquals(20.0, cartEntity.getPrice());
        assertEquals(1, cartEntity.getSelectedItemEntities().size());
        verify(cartEntityRepository).save(cartEntity);
        verify(selectedItemEntityRepository).deleteBySelectedItemId(request.getSelectedItemId());
    }


}