package nl.fontys.s3.dinemasterbackend.business.item_use_cases.implementations;

import nl.fontys.s3.dinemasterpro.business.converters.ItemCategoryConverter;
import nl.fontys.s3.dinemasterpro.business.converters.ItemConverter;
import nl.fontys.s3.dinemasterpro.business.dtos.get.items.GetItemsForCategoryRequest;
import nl.fontys.s3.dinemasterpro.business.dtos.get.items.GetItemsForCategoryResponse;
import nl.fontys.s3.dinemasterpro.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterpro.domain.classes.Appetizer;
import nl.fontys.s3.dinemasterpro.domain.classes.Item;
import nl.fontys.s3.dinemasterpro.domain.classes.ItemCategory;
import nl.fontys.s3.dinemasterpro.persistence.entity.AppetizerEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.ItemCategoryEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.ItemEntity;
import nl.fontys.s3.dinemasterpro.persistence.repositories.ItemCategoryRepository;
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
class GetItemsFromCategoryImplTest {
    @Mock
    ItemConverter itemConverter;
    @Mock
    ItemCategoryConverter itemCategoryConverter;
    @Mock
    ItemCategoryRepository itemCategoryRepository;
    @Mock
    ItemEntityRepository itemEntityRepository;

    @InjectMocks
    GetItemsFromCategoryImpl getItemsFromCategoryImpl;

    GetItemsForCategoryRequest getItemsForCategoryRequestWithVisibility;
    GetItemsForCategoryRequest getItemsForCategoryRequestWithoutVisibility;

    ItemCategoryEntity itemCategoryEntityAppetizer;
    ItemCategory itemCategoryBaseAppetizer;
    AppetizerEntity appetizerEntity1;
    Appetizer appetizerBase1;

    AppetizerEntity appetizerEntity2;
    Appetizer appetizerBase2;

    List<ItemEntity> expectedItemsFromCategoryEntityWithVisibility;
    List<Item> expectedItemsFromCategoryBaseWithVisibility;

    List<ItemEntity> expectedItemsFromCategoryEntityWithoutVisibility;
    List<Item> expectedItemsFromCategoryBaseWithoutVisibility;


    @BeforeEach
    void setUp() {
        getItemsForCategoryRequestWithVisibility = GetItemsForCategoryRequest.builder().category("Appetizer").isVisibleInMenu(true).build();
        getItemsForCategoryRequestWithoutVisibility = GetItemsForCategoryRequest.builder().category("Appetizer").isVisibleInMenu(false).build();

        itemCategoryEntityAppetizer = ItemCategoryEntity.builder().categoryId(1L).categoryName("APPETIZER").build();
        itemCategoryBaseAppetizer = ItemCategory.builder().categoryId(1L).categoryName("APPETIZER").build();

        appetizerEntity1 = AppetizerEntity.builder()
                .itemId(1L)
                .itemName("Bruschetta")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .itemCategory(itemCategoryEntityAppetizer)
                .visibleInMenu(false)
                .isVegetarian(true)
                .build();

        appetizerBase1 = Appetizer.builder()
                .itemId(1L)
                .itemName("Bruschetta")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .itemCategory(itemCategoryBaseAppetizer)
                .visibleInMenu(false)
                .isVegetarian(true)
                .build();

        appetizerEntity2 = AppetizerEntity.builder()
                .itemId(2L)
                .itemName("Garlic bread")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .itemCategory(itemCategoryEntityAppetizer)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();

        appetizerBase2 = Appetizer.builder()
                .itemId(2L)
                .itemName("Garlic bread")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .itemCategory(itemCategoryBaseAppetizer)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();

        expectedItemsFromCategoryEntityWithVisibility = List.of(appetizerEntity2);
        expectedItemsFromCategoryBaseWithVisibility = List.of(appetizerBase2);

        expectedItemsFromCategoryEntityWithoutVisibility = List.of(appetizerEntity1, appetizerEntity2);
        expectedItemsFromCategoryBaseWithoutVisibility = List.of(appetizerBase1, appetizerBase2);
    }


    @Test
    void givenCategoryNameAndVisibility_whenLookingForItemsWithinProvidedCategoryAndVisibility_thenReturnsFoundItemsInCategoryWithGivenVisibility() {
        when(itemCategoryRepository.findByCategoryName(getItemsForCategoryRequestWithVisibility.getCategory().toUpperCase())).thenReturn(Optional.ofNullable(itemCategoryEntityAppetizer));
        when(itemCategoryConverter.convertEntityToNormal(itemCategoryEntityAppetizer)).thenReturn(itemCategoryBaseAppetizer);
        when(itemEntityRepository.findByItemCategory_CategoryIdAndVisibleInMenu(itemCategoryBaseAppetizer.getCategoryId(), getItemsForCategoryRequestWithVisibility.getIsVisibleInMenu())).thenReturn(expectedItemsFromCategoryEntityWithVisibility);
        when(itemConverter.convertEntityToNormal(appetizerEntity2)).thenReturn(appetizerBase2);

        GetItemsForCategoryResponse response = getItemsFromCategoryImpl.readItemsByCategory(getItemsForCategoryRequestWithVisibility);

        assertEquals(response.getItemsInCategory(), expectedItemsFromCategoryBaseWithVisibility);
        verify(itemCategoryRepository).findByCategoryName(getItemsForCategoryRequestWithVisibility.getCategory().toUpperCase());
        verify(itemCategoryConverter).convertEntityToNormal(itemCategoryEntityAppetizer);
        verify(itemConverter).convertEntityToNormal(appetizerEntity2);
        verify(itemEntityRepository).findByItemCategory_CategoryIdAndVisibleInMenu(itemCategoryBaseAppetizer.getCategoryId(), getItemsForCategoryRequestWithVisibility.getIsVisibleInMenu());

    }

    @Test
    void givenCategoryName_whenLookingForItemsWithinProvidedCategory_thenReturnsFoundItemsInCategory() {
        when(itemCategoryRepository.findByCategoryName(getItemsForCategoryRequestWithoutVisibility.getCategory().toUpperCase())).thenReturn(Optional.ofNullable(itemCategoryEntityAppetizer));
        when(itemCategoryConverter.convertEntityToNormal(itemCategoryEntityAppetizer)).thenReturn(itemCategoryBaseAppetizer);
        when(itemEntityRepository.findByItemCategory_CategoryId(itemCategoryBaseAppetizer.getCategoryId())).thenReturn(expectedItemsFromCategoryEntityWithoutVisibility);
        when(itemConverter.convertEntityToNormal(appetizerEntity1)).thenReturn(appetizerBase1);
        when(itemConverter.convertEntityToNormal(appetizerEntity2)).thenReturn(appetizerBase2);

        GetItemsForCategoryResponse response = getItemsFromCategoryImpl.readItemsByCategory(getItemsForCategoryRequestWithoutVisibility);

        assertEquals(response.getItemsInCategory(), expectedItemsFromCategoryBaseWithoutVisibility);
        verify(itemCategoryRepository).findByCategoryName(getItemsForCategoryRequestWithoutVisibility.getCategory().toUpperCase());
        verify(itemCategoryConverter).convertEntityToNormal(itemCategoryEntityAppetizer);
        verify(itemConverter).convertEntityToNormal(appetizerEntity1);
        verify(itemConverter).convertEntityToNormal(appetizerEntity2);
        verify(itemEntityRepository).findByItemCategory_CategoryId(itemCategoryBaseAppetizer.getCategoryId());

    }

    @Test
    void givenCategoryName_whenGivenCategoryIsNotFound_thenThrowsCategoryNotFoundException() {
        when(itemCategoryRepository.findByCategoryName(getItemsForCategoryRequestWithVisibility.getCategory().toUpperCase())).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> {
            getItemsFromCategoryImpl.readItemsByCategory(getItemsForCategoryRequestWithVisibility);
        });
        assertEquals("ITEM_CATEGORY_NOT_FOUND", exception.getReason());

    }
}