package nl.fontys.s3.dinemasterbackend.business.item_use_cases.implementations;

import nl.fontys.s3.dinemasterbackend.business.converters.ItemConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.delete.DeleteItemRequest;
import nl.fontys.s3.dinemasterbackend.business.image_services.implementations.ImageService;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.exceptions.OperationNotPossible;
import nl.fontys.s3.dinemasterbackend.domain.classes.*;
import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.ItemEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.SelectedItemEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class DeleteItemByIdImplTest {
    // step 1: create mock instance
    @Mock
    ItemEntityRepository itemEntityRepository;
    @Mock
    SelectedItemEntityRepository selectedItemEntityRepository;
    @Mock
    ImageService imageService;
    @Mock
    ItemConverter itemConverterMock;

    @InjectMocks
    DeleteItemByIdImpl deleteItemByIdImpl;

    PizzaSizeEntity pizzaSizeEntity1;
    PizzaSizeEntity pizzaSizeEntity2;
    PizzaSizeEntity pizzaSizeEntity3;
    PizzaSize pizzaSizeBase1;
    PizzaSize pizzaSizeBase2;
    PizzaSize pizzaSizeBase3;
    ItemCategoryEntity itemCategoryEntityPizza;
    ItemCategory itemCategoryBasePizza;
    PizzaEntity peperoniEntity;
    Pizza peperoniBase;
    PizzaEntity requestedPizzaEntity;

    AppetizerEntity requestedAppetizerEntity;
    AppetizerEntity appetizerEntity;
    Appetizer appetizerBase;
    ItemCategoryEntity itemCategoryEntityAppetizer;
    ItemCategory itemCategoryBaseAppetizer;

    DeleteItemRequest deleteItemRequest;
    List<ItemEntity> allItemEntities;
    SelectedItemStatusOfPreparationEntity status;
    SelectedItemEntity selectedItem;
    List<SelectedItemEntity> selectedItemEntities;

    @BeforeEach
    void setUp() {
        pizzaSizeEntity1 = PizzaSizeEntity.builder().size("small").additionalPrice(1.0).build();
        pizzaSizeEntity2 = PizzaSizeEntity.builder().size("large").additionalPrice(2.0).build();
        pizzaSizeEntity3 = PizzaSizeEntity.builder().size("jumbo").additionalPrice(3.0).build();

        pizzaSizeBase1 = PizzaSize.builder().size("small").additionalPrice(1.0).build();
        pizzaSizeBase2 = PizzaSize.builder().size("large").additionalPrice(2.0).build();
        pizzaSizeBase3 = PizzaSize.builder().size("jumbo").additionalPrice(3.0).build();

        itemCategoryEntityPizza = ItemCategoryEntity.builder().categoryId(1L).categoryName("PIZZA").build();
        itemCategoryBasePizza = ItemCategory.builder().categoryId(1L).categoryName("PIZZA").build();

        itemCategoryEntityAppetizer = ItemCategoryEntity.builder().categoryId(1L).categoryName("APPETIZER").build();
        itemCategoryBaseAppetizer = ItemCategory.builder().categoryId(1L).categoryName("APPETIZER").build();


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

        requestedPizzaEntity = PizzaEntity.builder()
                .itemName("Peperoni")
                .itemImageVersion("v1730296343")
                .itemPrice(12.50)
                .visibleInMenu(true)
                .itemCategory(itemCategoryEntityPizza)
                .sizes(List.of(pizzaSizeEntity1, pizzaSizeEntity2, pizzaSizeEntity3))
                .base("Thin Crust")
                .ingredients(List.of("dough", "sauce", "cheese", "pepperoni", "oliveOil", "seasoning", "garlicPowder"))
                .build();

        requestedAppetizerEntity = AppetizerEntity.builder()
                .itemId(2L)
                .itemName("Bruschetta")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .itemCategory(itemCategoryEntityAppetizer)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();

        appetizerEntity = AppetizerEntity.builder()
                .itemId(2L)
                .itemName("Bruschetta")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .itemCategory(itemCategoryEntityAppetizer)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();

        appetizerBase = Appetizer.builder()
                .itemId(2L)
                .itemName("Bruschetta")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .itemCategory(itemCategoryBaseAppetizer)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();

        deleteItemRequest = DeleteItemRequest.builder().itemId(1L).build();
        allItemEntities = List.of(peperoniEntity, appetizerEntity);

        status = SelectedItemStatusOfPreparationEntity.builder().id(1L).statusName("PREPARING").build();
        selectedItem = SelectedItemEntity.builder()
                .selectedItemId(3L)
                .itemOfReference(peperoniEntity)
                .amount(2)
                .cart(CartEntity.builder().build())
                .itemCategory(itemCategoryEntityPizza)
                .comment("No peppers")
                .statusOfPreparation(status)
                .build();
        selectedItemEntities = List.of(selectedItem);

    }

    @Test
    void givenItemId_whenDeletingItemById_theDeletesItem() {
        when(itemEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(peperoniEntity));
        when(selectedItemEntityRepository.findSelectedItemsInActiveCarts(peperoniEntity)).thenReturn(List.of());

        deleteItemByIdImpl.deleteItemById(deleteItemRequest);
        //deleteItemByIdImpl.de

        verify(itemEntityRepository).findById(1L);
        verify(selectedItemEntityRepository).findSelectedItemsInActiveCarts(peperoniEntity);
        verify(itemEntityRepository).deleteById(peperoniEntity.getItemId());
        verify(imageService).deleteImage(peperoniEntity.getItemImageUrl());

    }

    @Test
    void givenItemId_whenDeletingItemById_thenThrowsAnErrorWhenItemIsNotFoundInDatabase() {

        when(itemEntityRepository.findById(1L)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> {
            deleteItemByIdImpl.deleteItemById(deleteItemRequest);
        });

        assertEquals("Item with ID " + deleteItemRequest.getItemId() + " from " + deleteItemRequest.getCategory() + " not found", exception.getReason());

        verify(itemEntityRepository).findById(1L);

    }

    @Test
    void givenItemId_whenDeletingItemById_thenThrowsAnErrorWhenItemIsFoundToBePartOfActiveOrders() {

        when(itemEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(peperoniEntity));
        when(selectedItemEntityRepository.findSelectedItemsInActiveCarts(peperoniEntity)).thenReturn(List.of(SelectedItemEntity.builder().build()));

        OperationNotPossible exception = assertThrows(OperationNotPossible.class, () -> {
            deleteItemByIdImpl.deleteItemById(deleteItemRequest);
        });

        assertEquals("Operation is not possible at the moment. There are active orders containing the selected item", exception.getReason());

        verify(itemEntityRepository).findById(1L);

    }
}