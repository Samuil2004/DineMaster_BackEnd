package nl.fontys.s3.dinemasterbackend.business.item_use_cases.implementations;

import nl.fontys.s3.dinemasterpro.business.converters.ItemConverter;
import nl.fontys.s3.dinemasterpro.business.dtos.create.items.CreateAppetizerRequest;
import nl.fontys.s3.dinemasterpro.business.dtos.create.items.CreateItemRequest;
import nl.fontys.s3.dinemasterpro.business.dtos.create.items.CreatePizzaRequest;
import nl.fontys.s3.dinemasterpro.business.dtos.get.items.GetItemByIdRequest;
import nl.fontys.s3.dinemasterpro.business.dtos.get.items.GetItemByIdResponse;
import nl.fontys.s3.dinemasterpro.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterpro.domain.classes.*;
import nl.fontys.s3.dinemasterpro.persistence.entity.*;
import nl.fontys.s3.dinemasterpro.persistence.repositories.ItemEntityRepository;
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
class GetItemByIdImplTest {
    @Mock
    ItemEntityRepository itemEntityRepository;
    @Mock
    ItemConverter itemConverterMock;
    @InjectMocks
    GetItemByIdImpl getItemById;

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
    CreateItemRequest createPizzaRequest;
    PizzaEntity requestedPizzaEntity;

    AppetizerEntity requestedAppetizerEntity;
    AppetizerEntity appetizerEntity;
    CreateAppetizerRequest createAppetizerRequest;
    Appetizer appetizerBase;
    ItemCategoryEntity itemCategoryEntityAppetizer;
    ItemCategory itemCategoryBaseAppetizer;

    GetItemByIdRequest getItemByIdRequest;


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

        createPizzaRequest = CreatePizzaRequest.builder()
                .itemName("Peperoni")
                //.itemImageURL("v1730296343")
                .itemPrice(12.50)
                .visibleInMenu(true)
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


        createAppetizerRequest = CreateAppetizerRequest.builder()
                .itemName("Bruschetta")
                //.itemImageURL("v1730296343")
                .itemPrice(7.50)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();

        requestedAppetizerEntity = AppetizerEntity.builder()
                .itemId(1L)
                .itemName("Bruschetta")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .itemCategory(itemCategoryEntityAppetizer)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();

        appetizerEntity = AppetizerEntity.builder()
                .itemId(1L)
                .itemName("Bruschetta")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .itemCategory(itemCategoryEntityAppetizer)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();

        appetizerBase = Appetizer.builder()
                .itemId(1L)
                .itemName("Bruschetta")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .itemCategory(itemCategoryBaseAppetizer)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();

        getItemByIdRequest = GetItemByIdRequest.builder().itemId(1L).build();

    }

    @Test
    void givenItemId_whenGettingItemById_thenReturnsFoundItem() {
        when(itemEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(peperoniEntity));
        when(itemConverterMock.convertEntityToNormal(peperoniEntity)).thenReturn(peperoniBase);

        GetItemByIdResponse foundItem = getItemById.getItemById(getItemByIdRequest);
        assertEquals(peperoniBase, foundItem.getFoundItem());

        verify(itemEntityRepository).findById(1L);
        verify(itemConverterMock).convertEntityToNormal(peperoniEntity);

    }

    @Test
    void givenItemId_whenItemIsNotFound_thenThrowsItemNotFoundError() {

        when(itemEntityRepository.findById(1L)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> {
            getItemById.getItemById(getItemByIdRequest);
        });

        assertEquals("ITEM_NOT_FOUND", exception.getReason());

        verify(itemEntityRepository).findById(1L);

    }
}