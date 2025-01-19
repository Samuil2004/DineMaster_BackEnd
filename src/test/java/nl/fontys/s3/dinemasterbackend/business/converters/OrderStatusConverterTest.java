package nl.fontys.s3.dinemasterbackend.business.converters;

import nl.fontys.s3.dinemasterbackend.domain.classes.OrderStatus;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderStatusEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class OrderStatusConverterTest {

    @InjectMocks
    OrderStatusConverter orderStatusConverter;

    @Test
    void convertEntityToNormal_shouldConvertCorrectly() {
        OrderStatusEntity entity = OrderStatusEntity.builder()
                .id(1L)
                .statusName("DELIVERED")
                .build();

        OrderStatus orderStatus = orderStatusConverter.convertEntityToNormal(entity);

        assertNotNull(orderStatus);
        assertEquals(1L, orderStatus.getId());
        assertEquals("DELIVERED", orderStatus.getStatusName());
    }

    @Test
    void convertNormalToEntity_shouldConvertCorrectly() {
        OrderStatus orderStatus = OrderStatus.builder()
                .id(2L)
                .statusName("PROCESSING")
                .build();

        OrderStatusEntity entity = orderStatusConverter.convertNormalToEntity(orderStatus);

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("PROCESSING", entity.getStatusName());
    }
}