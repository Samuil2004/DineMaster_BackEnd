package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import nl.fontys.s3.dinemasterpro.business.converters.SelectedItemConverter;
import nl.fontys.s3.dinemasterpro.business.dtos.get.orders_and_carts.GetSelectedItemsInOrdersByCategoryAndStatusRequest;
import nl.fontys.s3.dinemasterpro.business.dtos.get.orders_and_carts.GetSelectedItemsInOrdersByCategoryAndStatusResponse;
import nl.fontys.s3.dinemasterpro.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterpro.domain.classes.ItemCategory;
import nl.fontys.s3.dinemasterpro.domain.classes.SelectedItem;
import nl.fontys.s3.dinemasterpro.domain.classes.SelectedItemStatusOfPreparation;
import nl.fontys.s3.dinemasterpro.persistence.entity.ItemCategoryEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.SelectedItemEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.SelectedItemStatusOfPreparationEntity;
import nl.fontys.s3.dinemasterpro.persistence.repositories.ItemCategoryRepository;
import nl.fontys.s3.dinemasterpro.persistence.repositories.SelectedItemEntityRepository;
import nl.fontys.s3.dinemasterpro.persistence.repositories.SelectedItemStatusOfPreparationEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetSelectedItemInOrdersByCategoryAndStatusImplTest {

    @InjectMocks
    private GetSelectedItemInOrdersByCategoryAndStatusImpl getSelectedItemInOrdersByCategoryAndStatusImpl;

    @Mock
    private SelectedItemEntityRepository selectedItemEntityRepository;

    @Mock
    private SelectedItemStatusOfPreparationEntityRepository selectedItemStatusOfPreparationEntityRepository;

    @Mock
    private ItemCategoryRepository itemCategoryRepository;

    @Mock
    private SelectedItemConverter selectedItemConverter;

    private GetSelectedItemsInOrdersByCategoryAndStatusRequest request;
    private SelectedItemStatusOfPreparationEntity statusEntity;
    private SelectedItemStatusOfPreparation statusBase;

    private ItemCategoryEntity categoryEntity;
    private ItemCategory categoryBase;

    private SelectedItemEntity selectedItemEntity;
    private SelectedItem selectedItem;


    @BeforeEach
    void setUp() {
        request = GetSelectedItemsInOrdersByCategoryAndStatusRequest
                .builder()
                .status("PREPARING")
                .category("APPETIZER")
                .build();

        statusEntity = SelectedItemStatusOfPreparationEntity.builder().statusName("PREPARING").build();
        statusBase = SelectedItemStatusOfPreparation.builder().statusName("PREPARING").build();

        categoryEntity = ItemCategoryEntity.builder().categoryName("APPETIZER").categoryId(1L).build();
        categoryBase = ItemCategory.builder().categoryName("APPETIZER").categoryId(1L).build();

        selectedItemEntity = SelectedItemEntity.builder()
                .selectedItemId(1L)
                .statusOfPreparation(statusEntity)
                .itemCategory(categoryEntity)
                .build();

        selectedItem = SelectedItem.builder()
                .selectedItemId(1L)
                .statusOfPreparation(statusBase)
                .itemCategory(categoryBase)
                .build();
    }

    @Test
    void givenValidStatusAndCategory_whenSelectedItemsFound_thenReturnSelectedItems() {
        // Arrange
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName(request.getStatus())).thenReturn(Optional.of(statusEntity));
        when(itemCategoryRepository.findByCategoryName(request.getCategory())).thenReturn(Optional.of(categoryEntity));
        when(selectedItemEntityRepository.findByStatusOfPreparation_IdAndItemCategory_CategoryId(statusEntity.getId(), categoryEntity.getCategoryId()))
                .thenReturn(List.of(selectedItemEntity));
        when(selectedItemConverter.convertEntityToNormal(selectedItemEntity)).thenReturn(selectedItem);

        GetSelectedItemsInOrdersByCategoryAndStatusResponse response = getSelectedItemInOrdersByCategoryAndStatusImpl.getSelectedItemsInOrdersByCategoryAndStatus(request);

        assertNotNull(response);
        assertEquals(1, response.getSelectedItems().size());
        assertEquals(selectedItem, response.getSelectedItems().get(0));
        verify(selectedItemStatusOfPreparationEntityRepository).findByStatusName(request.getStatus());
        verify(itemCategoryRepository).findByCategoryName(request.getCategory());
        verify(selectedItemEntityRepository).findByStatusOfPreparation_IdAndItemCategory_CategoryId(statusEntity.getId(), categoryEntity.getCategoryId());
        verify(selectedItemConverter).convertEntityToNormal(selectedItemEntity);
    }

    @Test
    void givenInvalidStatus_whenStatusNotFound_thenThrowNotFoundException() {
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName(request.getStatus())).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> {
            getSelectedItemInOrdersByCategoryAndStatusImpl.getSelectedItemsInOrdersByCategoryAndStatus(request);
        });
        assertEquals("SELECTED ITEMS FOR GIVEN STATUS AND CATEGORY WERE NOT FOUND", exception.getReason());
        verify(selectedItemStatusOfPreparationEntityRepository).findByStatusName(request.getStatus());
    }

    @Test
    void givenInvalidCategory_whenCategoryNotFound_thenThrowNotFoundException() {
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName(request.getStatus())).thenReturn(Optional.of(statusEntity));
        when(itemCategoryRepository.findByCategoryName(request.getCategory())).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> {
            getSelectedItemInOrdersByCategoryAndStatusImpl.getSelectedItemsInOrdersByCategoryAndStatus(request);
        });
        assertEquals("SELECTED ITEMS FOR GIVEN STATUS AND CATEGORY WERE NOT FOUND", exception.getReason());
        verify(selectedItemStatusOfPreparationEntityRepository).findByStatusName(request.getStatus());
        verify(itemCategoryRepository).findByCategoryName(request.getCategory());
    }

    @Test
    void givenValidStatusAndCategory_whenNoSelectedItemsFound_thenReturnEmptyList() {
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName(request.getStatus())).thenReturn(Optional.of(statusEntity));
        when(itemCategoryRepository.findByCategoryName(request.getCategory())).thenReturn(Optional.of(categoryEntity));
        when(selectedItemEntityRepository.findByStatusOfPreparation_IdAndItemCategory_CategoryId(statusEntity.getId(), categoryEntity.getCategoryId()))
                .thenReturn(List.of());

        GetSelectedItemsInOrdersByCategoryAndStatusResponse response = getSelectedItemInOrdersByCategoryAndStatusImpl.getSelectedItemsInOrdersByCategoryAndStatus(request);

        assertNotNull(response);
        assertTrue(response.getSelectedItems().isEmpty());
        verify(selectedItemStatusOfPreparationEntityRepository).findByStatusName(request.getStatus());
        verify(itemCategoryRepository).findByCategoryName(request.getCategory());
        verify(selectedItemEntityRepository).findByStatusOfPreparation_IdAndItemCategory_CategoryId(statusEntity.getId(), categoryEntity.getCategoryId());
    }
}