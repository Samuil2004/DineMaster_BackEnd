package nl.fontys.s3.dinemasterbackend.business.converters;

import nl.fontys.s3.dinemasterpro.domain.classes.*;
import nl.fontys.s3.dinemasterpro.persistence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ItemConverterTest {
    @Mock
    PizzaSizesConverter pizzaSizesConverter;
    @Mock
    ItemCategoryConverter itemCategoryConverter;
    @InjectMocks
    ItemConverter itemConverter;

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


    AppetizerEntity appetizerEntity;
    Appetizer appetizerBase;
    ItemCategoryEntity itemCategoryEntityAppetizer;
    ItemCategory itemCategoryBaseAppetizer;

    BeverageEntity beverageEntity;
    Beverage beverageBase;
    ItemCategoryEntity itemCategoryEntityBeverage;
    ItemCategory itemCategoryBaseBeverage;

    ItemCategoryEntity itemCategoryEntityBurger;
    ItemCategory itemCategoryBaseBurger;
    BurgerEntity burgerEntity;
    Burger burgerBase;


    ItemCategoryEntity itemCategoryEntityGrill;
    ItemCategory itemCategoryBaseGrill;
    GrillEntity grillEntity;
    Grill grillBase;


    ItemCategoryEntity itemCategoryEntityPasta;
    ItemCategory itemCategoryBasePasta;
    PastaEntity pastaEntity;
    Pasta pastaBase;

    ItemCategoryEntity itemCategoryEntitySalad;
    ItemCategory itemCategoryBaseSalad;
    SaladEntity saladEntity;
    Salad saladBase;


    ItemCategoryEntity itemCategoryEntitySoup;
    ItemCategory itemCategoryBaseSoup;
    SoupEntity soupEntity;
    Soup soupBase;



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

        itemCategoryEntityBurger = ItemCategoryEntity.builder().categoryId(2L).categoryName("BURGER").build();
        itemCategoryBaseBurger = ItemCategory.builder().categoryId(2L).categoryName("BURGER").build();

        itemCategoryEntityGrill = ItemCategoryEntity.builder().categoryId(3L).categoryName("GRILL").build();
        itemCategoryBaseGrill = ItemCategory.builder().categoryId(3L).categoryName("GRILL").build();

        itemCategoryEntityPasta = ItemCategoryEntity.builder().categoryId(4L).categoryName("PASTA").build();
        itemCategoryBasePasta = ItemCategory.builder().categoryId(4L).categoryName("PASTA").build();

        itemCategoryEntitySalad = ItemCategoryEntity.builder().categoryId(5L).categoryName("SALAD").build();
        itemCategoryBaseSalad = ItemCategory.builder().categoryId(5L).categoryName("SALAD").build();

        itemCategoryEntitySoup = ItemCategoryEntity.builder().categoryId(6L).categoryName("SOUP").build();
        itemCategoryBaseSoup = ItemCategory.builder().categoryId(6L).categoryName("SOUP").build();


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

    }

    @Test
    void givenPizzaEntity_whenConvertsPizzaEntityToNormalPizza_thenReturnsBasePizza() {
        when(pizzaSizesConverter.convertEntityToNormal(pizzaSizeEntity1)).thenReturn(pizzaSizeBase1);
        when(pizzaSizesConverter.convertEntityToNormal(pizzaSizeEntity2)).thenReturn(pizzaSizeBase2);
        when(pizzaSizesConverter.convertEntityToNormal(pizzaSizeEntity3)).thenReturn(pizzaSizeBase3);
        when(itemCategoryConverter.convertEntityToNormal(itemCategoryEntityPizza)).thenReturn(itemCategoryBasePizza);

        Item convertedItem = itemConverter.convertEntityToNormal(peperoniEntity);

        assertEquals(convertedItem,peperoniBase);
        verify(itemCategoryConverter).convertEntityToNormal(itemCategoryEntityPizza);
    }

    @Test
    void givenAppetizerEntity_whenConvertsAppetizerEntityToNormalPizza_thenReturnsBaseAppetizer() {
        when(itemCategoryConverter.convertEntityToNormal(itemCategoryEntityAppetizer)).thenReturn(itemCategoryBaseAppetizer);

        Item convertedItem = itemConverter.convertEntityToNormal(appetizerEntity);

        assertEquals(convertedItem,appetizerBase);
        verify(itemCategoryConverter).convertEntityToNormal(itemCategoryEntityAppetizer);
    }


    @Test
    void givenBeverageEntity_whenConvertsBeverageEntityToNormal_thenReturnsBaseBeverage() {
        when(itemCategoryConverter.convertEntityToNormal(itemCategoryEntityBeverage)).thenReturn(itemCategoryBaseBeverage);

        Item convertedItem = itemConverter.convertEntityToNormal(beverageEntity);

        assertEquals(convertedItem, beverageBase);
        verify(itemCategoryConverter).convertEntityToNormal(itemCategoryEntityBeverage);
    }

    @Test
    void givenBurgerEntity_whenConvertsBurgerEntityToNormal_thenReturnsBaseBurger() {
        when(itemCategoryConverter.convertEntityToNormal(itemCategoryEntityBurger)).thenReturn(itemCategoryBaseBurger);

        Item convertedItem = itemConverter.convertEntityToNormal(burgerEntity);

        assertEquals(convertedItem, burgerBase);
        verify(itemCategoryConverter).convertEntityToNormal(itemCategoryEntityBurger);
    }

    @Test
    void givenGrillEntity_whenConvertsGrillEntityToNormal_thenReturnsBaseGrill() {
        when(itemCategoryConverter.convertEntityToNormal(itemCategoryEntityGrill)).thenReturn(itemCategoryBaseGrill);

        Item convertedItem = itemConverter.convertEntityToNormal(grillEntity);

        assertEquals(convertedItem, grillBase);
        verify(itemCategoryConverter).convertEntityToNormal(itemCategoryEntityGrill);
    }

    @Test
    void givenPastaEntity_whenConvertsPastaEntityToNormal_thenReturnsBasePasta() {
        when(itemCategoryConverter.convertEntityToNormal(itemCategoryEntityPasta)).thenReturn(itemCategoryBasePasta);

        Item convertedItem = itemConverter.convertEntityToNormal(pastaEntity);

        assertEquals(convertedItem, pastaBase);
        verify(itemCategoryConverter).convertEntityToNormal(itemCategoryEntityPasta);
    }

    @Test
    void givenSaladEntity_whenConvertsSaladEntityToNormal_thenReturnsBaseSalad() {
        when(itemCategoryConverter.convertEntityToNormal(itemCategoryEntitySalad)).thenReturn(itemCategoryBaseSalad);

        Item convertedItem = itemConverter.convertEntityToNormal(saladEntity);

        assertEquals(convertedItem, saladBase);
        verify(itemCategoryConverter).convertEntityToNormal(itemCategoryEntitySalad);
    }

    @Test
    void givenSoupEntity_whenConvertsSoupEntityToNormal_thenReturnsBaseSoup() {
        when(itemCategoryConverter.convertEntityToNormal(itemCategoryEntitySoup)).thenReturn(itemCategoryBaseSoup);

        Item convertedItem = itemConverter.convertEntityToNormal(soupEntity);

        assertEquals(convertedItem, soupBase);
        verify(itemCategoryConverter).convertEntityToNormal(itemCategoryEntitySoup);
    }

    @Test
    void givenPizzaBase_whenConvertsPizzaBaseToEntityPizza_thenReturnsEntityPizza() {
        when(pizzaSizesConverter.convertNormalToEntity(pizzaSizeBase1)).thenReturn(pizzaSizeEntity1);
        when(pizzaSizesConverter.convertNormalToEntity(pizzaSizeBase2)).thenReturn(pizzaSizeEntity2);
        when(pizzaSizesConverter.convertNormalToEntity(pizzaSizeBase3)).thenReturn(pizzaSizeEntity3);
        when(itemCategoryConverter.convertNormalToEntity(itemCategoryBasePizza)).thenReturn(itemCategoryEntityPizza);

        ItemEntity convertedItem = itemConverter.convertNormalToEntity(peperoniBase);

        assertEquals(convertedItem,peperoniEntity);
        verify(itemCategoryConverter).convertNormalToEntity(itemCategoryBasePizza);
    }
    @Test
    void givenAppetizerBase_whenConvertsAppetizerBaseToEntityAppetizer_thenReturnsEntityAppetizer() {
        when(itemCategoryConverter.convertNormalToEntity(itemCategoryBaseAppetizer)).thenReturn(itemCategoryEntityAppetizer);

        ItemEntity convertedItem = itemConverter.convertNormalToEntity(appetizerBase);

        assertEquals(convertedItem, appetizerEntity);
        verify(itemCategoryConverter).convertNormalToEntity(itemCategoryBaseAppetizer);
    }
    @Test
    void givenBeverageBase_whenConvertsBeverageBaseToEntityBeverage_thenReturnsEntityBeverage() {
        when(itemCategoryConverter.convertNormalToEntity(itemCategoryBaseBeverage)).thenReturn(itemCategoryEntityBeverage);

        ItemEntity convertedItem = itemConverter.convertNormalToEntity(beverageBase);

        assertEquals(convertedItem, beverageEntity);
        verify(itemCategoryConverter).convertNormalToEntity(itemCategoryBaseBeverage);
    }

    @Test
    void givenBurgerBase_whenConvertsBurgerBaseToEntityBurger_thenReturnsEntityBurger() {
        when(itemCategoryConverter.convertNormalToEntity(itemCategoryBaseBurger)).thenReturn(itemCategoryEntityBurger);

        ItemEntity convertedItem = itemConverter.convertNormalToEntity(burgerBase);

        assertEquals(convertedItem, burgerEntity);
        verify(itemCategoryConverter).convertNormalToEntity(itemCategoryBaseBurger);
    }

    @Test
    void givenGrillBase_whenConvertsGrillBaseToEntityGrill_thenReturnsEntityGrill() {
        when(itemCategoryConverter.convertNormalToEntity(itemCategoryBaseGrill)).thenReturn(itemCategoryEntityGrill);

        ItemEntity convertedItem = itemConverter.convertNormalToEntity(grillBase);

        assertEquals(convertedItem, grillEntity);
        verify(itemCategoryConverter).convertNormalToEntity(itemCategoryBaseGrill);
    }

    @Test
    void givenPastaBase_whenConvertsPastaBaseToEntityPasta_thenReturnsEntityPasta() {
        when(itemCategoryConverter.convertNormalToEntity(itemCategoryBasePasta)).thenReturn(itemCategoryEntityPasta);

        ItemEntity convertedItem = itemConverter.convertNormalToEntity(pastaBase);

        assertEquals(convertedItem, pastaEntity);
        verify(itemCategoryConverter).convertNormalToEntity(itemCategoryBasePasta);
    }

    @Test
    void givenSaladBase_whenConvertsSaladBaseToEntitySalad_thenReturnsEntitySalad() {
        when(itemCategoryConverter.convertNormalToEntity(itemCategoryBaseSalad)).thenReturn(itemCategoryEntitySalad);

        ItemEntity convertedItem = itemConverter.convertNormalToEntity(saladBase);

        assertEquals(convertedItem, saladEntity);
        verify(itemCategoryConverter).convertNormalToEntity(itemCategoryBaseSalad);
    }

    @Test
    void givenSoupBase_whenConvertsSoupBaseToEntitySoup_thenReturnsEntitySoup() {
        when(itemCategoryConverter.convertNormalToEntity(itemCategoryBaseSoup)).thenReturn(itemCategoryEntitySoup);

        ItemEntity convertedItem = itemConverter.convertNormalToEntity(soupBase);

        assertEquals(convertedItem, soupEntity);
        verify(itemCategoryConverter).convertNormalToEntity(itemCategoryBaseSoup);
    }





}