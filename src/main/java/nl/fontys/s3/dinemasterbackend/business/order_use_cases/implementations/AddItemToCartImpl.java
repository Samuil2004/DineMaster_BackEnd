package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.SelectedItemConverter;
import nl.fontys.s3.dinemasterbackend.business.converters.SelectedItemStatusOfPreparationConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.AddItemDifferentFromPizzaToCartRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.AddItemToCartRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.AddPizzaToCartRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.items.GetItemByIdRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.items.GetItemByIdResponse;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.item_use_cases.GetItemById;
import nl.fontys.s3.dinemasterbackend.business.order_use_cases.AddItemToCart;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.AccessToken;
import nl.fontys.s3.dinemasterbackend.domain.classes.*;
import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.CartEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.PizzaSizeRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.SelectedItemStatusOfPreparationEntityRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AddItemToCartImpl implements AddItemToCart {
    private final GetItemById getItemById;
    private final CartEntityRepository cartEntityRepository;
    private final PizzaSizeRepository pizzaSizeRepository;
    private final SelectedItemStatusOfPreparationEntityRepository selectedItemStatusOfPreparationEntityRepository;
    private final SelectedItemConverter selectedItemConverter;
    private final SelectedItemStatusOfPreparationConverter selectedItemStatusOfPreparationConverter;


    @Override
    @Transactional
    public void addItemToCart(AddItemToCartRequest addItemToCartRequest, AccessToken accessToken) {

        if (!addItemToCartRequest.getCustomerId().equals(accessToken.getUserId())) {
            throw new AccessDenied("Customers can only add items to their personal carts.");
        }

        GetItemByIdRequest getItemByIdRequest = GetItemByIdRequest.builder().itemId(addItemToCartRequest.getItemOfReferenceId()).build();
        GetItemByIdResponse getItemByIdResponse = getItemById.getItemById(getItemByIdRequest);

        SelectedItemEntity selectedItemEntity = defineAndCreateSelectedItem(addItemToCartRequest, getItemByIdResponse);

        Optional<CartEntity> customerCart = cartEntityRepository.findByCustomerIdAndIsActiveTrue(addItemToCartRequest.getCustomerId());
        if (customerCart.isPresent()) {
            if (selectedItemEntity instanceof SelectedPizzaEntity selectedPizzaEntity) {
                validateAndSavePizzaToCart(customerCart.get(), selectedPizzaEntity, selectedItemEntity);
            } else if (selectedItemEntity instanceof SelectedItemDifferentFromPizzaEntity differentFromPizzaEntity) {
                validateAndSaveOtherThanPizzaItemToCart(customerCart.get(), differentFromPizzaEntity, selectedItemEntity);
            }
        } else {
            CartEntity cartOfTheCustomer;
            if (selectedItemEntity instanceof SelectedPizzaEntity pizza) {

                cartOfTheCustomer = CartEntity.builder()
                        .customerId(addItemToCartRequest.getCustomerId())
                        .price(pizza.getItemPrice() * selectedItemEntity.getAmount())
                        .selectedItemEntities(List.of(selectedItemEntity))
                        .isActive(true)
                        .build();
            } else {
                cartOfTheCustomer = CartEntity.builder()
                        .customerId(addItemToCartRequest.getCustomerId())
                        .price(selectedItemEntity.getItemOfReference().getItemPrice() * selectedItemEntity.getAmount())
                        .selectedItemEntities(List.of(selectedItemEntity))
                        .isActive(true)
                        .build();
            }
            cartEntityRepository.save(cartOfTheCustomer);
        }

    }

    private void addNewPizzaToCartAndSave(CartEntity customerCart, SelectedPizzaEntity selectedPizzaEntity) {
        List<SelectedItemEntity> selectedItemsWithinTheCart = new ArrayList<>(customerCart.getSelectedItemEntities());

        selectedItemsWithinTheCart.add(selectedPizzaEntity);
        customerCart.setSelectedItemEntities(selectedItemsWithinTheCart);
        customerCart.setPrice(customerCart.getPrice() + selectedPizzaEntity.getAmount() * selectedPizzaEntity.getItemPrice());
        cartEntityRepository.save(customerCart);
    }

    private PizzaSizeEntity getPizzaPriceBySizeAndId(Long itemId, String size) {
        List<PizzaSizeEntity> allSizesForPizzaById = pizzaSizeRepository.findByPizza_ItemId(itemId);
        PizzaSizeEntity foundPizzaSize = allSizesForPizzaById.stream().filter(pizzaSize -> pizzaSize.getSize().equals(size)).findFirst().orElse(null);
        if (foundPizzaSize == null) {
            throw new NotFound("ITEM_NOT_FOUND");
        }
        return foundPizzaSize;
    }

    private SelectedItemEntity defineAndCreateSelectedItem(AddItemToCartRequest addItemToCartRequest, GetItemByIdResponse getItemByIdResponse) {
        Item item = getItemByIdResponse.getFoundItem();
        Optional<SelectedItemStatusOfPreparationEntity> foundStatus = selectedItemStatusOfPreparationEntityRepository.findByStatusName("NOT STARTED");
        if (addItemToCartRequest instanceof AddPizzaToCartRequest pizzaRequest && item instanceof Pizza pizza && foundStatus.isPresent()) {
            PizzaSizeEntity selectedPizzaSize = getPizzaPriceBySizeAndId(item.getItemId(), pizzaRequest.getPizzaSize());
            PizzaSize selectedSize = PizzaSize.builder().pizzaSizeId(selectedPizzaSize.getId()).size(selectedPizzaSize.getSize()).additionalPrice(selectedPizzaSize.getAdditionalPrice()).build();
            SelectedItem selectedItem = SelectedPizza.builder()
                    .itemOfReference(item)
                    .itemName(item.getItemName())
                    .itemImageVersion(item.getItemImageVersion())
                    .itemPrice(item.getItemPrice() + selectedSize.getAdditionalPrice())
                    .sizes(selectedSize)
                    .base(pizza.getBase())
                    .amount(pizzaRequest.getQuantity())
                    .itemCategory(item.getItemCategory())
                    .comment(addItemToCartRequest.getComment())
                    .statusOfPreparation(selectedItemStatusOfPreparationConverter.convertEntityToNormal(foundStatus.get()))
                    .build();

            return selectedItemConverter.convertNormalToEntity(selectedItem);

        } else if (addItemToCartRequest instanceof AddItemDifferentFromPizzaToCartRequest otherItemRequest && foundStatus.isPresent()) {
            SelectedItem selectedItem = SelectedItemDifferentFromPizza.builder()
                    .itemOfReference(item)
                    .itemFromMenu(item)
                    .amount(otherItemRequest.getQuantity())
                    .itemCategory(item.getItemCategory())
                    .comment(addItemToCartRequest.getComment())
                    .statusOfPreparation(selectedItemStatusOfPreparationConverter.convertEntityToNormal(foundStatus.get()))
                    .build();

            return selectedItemConverter.convertNormalToEntity(selectedItem);
        }
        throw new NotFound("ITEM NOT FOUND");
    }

    private void validateAndSavePizzaToCart(CartEntity customerCart, SelectedPizzaEntity selectedPizzaEntity, SelectedItemEntity selectedItemEntity) {
        Optional<SelectedItemEntity> foundPizzaByProvidedItemOfReferenceAndSize = customerCart.getSelectedItemEntities().stream().filter(pizza -> pizza.getItemOfReference().getItemId().equals(selectedPizzaEntity.getItemOfReference().getItemId()) && ((SelectedPizzaEntity) pizza).getSizes().getSize().equals(selectedPizzaEntity.getSizes().getSize()) && pizza.getComment().equals(selectedPizzaEntity.getComment())).findFirst();
        if (foundPizzaByProvidedItemOfReferenceAndSize.isPresent()) {
            foundPizzaByProvidedItemOfReferenceAndSize.get().setAmount(foundPizzaByProvidedItemOfReferenceAndSize.get().getAmount() + selectedItemEntity.getAmount());
            if (foundPizzaByProvidedItemOfReferenceAndSize.get() instanceof SelectedPizzaEntity pizza) {
                customerCart.setPrice(customerCart.getPrice() + selectedItemEntity.getAmount() * pizza.getItemPrice());
                cartEntityRepository.save(customerCart);
            }
        } else {
            addNewPizzaToCartAndSave(customerCart, selectedPizzaEntity);
        }
    }

    private void validateAndSaveOtherThanPizzaItemToCart(CartEntity customerCart, SelectedItemDifferentFromPizzaEntity differentFromPizzaEntity, SelectedItemEntity selectedItemEntity) {
        Optional<SelectedItemEntity> foundItemById = customerCart.getSelectedItemEntities().stream().filter(pizza -> pizza.getItemOfReference().getItemId().equals(differentFromPizzaEntity.getItemOfReference().getItemId()) && pizza.getComment().equals(differentFromPizzaEntity.getComment())).findFirst();
        if (foundItemById.isPresent()) {
            foundItemById.get().setAmount(foundItemById.get().getAmount() + selectedItemEntity.getAmount());
            if (foundItemById.get() instanceof SelectedItemDifferentFromPizzaEntity otherItem) {
                customerCart.setPrice(customerCart.getPrice() + selectedItemEntity.getAmount() * otherItem.getItemFromMenu().getItemPrice());
            }
            cartEntityRepository.save(customerCart);

        } else {
            List<SelectedItemEntity> selectedItemsWithinTheCart = new ArrayList<>(customerCart.getSelectedItemEntities());

            selectedItemsWithinTheCart.add(selectedItemEntity);
            customerCart.setSelectedItemEntities(selectedItemsWithinTheCart);

            SelectedItemDifferentFromPizzaEntity otherItem = (SelectedItemDifferentFromPizzaEntity) selectedItemEntity;
            customerCart.setPrice(customerCart.getPrice() + selectedItemEntity.getAmount() * otherItem.getItemFromMenu().getItemPrice());
            cartEntityRepository.save(customerCart);

        }
    }

}
