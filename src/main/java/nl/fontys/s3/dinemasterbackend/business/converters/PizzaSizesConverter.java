package nl.fontys.s3.dinemasterbackend.business.converters;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.domain.classes.*;
import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PizzaSizesConverter {
    public PizzaSize convertEntityToNormal(PizzaSizeEntity entity)
    {
        return PizzaSize.builder()
                .pizzaSizeId(entity.getId())
                .size(entity.getSize())
                .additionalPrice(entity.getAdditionalPrice())
                .build();
    }

    public PizzaSizeEntity convertNormalToEntity(PizzaSize item)
    {
        return PizzaSizeEntity.builder()
                .id(item.getPizzaSizeId())
                .size(item.getSize())
                .additionalPrice(item.getAdditionalPrice())
                .build();
    }
}

