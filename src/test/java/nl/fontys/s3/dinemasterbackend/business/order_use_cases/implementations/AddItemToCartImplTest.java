package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import nl.fontys.s3.dinemasterbackend.business.converters.CartConverter;
import nl.fontys.s3.dinemasterbackend.business.converters.SelectedItemConverter;
import nl.fontys.s3.dinemasterbackend.business.converters.SelectedItemStatusOfPreparationConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.AddItemDifferentFromPizzaToCartRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.AddItemToCartRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.orders.AddPizzaToCartRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.items.GetItemByIdRequest;
import nl.fontys.s3.dinemasterbackend.business.dtos.get.items.GetItemByIdResponse;
import nl.fontys.s3.dinemasterbackend.business.item_use_cases.GetItemById;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.exceptions.AccessDenied;
import nl.fontys.s3.dinemasterbackend.configuration.security.token.implementations.AccessTokenImpl;
import nl.fontys.s3.dinemasterbackend.domain.classes.*;

import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.CartEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.PizzaSizeRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.SelectedItemStatusOfPreparationEntityRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddItemToCartImplTest {
    @InjectMocks
    private AddItemToCartImpl addItemToCartImpl;

    @Mock
    private GetItemById getItemById;

    @Mock
    private CartEntityRepository cartEntityRepository;

    @Mock
    private PizzaSizeRepository pizzaSizeRepository;

    @Mock
    private SelectedItemStatusOfPreparationEntityRepository selectedItemStatusOfPreparationEntityRepository;

    @Mock
    private SelectedItemConverter selectedItemConverter;

    @Mock
    private CartConverter cartConverter;

    @Mock
    private SelectedItemStatusOfPreparationConverter selectedItemStatusOfPreparationConverter;

    PizzaSizeEntity pizzaSizeEntity1;
    PizzaSizeEntity pizzaSizeEntity2;
    PizzaSizeEntity pizzaSizeEntity3;
    PizzaSize pizzaSizeBase1;
    PizzaSize pizzaSizeBase2;
    PizzaSize pizzaSizeBase3;
    AddItemToCartRequest updatePizzaInCartRequest;
    AddItemToCartRequest addNewPizzaInCartRequest;
    AddItemToCartRequest addNewPizzaInCartRequestWithNonExistingSize;
    AddItemToCartRequest updateItemDifferentFromPizzaInCartRequest;
    AddItemToCartRequest addNewItemDifferentFromPizzaInCartRequest;
    AddItemToCartRequest simpleEmptyRequestWithCustomerIdOnly;
    AddItemToCartRequest simpleEmptyRequestWithWrongCustomerId;

    GetItemByIdResponse getPizzaByIdResponse;
    GetItemByIdResponse getPizzaByIdResponse2;
    GetItemByIdResponse getPizzaByIdResponse3;
    GetItemByIdResponse getPizzaByIdResponse4;

    PizzaEntity peperoniEntity;
    Pizza peperoniBase;
    PizzaEntity marinaraEntity;
    Pizza marinaraBase;
    ItemCategoryEntity itemCategoryEntityPizza;
    ItemCategory itemCategoryBasePizza;
    SelectedItemStatusOfPreparationEntity selectedItemStatusOfPreparationEntity;
    SelectedItemStatusOfPreparation selectedItemStatusOfPreparationBase;
    SelectedItemEntity selectedPizzaThatExistsInCartWithExistingSize;
    SelectedItemEntity selectedPizzaThatDoesNotExistInCart;
    SelectedItemEntity selectedPizzaThatExistsInCartWithNonExistingSize;

    SelectedItemEntity selectedItemDifferentFromPizzaThatExistsInCart;
    SelectedItemEntity selectedItemDifferentFromPizzaThatDoesNotExistInCart;

    AppetizerEntity appetizerEntity;
    Appetizer appetizerBase;
    AppetizerEntity garlicBreadEntity;
    Appetizer garlicBreadBase;
    ItemCategoryEntity itemCategoryEntityAppetizer;
    ItemCategory itemCategoryBaseAppetizer;

    CartEntity cartEntity;
    CartEntity cartEntityWithPizzaOnly;
    CartEntity cartEntityWithItemDifferentFromPizzaOnly;
    AccessTokenImpl accessToken;

    @BeforeEach
    void setUp() {

        itemCategoryEntityPizza = ItemCategoryEntity.builder().categoryId(1L).categoryName("PIZZA").build();
        itemCategoryBasePizza = ItemCategory.builder().categoryId(1L).categoryName("PIZZA").build();

        itemCategoryEntityAppetizer = ItemCategoryEntity.builder().categoryId(1L).categoryName("APPETIZER").build();
        itemCategoryBaseAppetizer = ItemCategory.builder().categoryId(1L).categoryName("APPETIZER").build();


        pizzaSizeEntity1 = PizzaSizeEntity.builder().size("small").additionalPrice(1.0).build();
        pizzaSizeEntity2 = PizzaSizeEntity.builder().size("large").additionalPrice(2.0).build();
        pizzaSizeEntity3 = PizzaSizeEntity.builder().size("jumbo").additionalPrice(3.0).build();

        pizzaSizeBase1 = PizzaSize.builder().size("small").additionalPrice(1.0).build();
        pizzaSizeBase2 = PizzaSize.builder().size("large").additionalPrice(2.0).build();
        pizzaSizeBase3 = PizzaSize.builder().size("jumbo").additionalPrice(3.0).build();

        selectedItemStatusOfPreparationEntity = SelectedItemStatusOfPreparationEntity.builder().id(1L).statusName("NOT STARTED").build();
        selectedItemStatusOfPreparationBase = SelectedItemStatusOfPreparation.builder().id(1L).statusName("NOT STARTED").build();

        peperoniEntity = PizzaEntity.builder()
                .itemId(1L)
                .itemName("Peperoni")
                .itemImageVersion("v1730296343")
                .itemPrice(12.50)
                .visibleInMenu(true)
                .itemCategory(itemCategoryEntityPizza)
                .sizes(List.of(pizzaSizeEntity1, pizzaSizeEntity2, pizzaSizeEntity3))
                .base("Thin Crust")
                .ingredients(List.of("dough", "sauce", "cheese", "pepperoni", "oliveOil", "seasoning", "garlicPowder"))
                .build();

        peperoniBase = Pizza.builder()
                .itemId(1L)
                .itemName("Peperoni")
                .itemImageVersion("v1730296343")
                .itemPrice(12.50)
                .visibleInMenu(true)
                .itemCategory(itemCategoryBasePizza)
                .sizes(List.of(pizzaSizeBase1, pizzaSizeBase2, pizzaSizeBase3))
                .base("Thin Crust")
                .ingredients(List.of("dough", "sauce", "cheese", "pepperoni", "oliveOil", "seasoning", "garlicPowder"))
                .build();

        marinaraEntity = PizzaEntity.builder()
                .itemId(2L)
                .itemName("Marinara")
                .itemImageVersion("v1730296343")
                .itemPrice(10.50)
                .visibleInMenu(true)
                .itemCategory(itemCategoryEntityPizza)
                .sizes(List.of(pizzaSizeEntity1, pizzaSizeEntity2, pizzaSizeEntity3))
                .base("Thin Crust")
                .ingredients(List.of("dough", "sauce", "cheese", "pepperoni", "oliveOil", "seasoning", "garlicPowder"))
                .build();

        marinaraBase = Pizza.builder()
                .itemId(2L)
                .itemName("Marinara")
                .itemImageVersion("v1730296343")
                .itemPrice(10.50)
                .visibleInMenu(true)
                .itemCategory(itemCategoryBasePizza)
                .sizes(List.of(pizzaSizeBase1, pizzaSizeBase2, pizzaSizeBase3))
                .base("Thin Crust")
                .ingredients(List.of("dough", "sauce", "cheese", "pepperoni", "oliveOil", "seasoning", "garlicPowder"))
                .build();

        appetizerEntity = AppetizerEntity.builder()
                .itemId(3L)
                .itemName("Bruschetta")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .itemCategory(itemCategoryEntityAppetizer)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();

        appetizerBase = Appetizer.builder()
                .itemId(3L)
                .itemName("Bruschetta")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .itemCategory(itemCategoryBaseAppetizer)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();

        garlicBreadEntity = AppetizerEntity.builder()
                .itemId(4L)
                .itemName("Garlic bread")
                .itemImageVersion("v1730296343")
                .itemPrice(5.50)
                .itemCategory(itemCategoryEntityAppetizer)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();

        garlicBreadBase = Appetizer.builder()
                .itemId(4L)
                .itemName("Garlic bread")
                .itemImageVersion("v1730296343")
                .itemPrice(5.50)
                .itemCategory(itemCategoryBaseAppetizer)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();


        cartEntity = CartEntity.builder()
                .cartId(1L)
                .customerId(1L)
                .price(1.23)
                .selectedItemEntities(new ArrayList<>())
                .isActive(true)
                .build();

        cartEntityWithPizzaOnly = CartEntity.builder()
                //.cartId(2L)
                .customerId(1L)
                .price(12.50)
                .selectedItemEntities(new ArrayList<>())
                .isActive(true)
                .build();

        cartEntityWithItemDifferentFromPizzaOnly = CartEntity.builder()
                //.cartId(3L)
                .customerId(1L)
                .price(5.5)
                .selectedItemEntities(new ArrayList<>())
                .isActive(true)
                .build();

        selectedPizzaThatExistsInCartWithExistingSize = SelectedPizzaEntity.builder()
                .selectedItemId(1L)
                .itemOfReference(peperoniEntity)
                .amount(1)
                .cart(cartEntity)
                .itemCategory(itemCategoryEntityPizza)
                .comment("No commment")
                .statusOfPreparation(selectedItemStatusOfPreparationEntity)
                .itemName("Peperoni")
                .itemPrice(12.50)
                .sizes(pizzaSizeEntity1)
                .base("Thin Crust")
                .build();

        selectedPizzaThatDoesNotExistInCart = SelectedPizzaEntity.builder()
                .selectedItemId(1L)
                .itemOfReference(marinaraEntity)
                .amount(1)
                .cart(cartEntity)
                .itemCategory(itemCategoryEntityPizza)
                .comment("No commment")
                .statusOfPreparation(selectedItemStatusOfPreparationEntity)
                .itemName("Peperoni")
                .itemPrice(12.50)
                .sizes(pizzaSizeEntity1)
                .base("Thin Crust")
                .build();

        selectedPizzaThatExistsInCartWithNonExistingSize = SelectedPizzaEntity.builder()
                .selectedItemId(1L)
                .itemOfReference(peperoniEntity)
                .amount(1)
                .cart(cartEntity)
                .itemCategory(itemCategoryEntityPizza)
                .comment("No commment")
                .statusOfPreparation(selectedItemStatusOfPreparationEntity)
                .itemName("Peperoni")
                .itemPrice(12.50)
                .sizes(pizzaSizeEntity2)
                .base("Thin Crust")
                .build();

        selectedItemDifferentFromPizzaThatExistsInCart = SelectedItemDifferentFromPizzaEntity.builder()
                .selectedItemId(2L)
                .itemOfReference(appetizerEntity)
                .amount(1)
                .cart(cartEntity)
                .itemCategory(itemCategoryEntityPizza)
                .comment("No commment")
                .statusOfPreparation(selectedItemStatusOfPreparationEntity)
                .itemFromMenu(appetizerEntity)
                .build();

        selectedItemDifferentFromPizzaThatDoesNotExistInCart = SelectedItemDifferentFromPizzaEntity.builder()
                .selectedItemId(3L)
                .itemOfReference(garlicBreadEntity)
                .amount(1)
                .cart(cartEntity)
                .itemCategory(itemCategoryEntityPizza)
                .comment("No commment")
                .statusOfPreparation(selectedItemStatusOfPreparationEntity)
                .itemFromMenu(garlicBreadEntity)
                .build();

        cartEntity.getSelectedItemEntities().add(selectedPizzaThatExistsInCartWithExistingSize);
        cartEntity.getSelectedItemEntities().add(selectedItemDifferentFromPizzaThatExistsInCart);

        cartEntityWithPizzaOnly.getSelectedItemEntities().add(selectedPizzaThatDoesNotExistInCart);
        cartEntityWithItemDifferentFromPizzaOnly.getSelectedItemEntities().add(selectedItemDifferentFromPizzaThatDoesNotExistInCart);


        updatePizzaInCartRequest = AddPizzaToCartRequest.builder().customerId(1L).quantity(2).itemOfReferenceId(1L).comment("No comment").pizzaSize("small").build();
        addNewPizzaInCartRequest = AddPizzaToCartRequest.builder().customerId(1L).quantity(2).itemOfReferenceId(2L).comment("No comment").pizzaSize("small").build();
        addNewPizzaInCartRequestWithNonExistingSize = AddPizzaToCartRequest.builder().customerId(1L).quantity(2).itemOfReferenceId(2L).comment("No comment").pizzaSize("non existing size").build();
        simpleEmptyRequestWithCustomerIdOnly = AddItemToCartRequest.builder().customerId(1L).build();
        simpleEmptyRequestWithWrongCustomerId = AddItemToCartRequest.builder().customerId(2L).build();

        updateItemDifferentFromPizzaInCartRequest = AddItemDifferentFromPizzaToCartRequest.builder().customerId(1L).quantity(2).itemOfReferenceId(3L).comment("No comment").itemOfReferenceId(1L).build();
        addNewItemDifferentFromPizzaInCartRequest = AddItemDifferentFromPizzaToCartRequest.builder().customerId(1L).quantity(2).itemOfReferenceId(4L).comment("No comment").itemOfReferenceId(1L).build();

        getPizzaByIdResponse = GetItemByIdResponse.builder().foundItem(peperoniBase).build();
        getPizzaByIdResponse2 = GetItemByIdResponse.builder().foundItem(marinaraBase).build();
        getPizzaByIdResponse3 = GetItemByIdResponse.builder().foundItem(appetizerBase).build();
        getPizzaByIdResponse4 = GetItemByIdResponse.builder().foundItem(garlicBreadBase).build();

        accessToken = new AccessTokenImpl("test@test", 1L, Set.of("CUSTOMER"));


    }

    @Test
    void givenAlreadyExistingInCartPizza_whenAddingPizzaToCart_thenUpdatesPizzaQuantityInCart() {
        when(getItemById.getItemById(any(GetItemByIdRequest.class))).thenReturn(getPizzaByIdResponse);
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName("NOT STARTED")).thenReturn(Optional.ofNullable(selectedItemStatusOfPreparationEntity));
        when(selectedItemStatusOfPreparationConverter.convertEntityToNormal(selectedItemStatusOfPreparationEntity)).thenReturn(selectedItemStatusOfPreparationBase);
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(updatePizzaInCartRequest.getCustomerId())).thenReturn(Optional.ofNullable(cartEntity));
        when(pizzaSizeRepository.findByPizza_ItemId(getPizzaByIdResponse.getFoundItem().getItemId())).thenReturn(List.of(pizzaSizeEntity1, pizzaSizeEntity2, pizzaSizeEntity3));
        when(selectedItemConverter.convertNormalToEntity(any(SelectedItem.class))).thenReturn(selectedPizzaThatExistsInCartWithExistingSize);

        addItemToCartImpl.addItemToCart(updatePizzaInCartRequest, accessToken);

        verify(cartEntityRepository).save(cartEntity);
        verify(getItemById).getItemById(any(GetItemByIdRequest.class));
        verify(selectedItemStatusOfPreparationEntityRepository).findByStatusName("NOT STARTED");
        verify(selectedItemStatusOfPreparationConverter).convertEntityToNormal(selectedItemStatusOfPreparationEntity);
        verify(pizzaSizeRepository).findByPizza_ItemId(getPizzaByIdResponse.getFoundItem().getItemId());
        verify(selectedItemConverter).convertNormalToEntity(any(SelectedItem.class));
        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(1L);
    }


    @Test
    void givenNonExistingInCartPizza_whenAddingPizzaToCart_thenAddsPizzaToCart() {
        when(getItemById.getItemById(any(GetItemByIdRequest.class))).thenReturn(getPizzaByIdResponse2);
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName("NOT STARTED")).thenReturn(Optional.ofNullable(selectedItemStatusOfPreparationEntity));
        when(selectedItemStatusOfPreparationConverter.convertEntityToNormal(selectedItemStatusOfPreparationEntity)).thenReturn(selectedItemStatusOfPreparationBase);
        when(pizzaSizeRepository.findByPizza_ItemId(getPizzaByIdResponse2.getFoundItem().getItemId())).thenReturn(List.of(pizzaSizeEntity1, pizzaSizeEntity2, pizzaSizeEntity3));
        when(selectedItemConverter.convertNormalToEntity(any(SelectedItem.class))).thenReturn(selectedPizzaThatDoesNotExistInCart);
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(updatePizzaInCartRequest.getCustomerId())).thenReturn(Optional.ofNullable(cartEntity));

        addItemToCartImpl.addItemToCart(addNewPizzaInCartRequest, accessToken);

        verify(cartEntityRepository).save(cartEntity);
        verify(getItemById).getItemById(any(GetItemByIdRequest.class));
        verify(selectedItemStatusOfPreparationEntityRepository).findByStatusName("NOT STARTED");
        verify(selectedItemStatusOfPreparationConverter).convertEntityToNormal(selectedItemStatusOfPreparationEntity);
        verify(pizzaSizeRepository).findByPizza_ItemId(getPizzaByIdResponse2.getFoundItem().getItemId());
        verify(selectedItemConverter).convertNormalToEntity(any(SelectedItem.class));
        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(1L);

    }

    @Test
    void givenNonExistingInCartPizza_whenAddingPizzaWithNonExistingSizeSelectedToCart_thenThrowsAnErrorThatSizeDoesNotExist() {
        when(getItemById.getItemById(any(GetItemByIdRequest.class))).thenReturn(getPizzaByIdResponse2);
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName("NOT STARTED")).thenReturn(Optional.ofNullable(selectedItemStatusOfPreparationEntity));
        NotFound exception = assertThrows(NotFound.class, () -> {
            addItemToCartImpl.addItemToCart(addNewPizzaInCartRequestWithNonExistingSize, accessToken);
        });
        assertEquals("ITEM_NOT_FOUND", exception.getReason());


        verify(getItemById).getItemById(any(GetItemByIdRequest.class));
        verify(selectedItemStatusOfPreparationEntityRepository).findByStatusName("NOT STARTED");
    }

    @Test
    void givenExistingPizzaWithNonExistingSize_whenAddingPizzaToCart_thenAddsPizzaToCart() {
        when(getItemById.getItemById(any(GetItemByIdRequest.class))).thenReturn(getPizzaByIdResponse);
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName("NOT STARTED")).thenReturn(Optional.ofNullable(selectedItemStatusOfPreparationEntity));
        when(selectedItemStatusOfPreparationConverter.convertEntityToNormal(selectedItemStatusOfPreparationEntity)).thenReturn(selectedItemStatusOfPreparationBase);
        when(pizzaSizeRepository.findByPizza_ItemId(getPizzaByIdResponse.getFoundItem().getItemId())).thenReturn(List.of(pizzaSizeEntity1, pizzaSizeEntity2, pizzaSizeEntity3));
        when(selectedItemConverter.convertNormalToEntity(any(SelectedItem.class))).thenReturn(selectedPizzaThatExistsInCartWithNonExistingSize);
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(updatePizzaInCartRequest.getCustomerId())).thenReturn(Optional.ofNullable(cartEntity));

        addItemToCartImpl.addItemToCart(updatePizzaInCartRequest, accessToken);

        verify(cartEntityRepository).save(cartEntity);
        verify(getItemById).getItemById(any(GetItemByIdRequest.class));
        verify(selectedItemStatusOfPreparationEntityRepository).findByStatusName("NOT STARTED");
        verify(selectedItemStatusOfPreparationConverter).convertEntityToNormal(selectedItemStatusOfPreparationEntity);
        verify(pizzaSizeRepository).findByPizza_ItemId(getPizzaByIdResponse.getFoundItem().getItemId());
        verify(selectedItemConverter).convertNormalToEntity(any(SelectedItem.class));
        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(1L);

    }

    @Test
    void givenAlreadyExistingInCartItemDifferentFromPizza_whenAddingItemDifferentFromPizzaToCart_thenUpdatesItemQuantityInCart() {
        when(getItemById.getItemById(any(GetItemByIdRequest.class))).thenReturn(getPizzaByIdResponse3);
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName("NOT STARTED")).thenReturn(Optional.ofNullable(selectedItemStatusOfPreparationEntity));
        when(selectedItemStatusOfPreparationConverter.convertEntityToNormal(selectedItemStatusOfPreparationEntity)).thenReturn(selectedItemStatusOfPreparationBase);
        when(selectedItemConverter.convertNormalToEntity(any(SelectedItem.class))).thenReturn(selectedItemDifferentFromPizzaThatExistsInCart);
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(updatePizzaInCartRequest.getCustomerId())).thenReturn(Optional.ofNullable(cartEntity));

        addItemToCartImpl.addItemToCart(updateItemDifferentFromPizzaInCartRequest, accessToken);

        verify(cartEntityRepository).save(cartEntity);
        verify(getItemById).getItemById(any(GetItemByIdRequest.class));
        verify(selectedItemStatusOfPreparationEntityRepository).findByStatusName("NOT STARTED");
        verify(selectedItemStatusOfPreparationConverter).convertEntityToNormal(selectedItemStatusOfPreparationEntity);
        verify(selectedItemConverter).convertNormalToEntity(any(SelectedItem.class));
        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(1L);

    }

    @Test
    void givenNonExistingInCartItemDifferentFromPizza_whenAddingItemDifferentFromPizzaToCart_thenAddsItemInCart() {
        when(getItemById.getItemById(any(GetItemByIdRequest.class))).thenReturn(getPizzaByIdResponse4);
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName("NOT STARTED")).thenReturn(Optional.ofNullable(selectedItemStatusOfPreparationEntity));
        when(selectedItemStatusOfPreparationConverter.convertEntityToNormal(selectedItemStatusOfPreparationEntity)).thenReturn(selectedItemStatusOfPreparationBase);
        when(selectedItemConverter.convertNormalToEntity(any(SelectedItem.class))).thenReturn(selectedItemDifferentFromPizzaThatDoesNotExistInCart);
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(updatePizzaInCartRequest.getCustomerId())).thenReturn(Optional.ofNullable(cartEntity));

        addItemToCartImpl.addItemToCart(addNewItemDifferentFromPizzaInCartRequest, accessToken);

        verify(cartEntityRepository).save(cartEntity);
        verify(getItemById).getItemById(any(GetItemByIdRequest.class));
        verify(selectedItemStatusOfPreparationEntityRepository).findByStatusName("NOT STARTED");
        verify(selectedItemStatusOfPreparationConverter).convertEntityToNormal(selectedItemStatusOfPreparationEntity);
        verify(selectedItemConverter).convertNormalToEntity(any(SelectedItem.class));
        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(1L);

    }

    @Test
    void givenAPizzaToBeAddedToNonExistingCart_whenAddingPizzaToCart_thenCreateCartAndAddsPizzaToCart() {
        when(getItemById.getItemById(any(GetItemByIdRequest.class))).thenReturn(getPizzaByIdResponse2);
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName("NOT STARTED")).thenReturn(Optional.ofNullable(selectedItemStatusOfPreparationEntity));
        when(selectedItemStatusOfPreparationConverter.convertEntityToNormal(selectedItemStatusOfPreparationEntity)).thenReturn(selectedItemStatusOfPreparationBase);
        when(pizzaSizeRepository.findByPizza_ItemId(getPizzaByIdResponse2.getFoundItem().getItemId())).thenReturn(List.of(pizzaSizeEntity1, pizzaSizeEntity2, pizzaSizeEntity3));
        when(selectedItemConverter.convertNormalToEntity(any(SelectedItem.class))).thenReturn(selectedPizzaThatDoesNotExistInCart);
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(updatePizzaInCartRequest.getCustomerId())).thenReturn(Optional.empty());

        addItemToCartImpl.addItemToCart(addNewPizzaInCartRequest, accessToken);

        verify(cartEntityRepository).save(cartEntityWithPizzaOnly);
        verify(getItemById).getItemById(any(GetItemByIdRequest.class));
        verify(selectedItemStatusOfPreparationEntityRepository).findByStatusName("NOT STARTED");
        verify(selectedItemStatusOfPreparationConverter).convertEntityToNormal(selectedItemStatusOfPreparationEntity);
        verify(pizzaSizeRepository).findByPizza_ItemId(getPizzaByIdResponse2.getFoundItem().getItemId());
        verify(selectedItemConverter).convertNormalToEntity(any(SelectedItem.class));
        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(1L);

    }

    @Test
    void givenAnItemDifferentFromPizzaToBeAddedToNonExistingCart_whenAddingItemDifferentFromPizzaToCart_thenCreateCartAndAddsItemToCart() {
        when(getItemById.getItemById(any(GetItemByIdRequest.class))).thenReturn(getPizzaByIdResponse4);
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName("NOT STARTED")).thenReturn(Optional.ofNullable(selectedItemStatusOfPreparationEntity));
        when(selectedItemStatusOfPreparationConverter.convertEntityToNormal(selectedItemStatusOfPreparationEntity)).thenReturn(selectedItemStatusOfPreparationBase);
        when(selectedItemConverter.convertNormalToEntity(any(SelectedItem.class))).thenReturn(selectedItemDifferentFromPizzaThatDoesNotExistInCart);
        when(cartEntityRepository.findByCustomerIdAndIsActiveTrue(updatePizzaInCartRequest.getCustomerId())).thenReturn(Optional.empty());

        addItemToCartImpl.addItemToCart(addNewItemDifferentFromPizzaInCartRequest, accessToken);

        verify(cartEntityRepository).save(cartEntityWithItemDifferentFromPizzaOnly);
        verify(getItemById).getItemById(any(GetItemByIdRequest.class));
        verify(selectedItemStatusOfPreparationEntityRepository).findByStatusName("NOT STARTED");
        verify(selectedItemStatusOfPreparationConverter).convertEntityToNormal(selectedItemStatusOfPreparationEntity);
        verify(selectedItemConverter).convertNormalToEntity(any(SelectedItem.class));
        verify(cartEntityRepository).findByCustomerIdAndIsActiveTrue(1L);

    }

    @Test
    void givenAnEmptyRequestWithCustomerIdOnly_whenAddingItemToCart_thenThrowsAnErrorOfItemNotFound() {
        when(getItemById.getItemById(any(GetItemByIdRequest.class))).thenReturn(getPizzaByIdResponse);
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName("NOT STARTED")).thenReturn(Optional.ofNullable(selectedItemStatusOfPreparationEntity));


        NotFound exception = assertThrows(NotFound.class, () -> {
            addItemToCartImpl.addItemToCart(simpleEmptyRequestWithCustomerIdOnly, accessToken);
        });
        assertEquals("ITEM NOT FOUND", exception.getReason());

        verify(getItemById).getItemById(any(GetItemByIdRequest.class));
        verify(selectedItemStatusOfPreparationEntityRepository).findByStatusName("NOT STARTED");

    }

    @Test
    void givenAnRequestWithDifferentFromTheAuthenticatedUserId_whenAddingItemToCart_thenThrowsUnauthorizedUser() {

        AccessDenied exception = assertThrows(AccessDenied.class, () -> {
            addItemToCartImpl.addItemToCart(simpleEmptyRequestWithWrongCustomerId, accessToken);
        });
        assertEquals("Customers can only add items to their personal carts.", exception.getReason());

    }


}