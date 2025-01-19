package nl.fontys.s3.dinemasterbackend.business.converters;

import nl.fontys.s3.dinemasterbackend.domain.classes.ItemCategory;
import nl.fontys.s3.dinemasterbackend.persistence.entity.ItemCategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ItemCategoryConverterTest {

    @InjectMocks
    ItemCategoryConverter itemCategoryConverter;

    ItemCategoryEntity itemCategoryEntityPizza;
    ItemCategory itemCategoryBasePizza;

    @BeforeEach
    void setUp() {
        itemCategoryEntityPizza = ItemCategoryEntity.builder().categoryId(1L).categoryName("PIZZA").build();
        itemCategoryBasePizza = ItemCategory.builder().categoryId(1L).categoryName("PIZZA").build();
    }

    @Test
    void convertEntityToNormal() {
        ItemCategory itemCategory = itemCategoryConverter.convertEntityToNormal(itemCategoryEntityPizza);
        assertEquals(itemCategory,itemCategoryBasePizza);
    }

    @Test
    void convertNormalToEntity() {
        ItemCategoryEntity itemCategory = itemCategoryConverter.convertNormalToEntity(itemCategoryBasePizza);
        assertEquals(itemCategory,itemCategoryEntityPizza);
    }
}