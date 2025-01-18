package nl.fontys.s3.dinemasterbackend.repositories;

import jakarta.persistence.EntityManager;
import nl.fontys.s3.dinemasterpro.persistence.entity.SelectedItemStatusOfPreparationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class SelectedItemStatusOfPreparationEntityRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SelectedItemStatusOfPreparationEntityRepository selectedItemStatusOfPreparationEntityRepository;

    SelectedItemStatusOfPreparationEntity selectedItemStatusEntity;

    @BeforeEach
    void beforeEach() {
        selectedItemStatusEntity = new SelectedItemStatusOfPreparationEntity();
        selectedItemStatusEntity.setId(1L);
        selectedItemStatusEntity.setStatusName("PREPARING");

        selectedItemStatusEntity = entityManager.merge(selectedItemStatusEntity);
    }

    @Test
    void findSelectedItemsInActiveCarts_returnsFoundStatus_whenStatusNameExists() {
        Optional<SelectedItemStatusOfPreparationEntity> foundStatus = selectedItemStatusOfPreparationEntityRepository.findByStatusName(selectedItemStatusEntity.getStatusName());
        assertEquals(foundStatus.get(), selectedItemStatusEntity);
    }

    @Test
    void findSelectedItemsInActiveCarts_returnsEmptyObject_whenStatusNameDoesNotExist() {
        Optional<SelectedItemStatusOfPreparationEntity> foundStatus = selectedItemStatusOfPreparationEntityRepository.findByStatusName("Non existing status name");
        assertTrue(foundStatus.isEmpty());
    }
}