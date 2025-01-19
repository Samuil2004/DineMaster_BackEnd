package nl.fontys.s3.dinemasterbackend.repositories;

import jakarta.persistence.EntityManager;
import nl.fontys.s3.dinemasterbackend.persistence.entity.ItemCategoryEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.PizzaEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.PizzaSizeEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.ItemEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class ItemEntityRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ItemEntityRepository itemEntityRepository;

    ItemCategoryEntity itemCategory;
    PizzaEntity peperoniEntity;
    PizzaEntity marinaraEntity;
    PizzaSizeEntity pizzaSizeEntity1;
    PizzaSizeEntity pizzaSizeEntity2;
    PizzaSizeEntity pizzaSizeEntity3;
    PizzaSizeEntity pizzaSizeEntity4;
    PizzaSizeEntity pizzaSizeEntity5;
    PizzaSizeEntity pizzaSizeEntity6;
    @BeforeEach
    void beforeEach() {
        //When I persist an object to the database (entityManager.persist) it automatically detaches it and then I can not use it in other objects
        //which I will later persist. Hence, I have to use .merge which saves the object and does not detach it and returns the saved object
        itemCategory = ItemCategoryEntity.builder().categoryId(1L).categoryName("pizza").build();
        itemCategory = entityManager.merge(itemCategory);

        peperoniEntity = PizzaEntity.builder()
                .itemId(1L)
                .itemName("Peperoni")
                .itemImageVersion("v6782736")
                .itemPrice(12.50)
                .visibleInMenu(true)
                .itemCategory(itemCategory)
                .base("Thin Crust")
                .ingredients(new ArrayList<>(List.of("dough", "sauce", "cheese", "pepperoni", "oliveOil", "seasoning", "garlicPowder")))
                .build();

        marinaraEntity = PizzaEntity.builder()
                .itemId(2L)
                .itemName("Marinara")
                .itemImageVersion("v6782736")
                .itemPrice(12.50)
                .visibleInMenu(false)
                .itemCategory(itemCategory)
                .base("Thin Crust")
                .ingredients(new ArrayList<>(List.of("dough", "sauce", "cheese", "pepperoni", "oliveOil", "seasoning", "garlicPowder")))
                .build();

        peperoniEntity = entityManager.merge(peperoniEntity);
        marinaraEntity = entityManager.merge(marinaraEntity);

        pizzaSizeEntity1 = PizzaSizeEntity.builder().id(1L).size("small").additionalPrice(1.0).pizza(peperoniEntity).build();
        pizzaSizeEntity2 = PizzaSizeEntity.builder().id(2L).size("large").additionalPrice(2.0).pizza(peperoniEntity).build();
        pizzaSizeEntity3 = PizzaSizeEntity.builder().id(3L).size("jumbo").additionalPrice(3.0).pizza(peperoniEntity).build();

        pizzaSizeEntity4 = PizzaSizeEntity.builder().id(1L).size("small").additionalPrice(1.0).pizza(marinaraEntity).build();
        pizzaSizeEntity5 = PizzaSizeEntity.builder().id(2L).size("large").additionalPrice(2.0).pizza(marinaraEntity).build();
        pizzaSizeEntity6 = PizzaSizeEntity.builder().id(3L).size("jumbo").additionalPrice(3.0).pizza(marinaraEntity).build();

        pizzaSizeEntity1 = entityManager.merge(pizzaSizeEntity1);
        pizzaSizeEntity2 = entityManager.merge(pizzaSizeEntity2);
        pizzaSizeEntity3 = entityManager.merge(pizzaSizeEntity3);

        pizzaSizeEntity4 = entityManager.merge(pizzaSizeEntity4);
        pizzaSizeEntity5 = entityManager.merge(pizzaSizeEntity5);
        pizzaSizeEntity6 = entityManager.merge(pizzaSizeEntity6);

        peperoniEntity.setSizes(new ArrayList<>(List.of(pizzaSizeEntity1, pizzaSizeEntity2, pizzaSizeEntity3)));
        marinaraEntity.setSizes(new ArrayList<>(List.of(pizzaSizeEntity4, pizzaSizeEntity5, pizzaSizeEntity6)));

        entityManager.merge(peperoniEntity);
        entityManager.merge(marinaraEntity);

    }

    @Test
    void existsByItemName() {
        boolean exists = itemEntityRepository.existsByItemName("Peperoni");
        assertTrue(exists);
    }

}