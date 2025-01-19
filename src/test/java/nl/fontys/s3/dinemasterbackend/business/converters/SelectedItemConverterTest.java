package nl.fontys.s3.dinemasterbackend.business.converters;

import nl.fontys.s3.dinemasterbackend.domain.classes.*;
import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class SelectedItemConverterTest {
    @InjectMocks
    private SelectedItemConverter selectedItemConverter;
    @Mock
    private ItemConverter itemConverter;
    @Mock
    private PizzaSizesConverter pizzaSizesConverter;
    @Mock
    private ItemCategoryConverter itemCategoryConverter;
    @Mock
    private SelectedItemStatusOfPreparationConverter selectedItemStatusOfPreparationConverter;

    @Test
    void convertEntityToNormal_shouldConvertSelectedPizzaEntityCorrectly() {
        SelectedPizzaEntity pizzaEntity = SelectedPizzaEntity.builder()
                .selectedItemId(1L)
                .itemName("Pepperoni Pizza")
                .amount(2)
                .itemImageVersion("v12345")
                .itemPrice(15.99)
                .sizes(mock(PizzaSizeEntity.class))
                .base("Thin Crust")
                .comment("Extra Cheese")
                .itemCategory(mock(ItemCategoryEntity.class))
                .statusOfPreparation(mock(SelectedItemStatusOfPreparationEntity.class))
                .itemOfReference(mock(ItemEntity.class))
                .build();

        when(itemConverter.convertEntityToNormal(pizzaEntity.getItemOfReference())).thenReturn(mock(Item.class));
        when(pizzaSizesConverter.convertEntityToNormal(pizzaEntity.getSizes())).thenReturn(mock(PizzaSize.class));
        when(itemCategoryConverter.convertEntityToNormal(pizzaEntity.getItemCategory())).thenReturn(mock(ItemCategory.class));
        when(selectedItemStatusOfPreparationConverter.convertEntityToNormal(pizzaEntity.getStatusOfPreparation())).thenReturn(mock(SelectedItemStatusOfPreparation.class));

        SelectedItem result = selectedItemConverter.convertEntityToNormal(pizzaEntity);

        assertNotNull(result);
        assertTrue(result instanceof SelectedPizza);
        SelectedPizza pizza = (SelectedPizza) result;
        assertEquals(1L, pizza.getSelectedItemId());
        assertEquals("Pepperoni Pizza", pizza.getItemName());
        assertEquals(2, pizza.getAmount());
        assertEquals("v12345", pizza.getItemImageVersion());
        assertEquals(15.99, pizza.getItemPrice());
        assertEquals("Thin Crust", pizza.getBase());
        assertEquals("Extra Cheese", pizza.getComment());
        verify(itemConverter).convertEntityToNormal(pizzaEntity.getItemOfReference());
        verify(pizzaSizesConverter).convertEntityToNormal(pizzaEntity.getSizes());
        verify(itemCategoryConverter).convertEntityToNormal(pizzaEntity.getItemCategory());
        verify(selectedItemStatusOfPreparationConverter).convertEntityToNormal(pizzaEntity.getStatusOfPreparation());
    }

    @Test
    void convertEntityToNormal_shouldConvertSelectedItemDifferentFromPizzaEntityCorrectly() {
        SelectedItemDifferentFromPizzaEntity otherEntity = SelectedItemDifferentFromPizzaEntity.builder()
                .selectedItemId(2L)
                .amount(1)
                .comment("No onions")
                .itemCategory(mock(ItemCategoryEntity.class))
                .statusOfPreparation(mock(SelectedItemStatusOfPreparationEntity.class))
                .itemOfReference(mock(ItemEntity.class))
                .itemFromMenu(mock(ItemEntity.class))
                .build();

        when(itemConverter.convertEntityToNormal(otherEntity.getItemOfReference())).thenReturn(mock(Item.class));
        when(itemConverter.convertEntityToNormal(otherEntity.getItemFromMenu())).thenReturn(mock(Item.class));
        when(itemCategoryConverter.convertEntityToNormal(otherEntity.getItemCategory())).thenReturn(mock(ItemCategory.class));
        when(selectedItemStatusOfPreparationConverter.convertEntityToNormal(otherEntity.getStatusOfPreparation())).thenReturn(mock(SelectedItemStatusOfPreparation.class));

        SelectedItem result = selectedItemConverter.convertEntityToNormal(otherEntity);

        assertNotNull(result);
        assertTrue(result instanceof SelectedItemDifferentFromPizza);
        SelectedItemDifferentFromPizza other = (SelectedItemDifferentFromPizza) result;
        assertEquals(2L, other.getSelectedItemId());
        assertEquals(1, other.getAmount());
        assertEquals("No onions", other.getComment());
        verify(itemConverter).convertEntityToNormal(otherEntity.getItemOfReference());
        verify(itemConverter).convertEntityToNormal(otherEntity.getItemFromMenu());
        verify(itemCategoryConverter).convertEntityToNormal(otherEntity.getItemCategory());
        verify(selectedItemStatusOfPreparationConverter).convertEntityToNormal(otherEntity.getStatusOfPreparation());
    }

    @Test
    void convertNormalToEntity_shouldConvertSelectedPizzaCorrectly() {
        SelectedPizza pizza = SelectedPizza.builder()
                .selectedItemId(1L)
                .itemName("Veggie Pizza")
                .amount(3)
                .itemImageVersion("v56789")
                .itemPrice(12.50)
                .sizes(mock(PizzaSize.class))
                .base("Stuffed Crust")
                .comment("Gluten-Free")
                .itemCategory(mock(ItemCategory.class))
                .statusOfPreparation(mock(SelectedItemStatusOfPreparation.class))
                .itemOfReference(mock(Item.class))
                .build();

        when(itemConverter.convertNormalToEntity(pizza.getItemOfReference())).thenReturn(mock(ItemEntity.class));
        when(pizzaSizesConverter.convertNormalToEntity(pizza.getSizes())).thenReturn(mock(PizzaSizeEntity.class));
        when(itemCategoryConverter.convertNormalToEntity(pizza.getItemCategory())).thenReturn(mock(ItemCategoryEntity.class));
        when(selectedItemStatusOfPreparationConverter.convertNormalToEntity(pizza.getStatusOfPreparation())).thenReturn(mock(SelectedItemStatusOfPreparationEntity.class));

        SelectedItemEntity result = selectedItemConverter.convertNormalToEntity(pizza);

        assertNotNull(result);
        assertTrue(result instanceof SelectedPizzaEntity);
        SelectedPizzaEntity pizzaEntity = (SelectedPizzaEntity) result;
        assertEquals(1L, pizzaEntity.getSelectedItemId());
        assertEquals("Veggie Pizza", pizzaEntity.getItemName());
        assertEquals(3, pizzaEntity.getAmount());
        assertEquals("v56789", pizzaEntity.getItemImageVersion());
        assertEquals(12.50, pizzaEntity.getItemPrice());
        assertEquals("Stuffed Crust", pizzaEntity.getBase());
        assertEquals("Gluten-Free", pizzaEntity.getComment());
        verify(itemConverter).convertNormalToEntity(pizza.getItemOfReference());
        verify(pizzaSizesConverter).convertNormalToEntity(pizza.getSizes());
        verify(itemCategoryConverter).convertNormalToEntity(pizza.getItemCategory());
        verify(selectedItemStatusOfPreparationConverter).convertNormalToEntity(pizza.getStatusOfPreparation());
    }

    @Test
    void convertNormalToEntity_shouldConvertSelectedItemDifferentFromPizzaCorrectly() {
        // Arrange
        SelectedItemDifferentFromPizza other = SelectedItemDifferentFromPizza.builder()
                .selectedItemId(3L)
                .amount(2)
                .comment("No garlic")
                .itemCategory(mock(ItemCategory.class))
                .statusOfPreparation(mock(SelectedItemStatusOfPreparation.class))
                .itemOfReference(mock(Item.class))
                .itemFromMenu(mock(Item.class))
                .build();

        when(itemConverter.convertNormalToEntity(other.getItemOfReference())).thenReturn(mock(ItemEntity.class));
        when(itemConverter.convertNormalToEntity(other.getItemFromMenu())).thenReturn(mock(ItemEntity.class));
        when(itemCategoryConverter.convertNormalToEntity(other.getItemCategory())).thenReturn(mock(ItemCategoryEntity.class));
        when(selectedItemStatusOfPreparationConverter.convertNormalToEntity(other.getStatusOfPreparation())).thenReturn(mock(SelectedItemStatusOfPreparationEntity.class));

        SelectedItemEntity result = selectedItemConverter.convertNormalToEntity(other);

        assertNotNull(result);
        assertTrue(result instanceof SelectedItemDifferentFromPizzaEntity);
        SelectedItemDifferentFromPizzaEntity otherEntity = (SelectedItemDifferentFromPizzaEntity) result;
        assertEquals(3L, otherEntity.getSelectedItemId());
        assertEquals(2, otherEntity.getAmount());
        assertEquals("No garlic", otherEntity.getComment());
        verify(itemConverter).convertNormalToEntity(other.getItemOfReference());
        verify(itemConverter).convertNormalToEntity(other.getItemFromMenu());
        verify(itemCategoryConverter).convertNormalToEntity(other.getItemCategory());
        verify(selectedItemStatusOfPreparationConverter).convertNormalToEntity(other.getStatusOfPreparation());
    }

    @Test
    void convertEntityToNormal_shouldReturnNullForUnsupportedEntity() {
        SelectedItemEntity unsupportedEntity = mock(SelectedItemEntity.class);

        SelectedItem result = selectedItemConverter.convertEntityToNormal(unsupportedEntity);

        assertNull(result);
    }

    @Test
    void convertNormalToEntity_shouldReturnNullForUnsupportedItem() {
        SelectedItem unsupportedItem = mock(SelectedItem.class);

        SelectedItemEntity result = selectedItemConverter.convertNormalToEntity(unsupportedItem);

        assertNull(result);
    }
}