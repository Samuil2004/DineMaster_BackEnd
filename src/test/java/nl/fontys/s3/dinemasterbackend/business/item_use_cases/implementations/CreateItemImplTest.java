package nl.fontys.s3.dinemasterbackend.business.item_use_cases.implementations;

import nl.fontys.s3.dinemasterpro.business.converters.ItemCategoryConverter;
import nl.fontys.s3.dinemasterpro.business.converters.ItemConverter;
import nl.fontys.s3.dinemasterpro.business.dtos.create.items.*;
import nl.fontys.s3.dinemasterpro.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterpro.business.exceptions.OperationNotPossible;
import nl.fontys.s3.dinemasterpro.domain.classes.*;
import nl.fontys.s3.dinemasterpro.persistence.entity.*;
import nl.fontys.s3.dinemasterpro.persistence.repositories.ItemCategoryRepository;
import nl.fontys.s3.dinemasterpro.persistence.repositories.ItemEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CreateItemImplTest {

    @Mock
    ItemEntityRepository itemEntityRepository;
    @Mock
    ItemCategoryRepository itemCategoryRepository;
    @Mock
    ItemConverter itemConverterMock;
    @Mock
    ItemCategoryConverter itemCategoryConverterMock;
    @InjectMocks
    CreateItemImpl createItemImpl;

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

    BeverageEntity requestedBeverageEntity;
    BeverageEntity beverageEntity;
    CreateBeverageRequest createBeverageRequest;
    Beverage beverageBase;
    ItemCategoryEntity itemCategoryEntityBeverage;
    ItemCategory itemCategoryBaseBeverage;

    ItemCategoryEntity itemCategoryEntityBurger;
    ItemCategory itemCategoryBaseBurger;
    BurgerEntity burgerEntity;
    Burger burgerBase;
    CreateItemRequest createBurgerRequest;
    BurgerEntity requestedBurgerEntity;

    ItemCategoryEntity itemCategoryEntityGrill;
    ItemCategory itemCategoryBaseGrill;
    GrillEntity grillEntity;
    Grill grillBase;
    CreateItemRequest createGrillRequest;
    GrillEntity requestedGrillEntity;

    ItemCategoryEntity itemCategoryEntityPasta;
    ItemCategory itemCategoryBasePasta;
    PastaEntity pastaEntity;
    Pasta pastaBase;
    CreateItemRequest createPastaRequest;
    PastaEntity requestedPastaEntity;

    ItemCategoryEntity itemCategoryEntitySalad;
    ItemCategory itemCategoryBaseSalad;
    SaladEntity saladEntity;
    Salad saladBase;
    CreateItemRequest createSaladRequest;
    SaladEntity requestedSaladEntity;

    ItemCategoryEntity itemCategoryEntitySoup;
    ItemCategory itemCategoryBaseSoup;
    SoupEntity soupEntity;
    Soup soupBase;
    CreateItemRequest createSoupRequest;
    SoupEntity requestedSoupEntity;

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

        itemCategoryEntityBeverage = ItemCategoryEntity.builder().categoryId(1L).categoryName("BEVERAGE").build();
        itemCategoryBaseBeverage = ItemCategory.builder().categoryId(1L).categoryName("BEVERAGE").build();



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

        requestedPizzaEntity= PizzaEntity.builder()
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




        createBeverageRequest = CreateBeverageRequest.builder()
                .itemName("Cola")
                //.itemImageURL("v1730296343")
                .itemPrice(2.50)
                .visibleInMenu(true)
                .size(0.33)
                .build();

        requestedBeverageEntity = BeverageEntity.builder()
                .itemId(1L)
                .itemName("Cola")
                .itemImageVersion("v1730296343")
                .itemPrice(2.50)
                .visibleInMenu(true)
                .itemCategory(itemCategoryEntityBeverage)
                .size(0.33)
                .build();

        beverageEntity = BeverageEntity.builder()
                .itemId(1L)
                .itemName("Cola")
                .itemImageVersion("v1730296343")
                .itemPrice(2.50)
                .visibleInMenu(true)
                .itemCategory(itemCategoryEntityBeverage)
                .size(0.33)
                .build();

        beverageBase = Beverage.builder()
                .itemId(1L)
                .itemName("Cola")
                .itemImageVersion("v1730296343")
                .itemPrice(2.50)
                .visibleInMenu(true)
                .itemCategory(itemCategoryBaseBeverage)
                .size(0.33)
                .build();


        itemCategoryEntityBurger = ItemCategoryEntity.builder().categoryId(2L).categoryName("BURGER").build();
        itemCategoryBaseBurger = ItemCategory.builder().categoryId(2L).categoryName("BURGER").build();

        burgerEntity = BurgerEntity.builder()
                .itemId(1L)
                .itemName("Classic Burger")
                .itemImageVersion("v1730296343")
                .itemPrice(8.50)
                .visibleInMenu(true)
                .itemCategory(itemCategoryEntityBurger)
                .weight(250)
                .ingredients(List.of("beef patty", "lettuce", "tomato", "cheese", "bun"))
                .build();

        burgerBase = Burger.builder()
                .itemId(1L)
                .itemName("Classic Burger")
                .itemImageVersion("v1730296343")
                .itemPrice(8.50)
                .visibleInMenu(true)
                .itemCategory(itemCategoryBaseBurger)
                .weight(250)
                .ingredients(List.of("beef patty", "lettuce", "tomato", "cheese", "bun"))
                .build();

        createBurgerRequest = CreateBurgerRequest.builder()
                .itemName("Classic Burger")
                //.itemImageURL("v1730296343")
                .itemPrice(8.50)
                .visibleInMenu(true)
                .weight(250)
                .ingredients(List.of("beef patty", "lettuce", "tomato", "cheese", "bun"))
                .build();

        requestedBurgerEntity = BurgerEntity.builder()
                .itemName("Classic Burger")
                .itemImageVersion("v1730296343")
                .itemPrice(8.50)
                .visibleInMenu(true)
                .itemCategory(itemCategoryEntityBurger)
                .weight(250)
                .ingredients(List.of("beef patty", "lettuce", "tomato", "cheese", "bun"))
                .build();

        itemCategoryEntityGrill = ItemCategoryEntity.builder().categoryId(3L).categoryName("GRILL").build();
        itemCategoryBaseGrill = ItemCategory.builder().categoryId(3L).categoryName("GRILL").build();

        grillEntity = GrillEntity.builder()
                .itemId(1L)
                .itemName("BBQ Ribs")
                .itemImageVersion("v1730296344")
                .itemPrice(15.99)
                .visibleInMenu(true)
                .itemCategory(itemCategoryEntityGrill)
                .weight(500)
                .ingredients(List.of("pork ribs", "BBQ sauce"))
                .build();

        grillBase = Grill.builder()
                .itemId(1L)
                .itemName("BBQ Ribs")
                .itemImageVersion("v1730296344")
                .itemPrice(15.99)
                .visibleInMenu(true)
                .itemCategory(itemCategoryBaseGrill)
                .weight(500)
                .ingredients(List.of("pork ribs", "BBQ sauce"))
                .build();

        createGrillRequest = CreateGrillRequest.builder()
                .itemName("BBQ Ribs")
                //.itemImageURL("v1730296344")
                .itemPrice(15.99)
                .visibleInMenu(true)
                .weight(500)
                .ingredients(List.of("pork ribs", "BBQ sauce"))
                .build();

        requestedGrillEntity = GrillEntity.builder()
                .itemName("BBQ Ribs")
                .itemImageVersion("v1730296344")
                .itemPrice(15.99)
                .visibleInMenu(true)
                .itemCategory(itemCategoryEntityGrill)
                .weight(500)
                .ingredients(List.of("pork ribs", "BBQ sauce"))
                .build();

        itemCategoryEntityPasta = ItemCategoryEntity.builder().categoryId(4L).categoryName("PASTA").build();
        itemCategoryBasePasta = ItemCategory.builder().categoryId(4L).categoryName("PASTA").build();

        pastaEntity = PastaEntity.builder()
                .itemId(1L)
                .itemName("Spaghetti Carbonara")
                .itemImageVersion("v1730296344")
                .itemPrice(12.99)
                .visibleInMenu(true)
                .itemCategory(itemCategoryEntityPasta)
                .pastaType("Spaghetti")
                .weight(400)
                .ingredients(List.of("spaghetti", "egg", "bacon", "parmesan"))
                .build();

        pastaBase = Pasta.builder()
                .itemId(1L)
                .itemName("Spaghetti Carbonara")
                .itemImageVersion("v1730296344")
                .itemPrice(12.99)
                .visibleInMenu(true)
                .itemCategory(itemCategoryBasePasta)
                .pastaType("Spaghetti")
                .weight(400)
                .ingredients(List.of("spaghetti", "egg", "bacon", "parmesan"))
                .build();

        createPastaRequest = CreatePastaRequest.builder()
                .itemName("Spaghetti Carbonara")
                //.itemImageURL("v1730296344")
                .itemPrice(12.99)
                .visibleInMenu(true)
                .pastaType("Spaghetti")
                .weight(400)
                .ingredients(List.of("spaghetti", "egg", "bacon", "parmesan"))
                .build();

        requestedPastaEntity = PastaEntity.builder()
                .itemName("Spaghetti Carbonara")
                .itemImageVersion("v1730296344")
                .itemPrice(12.99)
                .visibleInMenu(true)
                .itemCategory(itemCategoryEntityPasta)
                .pastaType("Spaghetti")
                .weight(400)
                .ingredients(List.of("spaghetti", "egg", "bacon", "parmesan"))
                .build();

        itemCategoryEntitySalad = ItemCategoryEntity.builder().categoryId(5L).categoryName("SALAD").build();
        itemCategoryBaseSalad = ItemCategory.builder().categoryId(5L).categoryName("SALAD").build();

        saladEntity = SaladEntity.builder()
                .itemId(1L)
                .itemName("Caesar Salad")
                .itemImageVersion("v1730296345")
                .itemPrice(8.99)
                .visibleInMenu(true)
                .itemCategory(itemCategoryEntitySalad)
                .weight(300)
                .ingredients(List.of("lettuce", "croutons", "parmesan", "caesar dressing"))
                .build();

        saladBase = Salad.builder()
                .itemId(1L)
                .itemName("Caesar Salad")
                .itemImageVersion("v1730296345")
                .itemPrice(8.99)
                .visibleInMenu(true)
                .itemCategory(itemCategoryBaseSalad)
                .weight(300)
                .ingredients(List.of("lettuce", "croutons", "parmesan", "caesar dressing"))
                .build();

        createSaladRequest = CreateSaladRequest.builder()
                .itemName("Caesar Salad")
                //.itemImageURL("v1730296345")
                .itemPrice(8.99)
                .visibleInMenu(true)
                .weight(300)
                .ingredients(List.of("lettuce", "croutons", "parmesan", "caesar dressing"))
                .build();

        requestedSaladEntity = SaladEntity.builder()
                .itemName("Caesar Salad")
                .itemImageVersion("v1730296345")
                .itemPrice(8.99)
                .visibleInMenu(true)
                .itemCategory(itemCategoryEntitySalad)
                .weight(300)
                .ingredients(List.of("lettuce", "croutons", "parmesan", "caesar dressing"))
                .build();

        itemCategoryEntitySoup = ItemCategoryEntity.builder().categoryId(6L).categoryName("SOUP").build();
        itemCategoryBaseSoup = ItemCategory.builder().categoryId(6L).categoryName("SOUP").build();

        soupEntity = SoupEntity.builder()
                .itemId(1L)
                .itemName("Tomato Soup")
                .itemImageVersion("v2034849567")
                .itemPrice(5.99)
                .visibleInMenu(true)
                .itemCategory(itemCategoryEntitySoup)
                .isVegetarian(true)
                .ingredients(List.of("tomatoes", "basil", "garlic"))
                .build();

        soupBase = Soup.builder()
                .itemId(1L)
                .itemName("Tomato Soup")
                .itemImageVersion("v2034849567")
                .itemPrice(5.99)
                .visibleInMenu(true)
                .itemCategory(itemCategoryBaseSoup)
                .isVegetarian(true)
                .ingredients(List.of("tomatoes", "basil", "garlic"))
                .build();

        createSoupRequest = CreateSoupRequest.builder()
                .itemName("Tomato Soup")
                //.itemImageURL("v2034849567")
                .itemPrice(5.99)
                .visibleInMenu(true)
                .isVegetarian(true)
                .ingredients(List.of("tomatoes", "basil", "garlic"))
                .build();

        requestedSoupEntity = SoupEntity.builder()
                .itemName("Tomato Soup")
                .itemImageVersion("v2034849567")
                .itemPrice(5.99)
                .visibleInMenu(true)
                .itemCategory(itemCategoryEntitySoup)
                .isVegetarian(true)
                .ingredients(List.of("tomatoes", "basil", "garlic"))
                .build();
    }

    @Test
    void givenPizzaDetails_whenCreatingPizza_theReturnsTheIdOfTheNewItem() {

        when(itemEntityRepository.existsByItemName(createPizzaRequest.getItemName())).thenReturn(false);

        when(itemCategoryRepository.findByCategoryName("PIZZA")).thenReturn(Optional.ofNullable(itemCategoryEntityPizza));
        when(itemCategoryConverterMock.convertEntityToNormal(itemCategoryEntityPizza)).thenReturn(itemCategoryBasePizza);

        when(itemConverterMock.convertNormalToEntity(Mockito.any(Pizza.class))).thenReturn(requestedPizzaEntity);
        when(itemEntityRepository.save(requestedPizzaEntity)).thenReturn(peperoniEntity);
        when(itemConverterMock.convertEntityToNormal(peperoniEntity)).thenReturn(peperoniBase);


        CreateItemResponse response = createItemImpl.createItem(createPizzaRequest);


        assertEquals(1L,response.getItemId());
        verify(itemEntityRepository).existsByItemName(createPizzaRequest.getItemName());
        verify(itemCategoryRepository).findByCategoryName("PIZZA");
        verify(itemCategoryConverterMock).convertEntityToNormal(itemCategoryEntityPizza);
        verify(itemConverterMock).convertNormalToEntity(Mockito.any(Pizza.class));
        verify(itemEntityRepository).save(requestedPizzaEntity);
        verify(itemConverterMock).convertEntityToNormal(peperoniEntity);
    }

    @Test
    void givenAppetizerDetails_whenCreatingAppetizer_thenReturnsTheIdOfTheNewItem() {

        when(itemEntityRepository.existsByItemName(createAppetizerRequest.getItemName())).thenReturn(false);

        when(itemCategoryRepository.findByCategoryName("APPETIZER")).thenReturn(Optional.of(itemCategoryEntityAppetizer));
        when(itemCategoryConverterMock.convertEntityToNormal(itemCategoryEntityAppetizer)).thenReturn(itemCategoryBaseAppetizer);

        when(itemConverterMock.convertNormalToEntity(Mockito.any(Appetizer.class))).thenReturn(requestedAppetizerEntity);
        when(itemEntityRepository.save(requestedAppetizerEntity)).thenReturn(appetizerEntity);
        when(itemConverterMock.convertEntityToNormal(appetizerEntity)).thenReturn(appetizerBase);

        CreateItemResponse response = createItemImpl.createItem(createAppetizerRequest);

        assertEquals(1L,response.getItemId());
        verify(itemEntityRepository).existsByItemName(createAppetizerRequest.getItemName());
        verify(itemCategoryRepository).findByCategoryName("APPETIZER");
        verify(itemCategoryConverterMock).convertEntityToNormal(itemCategoryEntityAppetizer);
        verify(itemConverterMock).convertNormalToEntity(Mockito.any(Appetizer.class));
        verify(itemEntityRepository).save(requestedAppetizerEntity);
        verify(itemConverterMock).convertEntityToNormal(appetizerEntity);
    }
    @Test
    void givenPastaDetails_whenCreatingPasta_thenReturnsTheIdOfTheNewItem() {

        when(itemEntityRepository.existsByItemName(createPastaRequest.getItemName())).thenReturn(false);
        when(itemCategoryRepository.findByCategoryName("PASTA")).thenReturn(Optional.of(itemCategoryEntityPasta));
        when(itemCategoryConverterMock.convertEntityToNormal(itemCategoryEntityPasta)).thenReturn(itemCategoryBasePasta);

        when(itemConverterMock.convertNormalToEntity(Mockito.any(Pasta.class))).thenReturn(requestedPastaEntity);
        when(itemEntityRepository.save(requestedPastaEntity)).thenReturn(pastaEntity);
        when(itemConverterMock.convertEntityToNormal(pastaEntity)).thenReturn(pastaBase);

        CreateItemResponse response = createItemImpl.createItem(createPastaRequest);

        assertEquals(1L,response.getItemId());
        verify(itemEntityRepository).existsByItemName(createPastaRequest.getItemName());
        verify(itemCategoryRepository).findByCategoryName("PASTA");
        verify(itemCategoryConverterMock).convertEntityToNormal(itemCategoryEntityPasta);
        verify(itemConverterMock).convertNormalToEntity(Mockito.any(Pasta.class));
        verify(itemEntityRepository).save(requestedPastaEntity);
        verify(itemConverterMock).convertEntityToNormal(pastaEntity);
    }
    @Test
    void givenBeverageDetails_whenCreatingBeverage_theReturnsTheIdOfTheNewItem() {

        when(itemEntityRepository.existsByItemName(createBeverageRequest.getItemName())).thenReturn(false);

        when(itemCategoryRepository.findByCategoryName("BEVERAGE")).thenReturn(Optional.of(itemCategoryEntityBeverage));
        when(itemCategoryConverterMock.convertEntityToNormal(itemCategoryEntityBeverage)).thenReturn(itemCategoryBaseBeverage);

        when(itemConverterMock.convertNormalToEntity(Mockito.any(Beverage.class))).thenReturn(requestedBeverageEntity);
        when(itemEntityRepository.save(requestedBeverageEntity)).thenReturn(beverageEntity);
        when(itemConverterMock.convertEntityToNormal(beverageEntity)).thenReturn(beverageBase);

        CreateItemResponse response = createItemImpl.createItem(createBeverageRequest);

        assertEquals(1L,response.getItemId());
        verify(itemEntityRepository).existsByItemName(createBeverageRequest.getItemName());
        verify(itemCategoryRepository).findByCategoryName("BEVERAGE");
        verify(itemCategoryConverterMock).convertEntityToNormal(itemCategoryEntityBeverage);
        verify(itemConverterMock).convertNormalToEntity(Mockito.any(Beverage.class));
        verify(itemEntityRepository).save(requestedBeverageEntity);
        verify(itemConverterMock).convertEntityToNormal(beverageEntity);
    }

    @Test
    void givenBurgerDetails_whenCreatingBurger_thenReturnsTheIdOfTheNewItem() {

        when(itemEntityRepository.existsByItemName(createBurgerRequest.getItemName())).thenReturn(false);
        when(itemCategoryRepository.findByCategoryName("BURGER")).thenReturn(Optional.of(itemCategoryEntityBurger));
        when(itemCategoryConverterMock.convertEntityToNormal(itemCategoryEntityBurger)).thenReturn(itemCategoryBaseBurger);

        when(itemConverterMock.convertNormalToEntity(Mockito.any(Burger.class))).thenReturn(requestedBurgerEntity);
        when(itemEntityRepository.save(requestedBurgerEntity)).thenReturn(burgerEntity);
        when(itemConverterMock.convertEntityToNormal(burgerEntity)).thenReturn(burgerBase);

        CreateItemResponse response = createItemImpl.createItem(createBurgerRequest);

        assertEquals(1L,response.getItemId());
        verify(itemEntityRepository).existsByItemName(createBurgerRequest.getItemName());
        verify(itemCategoryRepository).findByCategoryName("BURGER");
        verify(itemCategoryConverterMock).convertEntityToNormal(itemCategoryEntityBurger);
        verify(itemConverterMock).convertNormalToEntity(Mockito.any(Burger.class));
        verify(itemEntityRepository).save(requestedBurgerEntity);
        verify(itemConverterMock).convertEntityToNormal(burgerEntity);
    }

    @Test
    void givenGrillDetails_whenCreatingGrill_thenReturnsTheIdOfTheNewItem() {

        when(itemEntityRepository.existsByItemName(createGrillRequest.getItemName())).thenReturn(false);
        when(itemCategoryRepository.findByCategoryName("GRILL")).thenReturn(Optional.of(itemCategoryEntityGrill));
        when(itemCategoryConverterMock.convertEntityToNormal(itemCategoryEntityGrill)).thenReturn(itemCategoryBaseGrill);

        when(itemConverterMock.convertNormalToEntity(Mockito.any(Grill.class))).thenReturn(requestedGrillEntity);
        when(itemEntityRepository.save(requestedGrillEntity)).thenReturn(grillEntity);
        when(itemConverterMock.convertEntityToNormal(grillEntity)).thenReturn(grillBase);

        CreateItemResponse response = createItemImpl.createItem(createGrillRequest);

        assertEquals(1L,response.getItemId());
        verify(itemEntityRepository).existsByItemName(createGrillRequest.getItemName());
        verify(itemCategoryRepository).findByCategoryName("GRILL");
        verify(itemCategoryConverterMock).convertEntityToNormal(itemCategoryEntityGrill);
        verify(itemConverterMock).convertNormalToEntity(Mockito.any(Grill.class));
        verify(itemEntityRepository).save(requestedGrillEntity);
        verify(itemConverterMock).convertEntityToNormal(grillEntity);
    }

    @Test
    void givenSaladDetails_whenCreatingSalad_thenReturnsTheIdOfTheNewItem() {

        when(itemEntityRepository.existsByItemName(createSaladRequest.getItemName())).thenReturn(false);
        when(itemCategoryRepository.findByCategoryName("SALAD")).thenReturn(Optional.of(itemCategoryEntitySalad));
        when(itemCategoryConverterMock.convertEntityToNormal(itemCategoryEntitySalad)).thenReturn(itemCategoryBaseSalad);

        when(itemConverterMock.convertNormalToEntity(Mockito.any(Salad.class))).thenReturn(requestedSaladEntity);
        when(itemEntityRepository.save(requestedSaladEntity)).thenReturn(saladEntity);
        when(itemConverterMock.convertEntityToNormal(saladEntity)).thenReturn(saladBase);

        CreateItemResponse response = createItemImpl.createItem(createSaladRequest);

        assertEquals(1L,response.getItemId());
        verify(itemEntityRepository).existsByItemName(createSaladRequest.getItemName());
        verify(itemCategoryRepository).findByCategoryName("SALAD");
        verify(itemCategoryConverterMock).convertEntityToNormal(itemCategoryEntitySalad);
        verify(itemConverterMock).convertNormalToEntity(Mockito.any(Salad.class));
        verify(itemEntityRepository).save(requestedSaladEntity);
        verify(itemConverterMock).convertEntityToNormal(saladEntity);
    }

    @Test
    void givenSoupDetails_whenCreatingSoup_thenReturnsTheIdOfTheNewItem() {

        when(itemEntityRepository.existsByItemName(createSoupRequest.getItemName())).thenReturn(false);
        when(itemCategoryRepository.findByCategoryName("SOUP")).thenReturn(Optional.of(itemCategoryEntitySoup));
        when(itemCategoryConverterMock.convertEntityToNormal(itemCategoryEntitySoup)).thenReturn(itemCategoryBaseSoup);

        when(itemConverterMock.convertNormalToEntity(Mockito.any(Soup.class))).thenReturn(requestedSoupEntity);
        when(itemEntityRepository.save(requestedSoupEntity)).thenReturn(soupEntity);
        when(itemConverterMock.convertEntityToNormal(soupEntity)).thenReturn(soupBase);

        CreateItemResponse response = createItemImpl.createItem(createSoupRequest);

        assertEquals(1L,response.getItemId());
        verify(itemEntityRepository).existsByItemName(createSoupRequest.getItemName());
        verify(itemCategoryRepository).findByCategoryName("SOUP");
        verify(itemCategoryConverterMock).convertEntityToNormal(itemCategoryEntitySoup);
        verify(itemConverterMock).convertNormalToEntity(Mockito.any(Soup.class));
        verify(itemEntityRepository).save(requestedSoupEntity);
        verify(itemConverterMock).convertEntityToNormal(soupEntity);
    }

    @Test
    void givenPizzaDetails_ThrowsItemError_whenItemWithPassedNameAlreadyExists() {

        when(itemEntityRepository.existsByItemName(createPizzaRequest.getItemName())).thenReturn(true);

        OperationNotPossible exception = assertThrows(OperationNotPossible.class, () -> {
            createItemImpl.createItem(createPizzaRequest);
        });
        assertEquals("Item with provided name already exists.", exception.getReason());
    }

    @Test
    void givenPizzaDetails_ThrowsCategoryNotFound_whenNonExistingCategoryForItemIsPassed() {

        when(itemEntityRepository.existsByItemName(createPizzaRequest.getItemName())).thenReturn(false);
        when(itemCategoryRepository.findByCategoryName("PIZZA")).thenReturn(Optional.ofNullable(null));

        NotFound exception = assertThrows(NotFound.class, () -> {
            createItemImpl.createItem(createPizzaRequest);
        });
        assertEquals("ITEM_CATEGORY_NOT_FOUND", exception.getReason());
    }

    @Test
    void givenPizzaDetails_ThrowsCategoryNotFound_whenNonExistingCategoryIsPassed() {
        CreateItemRequest request = new CreateItemRequest();
        when(itemEntityRepository.existsByItemName(request.getItemName())).thenReturn(false);

        NotFound exception = assertThrows(NotFound.class, () -> {
            createItemImpl.createItem(request);
        });
        assertEquals("The passed category does not exist", exception.getReason());
    }

}