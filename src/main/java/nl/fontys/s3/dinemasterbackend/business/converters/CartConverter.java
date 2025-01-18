package nl.fontys.s3.dinemasterbackend.business.converters;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.domain.classes.Cart;
import nl.fontys.s3.dinemasterbackend.persistence.entity.CartEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CartConverter
{
    private final SelectedItemConverter selectedItemConverter;
    public Cart convertEntityToNormal(CartEntity cartEntity)
    {
        return Cart.builder()
                .cartId(cartEntity.getCartId())
                .customerId(cartEntity.getCustomerId())
                .price(cartEntity.getPrice())
                .selectedItems(cartEntity.getSelectedItemEntities()
                        .stream()
                        .map(selectedItemConverter::convertEntityToNormal).toList())
                .isActive(cartEntity.getIsActive())
                .build();
    }
    public CartEntity convertNormalToEntity (Cart cart)
    {
        return CartEntity.builder()
                .cartId(cart.getCartId())
                .customerId(cart.getCustomerId())
                .price(cart.getPrice())
                .selectedItemEntities(cart.getSelectedItems()
                        .stream()
                        .map(selectedItemConverter::convertNormalToEntity).toList())
                .isActive(cart.getIsActive())
                .build();
    }
}
