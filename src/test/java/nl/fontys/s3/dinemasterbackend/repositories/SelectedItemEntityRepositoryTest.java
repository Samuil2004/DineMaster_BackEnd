package nl.fontys.s3.dinemasterbackend.repositories;

import jakarta.persistence.EntityManager;
import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.SelectedItemEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class SelectedItemEntityRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private SelectedItemEntityRepository selectedItemEntityRepository;

    CartEntity cartEntity;
    SelectedItemEntity selectedItemEntity;
    SelectedItemStatusOfPreparationEntity selectedItemStatusEntity;
    AppetizerEntity appetizerEntity;
    ItemCategoryEntity itemCategoryEntityAppetizer;

    @BeforeEach
    void beforeEach() {
        //When I persist an object to the database (entityManager.persist) it automatically detaches it and then I can not use it in other objects
        //which I will later persist. Hence, I have to use .merge which saves the object and does not detach it and returns the saved object


        itemCategoryEntityAppetizer = ItemCategoryEntity.builder().categoryId(1L).categoryName("APPETIZER").build();
        itemCategoryEntityAppetizer = entityManager.merge(itemCategoryEntityAppetizer);

        selectedItemStatusEntity = new SelectedItemStatusOfPreparationEntity();
        selectedItemStatusEntity.setId(1L);
        selectedItemStatusEntity.setStatusName("PREPARING");
        selectedItemStatusEntity = entityManager.merge(selectedItemStatusEntity);

        appetizerEntity = AppetizerEntity.builder()
                .itemId(1L)
                .itemName("Bruschetta")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .itemCategory(itemCategoryEntityAppetizer)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();
        appetizerEntity = entityManager.merge(appetizerEntity);

        cartEntity = CartEntity.builder()
                .cartId(1L)
                .customerId(1L)
                .price(50.0)
                .selectedItemEntities(new ArrayList<>())
                .isActive(true)
                .build();

        selectedItemEntity = SelectedItemEntity.builder()
                .selectedItemId(1L)
                .itemOfReference(appetizerEntity)
                .amount(1)
                .cart(cartEntity)
                .itemCategory(itemCategoryEntityAppetizer)
                .comment("no comments")
                .statusOfPreparation(selectedItemStatusEntity)
                .build();
        cartEntity.setSelectedItemEntities(List.of(selectedItemEntity));

        cartEntity = entityManager.merge(cartEntity);

    }

    @Test
    void findSelectedItemsInActiveCarts() {
        List<SelectedItemEntity> foundItems = selectedItemEntityRepository.findSelectedItemsInActiveCarts(appetizerEntity);
        assertEquals(foundItems.get(0), selectedItemEntity);
    }


}