package nl.fontys.s3.dinemasterbackend.business.converters;

import nl.fontys.s3.dinemasterpro.domain.classes.PizzaSize;
import nl.fontys.s3.dinemasterpro.persistence.entity.PizzaSizeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PizzaSizesConverterTest {

    @InjectMocks
    PizzaSizesConverter pizzaSizesConverter;

    PizzaSizeEntity pizzaSizeEntity;
    PizzaSize pizzaSizeBase;

    @BeforeEach
    void setUp() {
        pizzaSizeEntity = PizzaSizeEntity.builder().size("small").additionalPrice(1.0).build();
        pizzaSizeBase = PizzaSize.builder().size("small").additionalPrice(1.0).build();

    }

    @Test
    void convertEntityToNormal() {
        PizzaSize convertedSize = pizzaSizesConverter.convertEntityToNormal(pizzaSizeEntity);
        assertEquals(pizzaSizeBase, convertedSize);
    }

    @Test
    void convertNormalToEntity() {
        PizzaSizeEntity convertedSize = pizzaSizesConverter.convertNormalToEntity(pizzaSizeBase);
        assertEquals(pizzaSizeEntity, convertedSize);
    }
}