package nl.fontys.s3.dinemasterbackend.business.converters;

import nl.fontys.s3.dinemasterpro.domain.classes.SelectedItemStatusOfPreparation;
import nl.fontys.s3.dinemasterpro.persistence.entity.SelectedItemStatusOfPreparationEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SelectedItemStatusOfPreparationConverterTest {
    @InjectMocks
    private SelectedItemStatusOfPreparationConverter converter;

    @Test
    void convertEntityToNormal_shouldConvertCorrectly() {

        SelectedItemStatusOfPreparationEntity entity = SelectedItemStatusOfPreparationEntity.builder()
                .id(1L)
                .statusName("In Progress")
                .build();

        SelectedItemStatusOfPreparation result = converter.convertEntityToNormal(entity);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("In Progress", result.getStatusName());
    }

    @Test
    void convertNormalToEntity_shouldConvertCorrectly() {
        SelectedItemStatusOfPreparation status = SelectedItemStatusOfPreparation.builder()
                .id(2L)
                .statusName("Ready")
                .build();

        SelectedItemStatusOfPreparationEntity result = converter.convertNormalToEntity(status);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Ready", result.getStatusName());
    }
}