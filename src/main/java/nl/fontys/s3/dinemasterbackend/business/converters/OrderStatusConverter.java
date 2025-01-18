package nl.fontys.s3.dinemasterbackend.business.converters;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.domain.classes.OrderStatus;
import nl.fontys.s3.dinemasterbackend.persistence.entity.OrderStatusEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderStatusConverter {
    public OrderStatus convertEntityToNormal(OrderStatusEntity entity)
    {
        return OrderStatus.builder()
                .id(entity.getId())
                .statusName(entity.getStatusName())
                .build();
    }
    public OrderStatusEntity convertNormalToEntity (OrderStatus orderStatus)
    {
        return OrderStatusEntity.builder()
                .id(orderStatus.getId())
                .statusName(orderStatus.getStatusName())
                .build();
    }
}
