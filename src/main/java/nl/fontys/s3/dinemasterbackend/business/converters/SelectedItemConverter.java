package nl.fontys.s3.dinemasterbackend.business.converters;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.domain.classes.*;
import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SelectedItemConverter {
    private final ItemConverter itemConverter;
    private final PizzaSizesConverter pizzaSizesConverter;
    private final ItemCategoryConverter itemCategoryConverter;
    private final SelectedItemStatusOfPreparationConverter selectedItemStatusOfPreparationConverter;

    public SelectedItem convertEntityToNormal(SelectedItemEntity entity) {
        if (entity instanceof SelectedPizzaEntity pizza) {
            return SelectedPizza.builder()
                    .itemOfReference(itemConverter.convertEntityToNormal(entity.getItemOfReference()))
                    .selectedItemId(pizza.getSelectedItemId())
                    .amount(pizza.getAmount())
                    .itemName(pizza.getItemName())
                    .itemImageVersion(pizza.getItemImageVersion())
                    .itemPrice(pizza.getItemPrice())
                    .sizes(pizzaSizesConverter.convertEntityToNormal(pizza.getSizes()))
                    .base(pizza.getBase())
                    .comment(pizza.getComment())
                    .itemCategory(itemCategoryConverter.convertEntityToNormal(entity.getItemCategory()))
                    .statusOfPreparation(selectedItemStatusOfPreparationConverter.convertEntityToNormal(entity.getStatusOfPreparation()))
                    .build();
        } else if (entity instanceof SelectedItemDifferentFromPizzaEntity otherThanPizza) {
            return SelectedItemDifferentFromPizza.builder()
                    .itemOfReference(itemConverter.convertEntityToNormal(entity.getItemOfReference()))
                    .selectedItemId(otherThanPizza.getSelectedItemId())
                    .itemFromMenu(itemConverter.convertEntityToNormal(otherThanPizza.getItemFromMenu()))
                    .amount(entity.getAmount())
                    .comment(entity.getComment())
                    .itemCategory(itemCategoryConverter.convertEntityToNormal(entity.getItemCategory()))
                    .statusOfPreparation(selectedItemStatusOfPreparationConverter.convertEntityToNormal(entity.getStatusOfPreparation()))
                    .build();
        }
        return null;
    }

    public SelectedItemEntity convertNormalToEntity(SelectedItem item) {
        if (item instanceof SelectedPizza pizza) {
            return SelectedPizzaEntity.builder()
                    .itemOfReference(itemConverter.convertNormalToEntity(item.getItemOfReference()))
                    .selectedItemId(pizza.getSelectedItemId())
                    .itemName(pizza.getItemName())
                    .amount(pizza.getAmount())
                    .itemImageVersion(pizza.getItemImageVersion())
                    .itemPrice(pizza.getItemPrice())
                    .sizes(pizzaSizesConverter.convertNormalToEntity(pizza.getSizes()))
                    .base(pizza.getBase())
                    .comment(pizza.getComment())
                    .itemCategory(itemCategoryConverter.convertNormalToEntity(item.getItemCategory()))
                    .statusOfPreparation(selectedItemStatusOfPreparationConverter.convertNormalToEntity(item.getStatusOfPreparation()))
                    .build();
        } else if (item instanceof SelectedItemDifferentFromPizza otherThanPizza) {
            return nl.fontys.s3.dinemasterbackend.persistence.entity.SelectedItemDifferentFromPizzaEntity.builder()
                    .itemOfReference(itemConverter.convertNormalToEntity(otherThanPizza.getItemOfReference()))
                    .selectedItemId(otherThanPizza.getSelectedItemId())
                    .itemFromMenu(itemConverter.convertNormalToEntity(otherThanPizza.getItemFromMenu()))
                    .amount(otherThanPizza.getAmount())
                    .comment(otherThanPizza.getComment())
                    .itemCategory(itemCategoryConverter.convertNormalToEntity(item.getItemCategory()))
                    .statusOfPreparation(selectedItemStatusOfPreparationConverter.convertNormalToEntity(item.getStatusOfPreparation()))
                    .build();
        }
        return null;
    }

}
