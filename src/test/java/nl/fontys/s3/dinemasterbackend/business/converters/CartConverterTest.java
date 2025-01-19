package nl.fontys.s3.dinemasterbackend.business.converters;

import nl.fontys.s3.dinemasterbackend.domain.classes.Cart;
import nl.fontys.s3.dinemasterbackend.domain.classes.SelectedItem;
import nl.fontys.s3.dinemasterbackend.domain.classes.SelectedPizza;
import nl.fontys.s3.dinemasterbackend.persistence.entity.CartEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.SelectedItemEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.SelectedPizzaEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CartConverterTest {
    @InjectMocks
    CartConverter cartConverter;

    @Mock
    private SelectedItemConverter selectedItemConverter;

    @Test
    void convertEntityToNormal_shouldConvertCorrectly() {
        SelectedItemEntity selectedItemEntity = SelectedPizzaEntity.builder()
                .selectedItemId(1L)
                .itemName("Pizza")
                .itemPrice(12.99)
                .build();

        CartEntity cartEntity = CartEntity.builder()
                .cartId(100L)
                .customerId(1L)
                .price(29.99)
                .selectedItemEntities(List.of(selectedItemEntity))
                .isActive(true)
                .build();

        SelectedItem selectedItem = SelectedPizza.builder()
                .selectedItemId(1L)
                .itemName("Pizza")
                .itemPrice(12.99)
                .build();

        when(selectedItemConverter.convertEntityToNormal(selectedItemEntity)).thenReturn(selectedItem);

        Cart cart = cartConverter.convertEntityToNormal(cartEntity);

        assertNotNull(cart);
        assertEquals(100L, cart.getCartId());
        assertEquals(1L, cart.getCustomerId());
        assertEquals(29.99, cart.getPrice());
        assertTrue(cart.getIsActive());

        verify(selectedItemConverter).convertEntityToNormal(selectedItemEntity);
        assertEquals(1, cart.getSelectedItems().size());
    }

    @Test
    void convertNormalToEntity_shouldConvertCorrectly() {
        SelectedItem selectedItem = SelectedPizza.builder()
                .selectedItemId(1L)
                .itemName("Pizza")
                .itemPrice(12.99)
                .build();

        Cart cart = Cart.builder()
                .cartId(100L)
                .customerId(1L)
                .price(29.99)
                .selectedItems(List.of(selectedItem))
                .isActive(true)
                .build();

        SelectedItemEntity selectedItemEntity = SelectedPizzaEntity.builder()
                .selectedItemId(1L)
                .itemName("Pizza")
                .itemPrice(12.99)
                .build();

        when(selectedItemConverter.convertNormalToEntity(selectedItem)).thenReturn(selectedItemEntity);

        CartEntity cartEntity = cartConverter.convertNormalToEntity(cart);

        assertNotNull(cartEntity);
        assertEquals(100L, cartEntity.getCartId());
        assertEquals(1L, cartEntity.getCustomerId());
        assertEquals(29.99, cartEntity.getPrice());
        assertTrue(cartEntity.getIsActive());

        verify(selectedItemConverter).convertNormalToEntity(selectedItem);
        assertEquals(1, cartEntity.getSelectedItemEntities().size());
    }
}