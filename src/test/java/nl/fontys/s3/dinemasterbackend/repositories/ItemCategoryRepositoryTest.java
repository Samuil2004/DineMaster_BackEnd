package nl.fontys.s3.dinemasterbackend.repositories;

import jakarta.persistence.EntityManager;
import nl.fontys.s3.dinemasterbackend.persistence.entity.ItemCategoryEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.ItemCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class ItemCategoryRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ItemCategoryRepository itemCategoryRepository;

    ItemCategoryEntity itemCategory;

    @BeforeEach
    void beforeEach() {
        //When I persist an object to the database (entityManager.persist) it automatically detaches it and then I can not use it in other objects
        //which I will later persist. Hence, I have to use .merge which saves the object and does not detach it and returns the saved object
        itemCategory = ItemCategoryEntity.builder().categoryId(1L).categoryName("pizza").build();
        itemCategory = entityManager.merge(itemCategory);
    }

        @Test
    void findByCategoryName() {
        Optional<ItemCategoryEntity> foundCategory = itemCategoryRepository.findByCategoryName("pizza");
        assertEquals(foundCategory.get(),itemCategory);
    }
}