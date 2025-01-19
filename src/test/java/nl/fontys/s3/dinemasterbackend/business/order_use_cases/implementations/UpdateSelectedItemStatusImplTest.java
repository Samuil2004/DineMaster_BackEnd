package nl.fontys.s3.dinemasterbackend.business.order_use_cases.implementations;

import nl.fontys.s3.dinemasterbackend.business.dtos.update.orders.UpdateSelectedItemStatusRequest;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.exceptions.OperationNotPossible;
import nl.fontys.s3.dinemasterbackend.persistence.entity.SelectedItemEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.SelectedItemStatusOfPreparationEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.SelectedItemEntityRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.SelectedItemStatusOfPreparationEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateSelectedItemStatusImplTest {

    @InjectMocks
    private UpdateSelectedItemStatusImpl updateSelectedItemStatusImpl;

    @Mock
    private SelectedItemEntityRepository selectedItemEntityRepository;

    @Mock
    private SelectedItemStatusOfPreparationEntityRepository selectedItemStatusOfPreparationEntityRepository;

    private UpdateSelectedItemStatusRequest request;
    private SelectedItemEntity selectedItemEntity;
    private SelectedItemStatusOfPreparationEntity preparedStatus;

    @BeforeEach
    void setUp() {
        request = UpdateSelectedItemStatusRequest.builder().selectedItemId(1L).build();

        selectedItemEntity = new SelectedItemEntity();
        selectedItemEntity.setSelectedItemId(1L);

        preparedStatus = new SelectedItemStatusOfPreparationEntity();
        preparedStatus.setStatusName("PREPARED");
    }

    @Test
    void givenValidSelectedItem_whenPreparedStatusExists_thenUpdateStatus() {
        when(selectedItemEntityRepository.findById(1L)).thenReturn(Optional.of(selectedItemEntity));
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName("PREPARED"))
                .thenReturn(Optional.of(preparedStatus));

        updateSelectedItemStatusImpl.updateSelectedItemStatus(request);

        verify(selectedItemEntityRepository).save(selectedItemEntity);
        assertEquals(preparedStatus, selectedItemEntity.getStatusOfPreparation());
    }

    @Test
    void givenValidSelectedItem_whenPreparedStatusDoesNotExist_thenThrowOperationNotPossible() {
        when(selectedItemEntityRepository.findById(1L)).thenReturn(Optional.of(selectedItemEntity));
        when(selectedItemStatusOfPreparationEntityRepository.findByStatusName("PREPARED"))
                .thenReturn(Optional.empty());

        OperationNotPossible exception = assertThrows(OperationNotPossible.class, () -> {
            updateSelectedItemStatusImpl.updateSelectedItemStatus(request);
        });
        assertEquals("SELECTED STATUS NOT FOUND", exception.getReason());
    }

    @Test
    void givenInvalidSelectedItem_whenNotFound_thenThrowNotFoundException() {
        when(selectedItemEntityRepository.findById(1L)).thenReturn(Optional.empty());

        NotFound exception = assertThrows(NotFound.class, () -> {
            updateSelectedItemStatusImpl.updateSelectedItemStatus(request);
        });
        assertEquals("SELECTED ITEM NOT FOUND", exception.getReason());
    }
}