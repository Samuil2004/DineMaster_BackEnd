package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.dtos.delete.DeleteItemFromCartRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.DeleteItemFromCart;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.CartEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.SelectedItemEntityRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DeleteItemFromCartImpl implements DeleteItemFromCart {
    private final CartEntityRepository cartEntityRepository;
    private final SelectedItemEntityRepository selectedItemEntityRepository;

    @Override
    @Transactional
    public void deleteItemFromCart(DeleteItemFromCartRequest request, AccessToken accessToken) {
        Optional<CartEntity> foundCart = cartEntityRepository.findById(request.getCartId());

        if (foundCart.isPresent() && !foundCart.get().getCustomerId().equals(accessToken.getUserId())) {
            throw new AccessDenied("Customers can remove items from their carts.");
        }

        if (foundCart.isEmpty()) {
            throw new NotFound("CART_NOT_FOUND");
        }
        List<SelectedItemEntity> selectedItemsWithinTheCart = new ArrayList<>(foundCart.get().getSelectedItemEntities());
        SelectedItemEntity selectedItem = selectedItemsWithinTheCart.stream().filter(item -> item.getSelectedItemId().equals(request.getSelectedItemId())).findFirst().orElse(null);

        if (selectedItem == null) {
            throw new NotFound("ITEM_NOT_FOUND");
        }
        selectedItemsWithinTheCart.remove(selectedItem);
        foundCart.get().setSelectedItemEntities(selectedItemsWithinTheCart);
        if (selectedItem instanceof SelectedPizzaEntity pizza) {
            foundCart.get().setPrice(foundCart.get().getPrice() - selectedItem.getAmount() * pizza.getItemPrice());
        } else if (selectedItem instanceof SelectedItemDifferentFromPizzaEntity otherItem) {
            foundCart.get().setPrice(foundCart.get().getPrice() - selectedItem.getAmount() * otherItem.getItemFromMenu().getItemPrice());
        }
        if (selectedItemsWithinTheCart.isEmpty()) {
            selectedItemEntityRepository.deleteBySelectedItemId(request.getSelectedItemId());
            cartEntityRepository.delete(foundCart.get());

        } else {
            selectedItemEntityRepository.deleteBySelectedItemId(request.getSelectedItemId());
            cartEntityRepository.save(foundCart.get());

        }
    }
}
