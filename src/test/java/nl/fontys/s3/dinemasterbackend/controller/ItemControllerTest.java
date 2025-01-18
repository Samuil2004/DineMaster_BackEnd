package nl.fontys.s3.dinemasterbackend.controller;

import nl.fontys.s3.dinemasterpro.business.dtos.create.items.*;
import nl.fontys.s3.dinemasterpro.business.dtos.update.items.*;
import nl.fontys.s3.dinemasterpro.domain.classes.Appetizer;
import nl.fontys.s3.dinemasterpro.domain.classes.Beverage;
import nl.fontys.s3.dinemasterpro.domain.classes.ItemCategory;
import nl.fontys.s3.dinemasterpro.persistence.entity.AppetizerEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.ItemCategoryEntity;
import nl.fontys.s3.dinemasterpro.persistence.entity.ItemEntity;
import nl.fontys.s3.dinemasterpro.persistence.repositories.ItemCategoryRepository;
import nl.fontys.s3.dinemasterpro.persistence.repositories.ItemEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemEntityRepository itemEntityRepository;

    @MockBean
    private ItemCategoryRepository itemCategoryRepository;


    Appetizer appetizerBase;
    ItemCategory itemCategoryBaseAppetizer;
    Beverage beverageBase;
    ItemCategory itemCategoryBaseBeverage;

    AppetizerEntity appetizerEntity;
    ItemCategoryEntity itemCategoryEntityAppetizer;

    @BeforeEach
    void setUp() {
        itemCategoryBaseAppetizer = ItemCategory.builder().categoryId(1L).categoryName("APPETIZER").build();
        itemCategoryEntityAppetizer = ItemCategoryEntity.builder().categoryId(1L).categoryName("APPETIZER").build();

        appetizerBase = Appetizer.builder()
                .itemId(1L)
                .itemName("Bruschetta")
                .itemImageVersion("v1730296343")
                .itemPrice(7.50)
                .ingredients(Collections.emptyList())
                .visibleInMenu(true)
                .itemCategory(itemCategoryBaseAppetizer)
                .isVegetarian(true)
                .build();


        appetizerEntity = AppetizerEntity.builder()
                .itemId(1L)
                .itemName("Bruschetta")
                .itemImageVersion("v1730296343")
                .itemPrice(10.0)
                .itemCategory(itemCategoryEntityAppetizer)
                .visibleInMenu(true)
                .isVegetarian(true)
                .build();


        itemCategoryBaseBeverage = ItemCategory.builder().categoryId(1L).categoryName("BEVERAGE").build();
        beverageBase = Beverage.builder()
                .itemId(1L)
                .itemName("Cola")
                .itemImageVersion("v1730296343")
                .itemPrice(2.50)
                .visibleInMenu(true)
                .itemCategory(itemCategoryBaseBeverage)
                .size(0.33)
                .build();

    }

    @Test
    @WithMockUser(username = "manager@fontys.nl", roles = {"MANAGER"})
    void createAppetizer_shouldReturnCreated() throws Exception {
        CreateAppetizerRequest request = new CreateAppetizerRequest();
        request.setItemName("Bruschetta");
        request.setItemPrice(10.0);
        request.setIngredients(Arrays.asList("Tomato", "Basil", "Garlic"));
        request.setVisibleInMenu(true);
        request.setIsVegetarian(true);


        ItemCategoryEntity cateogoryEntity = ItemCategoryEntity.builder().categoryId(1L).categoryName("APPETIZER").build();

        when(itemEntityRepository.existsByItemName(request.getItemName())).thenReturn(false);
        when(itemCategoryRepository.findByCategoryName("APPETIZER")).thenReturn(Optional.ofNullable(cateogoryEntity));
        when(itemEntityRepository.save(any(ItemEntity.class))).thenReturn(appetizerEntity);


        mockMvc.perform(post("/items/appetizer")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                    "itemName": "Bruschetta",
                                    "itemPrice": 10.0,
                                    "ingredients": ["Tomato", "Basil", "Garlic"],
                                    "visibleInMenu": true,
                                    "isVegetarian": true
                                }
                                """))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                            { "itemId": 1 }
                        """));

        verify(itemEntityRepository).existsByItemName(request.getItemName());
        verify(itemCategoryRepository).findByCategoryName("APPETIZER");
        verify(itemEntityRepository).save(any(ItemEntity.class));

    }

    @Test
    @WithMockUser(username = "customer@gmail.nl", roles = {"CUSTOMER"})
    void createAppetizer_shouldReturnAccessDenied_whenUserIsNotManager() throws Exception {
        CreateAppetizerRequest request = new CreateAppetizerRequest();
        request.setItemName("Bruschetta");
        request.setItemPrice(10.0);
        request.setIngredients(Arrays.asList("Tomato", "Basil", "Garlic"));
        request.setVisibleInMenu(true);
        request.setIsVegetarian(true);

        mockMvc.perform(post("/items/appetizer")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                    "itemName": "Bruschetta",
                                    "itemPrice": 10.0,
                                    "ingredients": ["Tomato", "Basil", "Garlic"],
                                    "visibleInMenu": true,
                                    "isVegetarian": true
                                }
                                """))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    void givenCategory_whenGetItems_thenReturn200WithItems() throws Exception {
        appetizerEntity.setIngredients(List.of("Bread"));
        when(itemCategoryRepository.findByCategoryName("APPETIZER")).thenReturn(Optional.ofNullable(itemCategoryEntityAppetizer));
        when(itemEntityRepository.findByItemCategory_CategoryId(1L)).thenReturn(List.of(appetizerEntity));
        mockMvc.perform(get("/items/categories/appetizer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                        {
                          "itemsInCategory": [
                            {
                              "itemId": 1,
                              "itemName": "Bruschetta",
                              "itemImageVersion": "v1730296343",
                              "itemPrice": 10.0,
                              "ingredients": ["Bread"],
                              "visibleInMenu": true,
                              "itemCategory": {
                                "categoryId": 1,
                                "categoryName": "APPETIZER"
                              },
                               "isVegetarian": true
                            }
                          ]
                        }
                        """));

        verify(itemCategoryRepository).findByCategoryName("APPETIZER");
        verify(itemEntityRepository).findByItemCategory_CategoryId(1L);
    }

    @Test
    @WithMockUser(username = "user@fontys.nl", roles = {"NOTMANAGER"})
    void deleteItemById_shouldReturnForbidden_whenUserIsNotManager() throws Exception {

        mockMvc.perform(delete("/items/{id}", appetizerBase.getItemId())
                        .param("category", "APPETIZER")
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user@fontys.nl", roles = {"NOTMANAGER"})
    void deleteItemByIdWhenCorruptedCategoryIsPassed_shouldReturnForbidden_whenUserIsNotManager() throws Exception {

        mockMvc.perform(delete("/items/{id}", appetizerBase.getItemId())
                        .param("category", " ")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "manager@fontys.nl", roles = {"MANAGER"})
    void givenAppetizerData_whenUpdatingAppetizer_shouldUpdateAppetizer() throws Exception {
        UpdateAppetizerRequest request = new UpdateAppetizerRequest();
        request.setItemId(1L);
        request.setItemName("Bruschetta");
        request.setItemImageVersion("v1730296343");
        request.setItemPrice(8.00);
        request.setIngredients(Arrays.asList("Tomato", "Basil", "Garlic"));
        request.setVisibleInMenu(true);
        request.setIsVegetarian(true);

        when(itemEntityRepository.findById(1L)).thenReturn(Optional.ofNullable(appetizerEntity));
        when(itemEntityRepository.save(any(ItemEntity.class))).thenReturn(appetizerEntity);

        mockMvc.perform(put("/items/appetizer/{id}", 1L)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                    "itemId":1,
                                    "itemName": "Bruschetta",
                                    "itemImageVersion": "v1730296343",
                                    "itemPrice": 8.00,
                                    "ingredients": ["Tomato", "Basil", "Garlic"],
                                    "visibleInMenu": true,
                                    "isVegetarian": true
                                }
                                """))
                .andExpect(status().isNoContent());

        verify(itemEntityRepository).findById(1L);
        verify(itemEntityRepository).save(any(ItemEntity.class));
    }


    @Test
    @WithMockUser(username = "manager@fontys.nl", roles = {"MANAGER"})
    void givenNegativeItemPrice_whenUpdatingItems_thenThrowsBadRequestError() throws Exception {
        UpdateAppetizerRequest request = new UpdateAppetizerRequest();
        request.setItemId(1L);
        request.setItemName("Bruschetta");
        request.setItemImageVersion("v1730296343");
        request.setItemPrice(8.00);
        request.setIngredients(Arrays.asList("Tomato", "Basil", "Garlic"));
        request.setVisibleInMenu(true);
        request.setIsVegetarian(true);

        mockMvc.perform(put("/items/appetizer/{id}", 1L)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                    "itemId":1,
                                    "itemName": "Bruschetta",
                                    "itemImageVersion": "v1730296343",
                                    "itemPrice": -8.00,
                                    "ingredients": ["Tomato", "Basil", "Garlic"],
                                    "visibleInMenu": true,
                                    "isVegetarian": true
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].field").value("itemPrice"))
                .andExpect(jsonPath("$.errors[0].error").value("Price must be at least 0.0."));
    }

}