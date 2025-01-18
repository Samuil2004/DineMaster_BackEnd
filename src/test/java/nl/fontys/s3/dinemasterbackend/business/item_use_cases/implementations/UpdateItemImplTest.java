package nl.fontys.s3.dinemasterbackend.business.item_use_cases.implementations;

import nl.fontys.s3.dinemasterpro.business.converters.ItemConverter;
import nl.fontys.s3.dinemasterpro.business.dtos.update.items.*;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateItemImplTest {
    @Mock
    ItemEntityRepository itemEntityRepository;
    @Mock
    ItemConverter itemConverterMock;
    @InjectMocks
    UpdateItemImpl updateItemImpl;

    PizzaEntity existingPizzaEntity;
    Pizza existingPizzaBase;
    UpdatePizzaRequest updatePizzaRequest;
    PizzaEntity updatedPizzaEntity;


    AppetizerEntity existingAppetizerEntity;
    UpdateAppetizerRequest updateAppetizerRequest;
    Appetizer existingAppetizerBase;
    AppetizerEntity updatedAppetizerEntity;

    BeverageEntity existingBeverageEntity;
    UpdateBeverageRequest updateBeverageRequest;
    Beverage existingBeverageBase;
    BeverageEntity updatedBeverageEntity;

    BurgerEntity existingBurgerEntity;
    UpdateBurgerRequest updateBurgerRequest;
    Burger existingBurgerBase;
    BurgerEntity updatedBurgerEntity;

    GrillEntity existingGrillEntity;
    UpdateGrillRequest updateGrillRequest;
    Grill existingGrillBase;
    GrillEntity updatedGrillEntity;

    PastaEntity existingPastaEntity;
    UpdatePastaRequest updatePastaRequest;
    Pasta existingPastaBase;
    PastaEntity updatedPastaEntity;

    SaladEntity existingSaladEntity;
    SaladEntity updatedSaladEntity;
    UpdateSaladRequest updateSaladRequest;
    Salad existingSaladBase;

    SoupEntity existingSoupEntity;
    SoupEntity updatedSoupEntity;
    UpdateSoupRequest updateSoupRequest;
    Soup existingSoupBase;

    @BeforeEach
    void setUp() {
        existingPizzaEntity = PizzaEntity.builder()
                .itemId(1L)
                .itemName("Peperoni")
                .itemImageVersion("v1730296343")
                .itemPrice(12.50)
                .ingredients(List.of("dough", "sauce", "cheese", "pepperoni", "oliveOil", "seasoning", "garlicPowder"))
                .sizes(List.of())
                .visibleInMenu(true)
                .itemCategory(null)
                .build();

        existingPizzaBase = Pizza.builder()
                .itemId(1L)
                .itemName("Peperoni")
                .itemImageVersion("v1730296343")
                .itemPrice(12.50)
                .ingredients(List.of("dough", "sauce", "cheese", "pepperoni", "oliveOil", "seasoning", "garlicPowder"))
                .sizes(List.of())
                .visibleInMenu(true)
                .itemCategory(null)
                .build();

        updatePizzaRequest = UpdatePizzaRequest.builder()
                .itemId(1L)
                .itemName("Peperoni Updated")
                .itemImageVersion("v1730296344")
                .itemPrice(13.00)
                .ingredients(List.of("dough", "sauce", "cheese", "pepperoni", "oliveOil", "seasoning", "garlicPowder"))
                .visibleInMenu(true)
                .sizes(List.of())
                .base("Thick Crust")
                .build();

        updatedPizzaEntity = PizzaEntity.builder()
                .itemId(1L)
                .itemName("Peperoni Updated")
                .itemImageVersion("v1730296343")
                .itemPrice(12.50)
                .ingredients(List.of("dough", "sauce", "cheese", "pepperoni", "oliveOil", "seasoning", "garlicPowder"))
                .sizes(List.of())
                .visibleInMenu(true)
                .itemCategory(null)
                .build();


        updateAppetizerRequest = UpdateAppetizerRequest.builder()
                .itemId(1L)
                .itemName("Bruschetta Updated")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();

        existingAppetizerEntity = AppetizerEntity.builder()
                .itemId(1L)
                .itemName("Bruschetta")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .itemCategory(null)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();

        existingAppetizerBase = Appetizer.builder()
                .itemId(1L)
                .itemName("Bruschetta")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .itemCategory(null)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();

        updatedAppetizerEntity = AppetizerEntity.builder()
                .itemId(1L)
                .itemName("Bruschetta")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .itemCategory(null)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();

        existingBeverageEntity = BeverageEntity.builder()
                .itemId(1L)
                .itemName("Cola")
                .itemImageVersion("v1730296343")
                .itemPrice(2.50)
                .visibleInMenu(true)
                .itemCategory(null)
                .size(0.33)
                .build();

        existingBeverageBase = Beverage.builder()
                .itemId(1L)
                .itemName("Cola")
                .itemImageVersion("v1730296343")
                .itemPrice(2.50)
                .visibleInMenu(true)
                .itemCategory(null)
                .size(0.33)
                .build();

        updateBeverageRequest = UpdateBeverageRequest.builder()
                .itemId(1L)
                .itemName("Cola Updated")
                .itemImageVersion("v1730296344")
                .itemPrice(3.00)
                .size(0.50)
                .visibleInMenu(true)
                .build();

        updatedBeverageEntity = BeverageEntity.builder()
                .itemId(1L)
                .itemName("Cola Updated")
                .itemImageVersion("v1730296344")
                .itemPrice(3.00)
                .visibleInMenu(true)
                .itemCategory(null)
                .size(0.50)
                .build();

        existingBurgerEntity = BurgerEntity.builder()
                .itemId(1L)
                .itemName("Classic Burger")
                .itemImageVersion("v1730296343")
                .itemPrice(8.50)
                .visibleInMenu(true)
                .itemCategory(null)
                .weight(250)
                .ingredients(List.of("beef patty", "lettuce", "tomato", "cheese", "bun"))
                .build();

        existingBurgerBase = Burger.builder()
                .itemId(1L)
                .itemName("Classic Burger")
                .itemImageVersion("v1730296343")
                .itemPrice(8.50)
                .visibleInMenu(true)
                .itemCategory(null)
                .weight(250)
                .ingredients(List.of("beef patty", "lettuce", "tomato", "cheese", "bun"))
                .build();

        updateBurgerRequest = UpdateBurgerRequest.builder()
                .itemId(1L)
                .itemName("Classic Burger Updated")
                .itemImageVersion("v1730296344")
                .itemPrice(9.00)
                .weight(300)
                .ingredients(List.of("beef patty", "lettuce", "tomato", "cheese", "bun", "bacon"))
                .visibleInMenu(true)
                .build();

        updatedBurgerEntity = BurgerEntity.builder()
                .itemId(1L)
                .itemName("Classic Burger Updated")
                .itemImageVersion("v1730296344")
                .itemPrice(9.00)
                .visibleInMenu(true)
                .itemCategory(null)
                .weight(300)
                .ingredients(List.of("beef patty", "lettuce", "tomato", "cheese", "bun", "bacon"))
                .build();


        existingGrillEntity = GrillEntity.builder()
                .itemId(1L)
                .itemName("BBQ Ribs")
                .itemImageVersion("v1730296344")
                .itemPrice(15.99)
                .visibleInMenu(true)
                .itemCategory(null)
                .weight(500)
                .ingredients(List.of("pork ribs", "BBQ sauce"))
                .build();

        existingGrillBase = Grill.builder()
                .itemId(1L)
                .itemName("BBQ Ribs")
                .itemImageVersion("v1730296344")
                .itemPrice(15.99)
                .visibleInMenu(true)
                .itemCategory(null)
                .weight(500)
                .ingredients(List.of("pork ribs", "BBQ sauce"))
                .build();

        updateGrillRequest = UpdateGrillRequest.builder()
                .itemId(1L)
                .itemName("BBQ Ribs Updated")
                .itemImageVersion("v1730296345")
                .itemPrice(16.99)
                .weight(600)
                .ingredients(List.of("pork ribs", "BBQ sauce", "garlic"))
                .visibleInMenu(true)
                .build();

        updatedGrillEntity = GrillEntity.builder()
                .itemId(1L)
                .itemName("BBQ Ribs Updated")
                .itemImageVersion("v1730296345")
                .itemPrice(16.99)
                .visibleInMenu(true)
                .itemCategory(null)
                .weight(600)
                .ingredients(List.of("pork ribs", "BBQ sauce", "garlic"))
                .build();


        existingPastaEntity = PastaEntity.builder()
                .itemId(1L)
                .itemName("Spaghetti Carbonara")
                .itemImageVersion("v1730296344")
                .itemPrice(12.99)
                .visibleInMenu(true)
                .itemCategory(null)
                .pastaType("Spaghetti")
                .weight(400)
                .ingredients(List.of("spaghetti", "egg", "bacon", "parmesan"))
                .build();

        existingPastaBase = Pasta.builder()
                .itemId(1L)
                .itemName("Spaghetti Carbonara")
                .itemImageVersion("v1730296344")
                .itemPrice(12.99)
                .visibleInMenu(true)
                .itemCategory(null)
                .pastaType("Spaghetti")
                .weight(400)
                .ingredients(List.of("spaghetti", "egg", "bacon", "parmesan"))
                .build();

        updatePastaRequest = UpdatePastaRequest.builder()
                .itemId(1L)
                .itemName("Spaghetti Carbonara Updated")
                .itemImageVersion("v1730296345")
                .itemPrice(13.99)
                .pastaType("Linguine")
                .weight(500)
                .ingredients(List.of("linguine", "egg", "bacon", "parmesan", "garlic"))
                .visibleInMenu(true)
                .build();

        updatedPastaEntity = PastaEntity.builder()
                .itemId(1L)
                .itemName("Spaghetti Carbonara Updated")
                .itemImageVersion("v1730296345")
                .itemPrice(13.99)
                .visibleInMenu(true)
                .itemCategory(null)
                .pastaType("Linguine")
                .weight(500)
                .ingredients(List.of("linguine", "egg", "bacon", "parmesan", "garlic"))
                .build();


        existingSaladEntity = SaladEntity.builder()
                .itemId(1L)
                .itemName("Caesar Salad")
                .itemImageVersion("v1730296345")
                .itemPrice(7.00)
                .visibleInMenu(true)
                .itemCategory(null)
                .weight(150)
                .ingredients(List.of("lettuce", "parmesan", "croutons", "caesar dressing"))
                .build();

        existingSaladBase = Salad.builder()
                .itemId(1L)
                .itemName("Caesar Salad")
                .itemImageVersion("v1730296345")
                .itemPrice(7.00)
                .visibleInMenu(true)
                .itemCategory(null)
                .weight(150)
                .ingredients(List.of("lettuce", "parmesan", "croutons", "caesar dressing"))
                .build();

        updateSaladRequest = UpdateSaladRequest.builder()
                .itemId(1L)
                .itemName("Caesar Salad Updated")
                .itemImageVersion("v1730296346")
                .itemPrice(8.00)
                .weight(200)
                .ingredients(List.of("lettuce", "parmesan", "croutons", "caesar dressing", "chicken"))
                .visibleInMenu(true)
                .build();

        updatedSaladEntity = SaladEntity.builder()
                .itemId(1L)
                .itemName("Caesar Salad Updated")
                .itemImageVersion("v1730296346")
                .itemPrice(8.00)
                .visibleInMenu(true)
                .itemCategory(null)
                .weight(200)
                .ingredients(List.of("lettuce", "parmesan", "croutons", "caesar dressing", "chicken"))
                .build();

        existingSoupEntity = SoupEntity.builder()
                .itemId(1L)
                .itemName("Tomato Soup")
                .itemImageVersion("v1730296344")
                .itemPrice(5.50)
                .visibleInMenu(true)
                .itemCategory(null)
                .isVegetarian(true)
                .ingredients(List.of("tomatoes", "onions", "garlic", "vegetable broth"))
                .build();

        existingSoupBase = Soup.builder()
                .itemId(1L)
                .itemName("Tomato Soup")
                .itemImageVersion("v1730296344")
                .itemPrice(5.50)
                .visibleInMenu(true)
                .itemCategory(null)
                .isVegetarian(true)
                .ingredients(List.of("tomatoes", "onions", "garlic", "vegetable broth"))
                .build();

        updateSoupRequest = UpdateSoupRequest.builder()
                .itemId(1L)
                .itemName("Tomato Soup Updated")
                .itemImageVersion("v1730296345")
                .itemPrice(6.00)
                .isVegetarian(false)
                .ingredients(List.of("tomatoes", "onions", "garlic", "chicken broth"))
                .visibleInMenu(true)
                .build();

        updatedSoupEntity = SoupEntity.builder()
                .itemId(1L)
                .itemName("Tomato Soup Updated")
                .itemImageVersion("v1730296345")
                .itemPrice(6.00)
                .visibleInMenu(true)
                .itemCategory(null)
                .isVegetarian(false)
                .ingredients(List.of("tomatoes", "onions", "garlic", "chicken broth"))
                .build();
    }

    @Test
    void givenValidPizzaId_whenUpdatingPizza_thenTheItemIsUpdated() {

        when(itemEntityRepository.findById(1L)).thenReturn(Optional.of(existingPizzaEntity));
        when(itemConverterMock.convertEntityToNormal(existingPizzaEntity)).thenReturn(existingPizzaBase);
        when(itemConverterMock.convertNormalToEntity(any(Pizza.class))).thenReturn(updatedPizzaEntity);

        updateItemImpl.updateItem(updatePizzaRequest);

        verify(itemEntityRepository).findById(1L);
        verify(itemEntityRepository).save(updatedPizzaEntity);
    }

    @Test
    void givenValidAppetizerId_whenUpdatingAppetizer_thenTheItemIsUpdated() {

        when(itemEntityRepository.findById(1L)).thenReturn(Optional.of(existingAppetizerEntity));
        when(itemConverterMock.convertEntityToNormal(existingAppetizerEntity)).thenReturn(existingAppetizerBase);
        when(itemConverterMock.convertNormalToEntity(any(Appetizer.class))).thenReturn(updatedAppetizerEntity);

        updateItemImpl.updateItem(updateAppetizerRequest);

        verify(itemEntityRepository).findById(1L);
        verify(itemEntityRepository).save(updatedAppetizerEntity);
    }

    @Test
    void givenValidBeverageId_whenUpdatingBeverage_thenTheItemIsUpdated() {
        when(itemEntityRepository.findById(1L)).thenReturn(Optional.of(existingBeverageEntity));
        when(itemConverterMock.convertEntityToNormal(existingBeverageEntity)).thenReturn(existingBeverageBase);
        when(itemConverterMock.convertNormalToEntity(any(Beverage.class))).thenReturn(updatedBeverageEntity);

        updateItemImpl.updateItem(updateBeverageRequest);

        verify(itemEntityRepository).findById(1L);
        verify(itemEntityRepository).save(updatedBeverageEntity);
    }

    @Test
    void givenValidBurgerId_whenUpdatingBurger_thenTheItemIsUpdated() {
        when(itemEntityRepository.findById(1L)).thenReturn(Optional.of(existingBurgerEntity));
        when(itemConverterMock.convertEntityToNormal(existingBurgerEntity)).thenReturn(existingBurgerBase);
        when(itemConverterMock.convertNormalToEntity(any(Burger.class))).thenReturn(updatedBurgerEntity);

        updateItemImpl.updateItem(updateBurgerRequest);

        verify(itemEntityRepository).findById(1L);
        verify(itemEntityRepository).save(updatedBurgerEntity);
    }

    @Test
    void givenValidGrillId_whenUpdatingGrill_thenTheItemIsUpdated() {
        when(itemEntityRepository.findById(1L)).thenReturn(Optional.of(existingGrillEntity));
        when(itemConverterMock.convertEntityToNormal(existingGrillEntity)).thenReturn(existingGrillBase);
        when(itemConverterMock.convertNormalToEntity(any(Grill.class))).thenReturn(updatedGrillEntity);

        updateItemImpl.updateItem(updateGrillRequest);

        verify(itemEntityRepository).findById(1L);
        verify(itemEntityRepository).save(updatedGrillEntity);
    }

    @Test
    void givenValidPastaId_whenUpdatingPasta_thenTheItemIsUpdated() {
        when(itemEntityRepository.findById(1L)).thenReturn(Optional.of(existingPastaEntity));
        when(itemConverterMock.convertEntityToNormal(existingPastaEntity)).thenReturn(existingPastaBase);
        when(itemConverterMock.convertNormalToEntity(any(Pasta.class))).thenReturn(updatedPastaEntity);

        updateItemImpl.updateItem(updatePastaRequest);

        verify(itemEntityRepository).findById(1L);
        verify(itemEntityRepository).save(updatedPastaEntity);
    }

    @Test
    void givenValidSaladId_whenUpdatingSalad_thenTheItemIsUpdated() {
        when(itemEntityRepository.findById(1L)).thenReturn(Optional.of(existingSaladEntity));
        when(itemConverterMock.convertEntityToNormal(existingSaladEntity)).thenReturn(existingSaladBase);
        when(itemConverterMock.convertNormalToEntity(any(Salad.class))).thenReturn(updatedSaladEntity);

        updateItemImpl.updateItem(updateSaladRequest);

        verify(itemEntityRepository).findById(1L);
        verify(itemEntityRepository).save(updatedSaladEntity);
    }

    @Test
    void givenValidSoupId_whenUpdatingSoup_thenTheItemIsUpdated() {
        when(itemEntityRepository.findById(1L)).thenReturn(Optional.of(existingSoupEntity));
        when(itemConverterMock.convertEntityToNormal(existingSoupEntity)).thenReturn(existingSoupBase);
        when(itemConverterMock.convertNormalToEntity(any(Soup.class))).thenReturn(updatedSoupEntity);

        updateItemImpl.updateItem(updateSoupRequest);

        verify(itemEntityRepository).findById(1L);
        verify(itemEntityRepository).save(updatedSoupEntity);
    }


    @Test
    void givenNonExistingItemId_whenUpdatingPizza_thenThrowItemNotFoundException() {
        when(itemEntityRepository.findById(1L)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> {
            updateItemImpl.updateItem(updatePizzaRequest);
        });

        assertEquals("Item with ID " + updatePizzaRequest.getItemId() + " not found.", exception.getReason());
        verify(itemEntityRepository).findById(1L);
    }

    @Test
    void givenNonPizzaItem_whenUpdatingPizza_thenThrowsOperationNotPossible() {
        when(itemEntityRepository.findById(1L)).thenReturn(Optional.of(existingBurgerEntity));
        when(itemConverterMock.convertEntityToNormal(existingBurgerEntity)).thenReturn(existingBurgerBase);

        assertThrows(NotFound.class, () -> updateItemImpl.updateItem(updatePizzaRequest), "Item not found");
    }

    @Test
    void givenNonPastaItem_whenUpdatingPasta_thenThrowsOperationNotPossible() {
        when(itemEntityRepository.findById(1L)).thenReturn(Optional.of(existingPizzaEntity));
        when(itemConverterMock.convertEntityToNormal(existingPizzaEntity)).thenReturn(existingPizzaBase);

        assertThrows(NotFound.class, () -> updateItemImpl.updateItem(updatePastaRequest), "Item not found");
    }

    @Test
    void givenNonAppetizerItem_whenUpdatingAppetizer_thenThrowsOperationNotPossible() {
        when(itemEntityRepository.findById(1L)).thenReturn(Optional.of(existingPizzaEntity));
        when(itemConverterMock.convertEntityToNormal(existingPizzaEntity)).thenReturn(existingPizzaBase);

        assertThrows(NotFound.class, () -> updateItemImpl.updateItem(updateAppetizerRequest), "Item not found");
    }

    @Test
    void givenNonBeverageItem_whenUpdatingBeverage_thenThrowsOperationNotPossible() {
        when(itemEntityRepository.findById(1L)).thenReturn(Optional.of(existingPizzaEntity));
        when(itemConverterMock.convertEntityToNormal(existingPizzaEntity)).thenReturn(existingPizzaBase);

        assertThrows(NotFound.class, () -> updateItemImpl.updateItem(updateBeverageRequest), "Item not found");
    }

    @Test
    void givenNonBurgerItem_whenUpdatingBurger_thenThrowsOperationNotPossible() {
        when(itemEntityRepository.findById(1L)).thenReturn(Optional.of(existingPizzaEntity));
        when(itemConverterMock.convertEntityToNormal(existingPizzaEntity)).thenReturn(existingPizzaBase);

        assertThrows(NotFound.class, () -> updateItemImpl.updateItem(updateBurgerRequest), "Item not found");
    }

    @Test
    void givenNonGrillItem_whenUpdatingGrill_thenThrowsOperationNotPossible() {
        when(itemEntityRepository.findById(1L)).thenReturn(Optional.of(existingPizzaEntity));
        when(itemConverterMock.convertEntityToNormal(existingPizzaEntity)).thenReturn(existingPizzaBase);

        assertThrows(NotFound.class, () -> updateItemImpl.updateItem(updateGrillRequest), "Item not found");
    }

    @Test
    void givenNonSaladItem_whenUpdatingSalad_thenThrowsOperationNotPossible() {
        when(itemEntityRepository.findById(1L)).thenReturn(Optional.of(existingPizzaEntity));
        when(itemConverterMock.convertEntityToNormal(existingPizzaEntity)).thenReturn(existingPizzaBase);

        assertThrows(NotFound.class, () -> updateItemImpl.updateItem(updateSaladRequest), "Item not found");
    }

    @Test
    void givenNonSoupItem_whenUpdatingSoup_thenThrowsOperationNotPossible() {
        when(itemEntityRepository.findById(1L)).thenReturn(Optional.of(existingPizzaEntity));
        when(itemConverterMock.convertEntityToNormal(existingPizzaEntity)).thenReturn(existingPizzaBase);

        assertThrows(NotFound.class, () -> updateItemImpl.updateItem(updateSoupRequest), "Item not found");
    }


}