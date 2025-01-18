package nl.fontys.s3.dinemasterbackend.business.converters;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.domain.classes.ItemCategory;
import nl.fontys.s3.dinemasterbackend.persistence.entity.ItemCategoryEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ItemCategoryConverter {
    public ItemCategory convertEntityToNormal(ItemCategoryEntity entity)
    {

        return ItemCategory.builder()
                .categoryId(entity.getCategoryId())
                .categoryName(entity.getCategoryName())
                .build();
    }
    public ItemCategoryEntity convertNormalToEntity (ItemCategory itemCategory)
    {
        return ItemCategoryEntity.builder()
                .categoryId(itemCategory.getCategoryId())
                .categoryName(itemCategory.getCategoryName())
                .build();
    }
}

