package nl.fontys.s3.dinemasterbackend.business.converters;

import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.domain.classes.*;
import nl.fontys.s3.dinemasterbackend.persistence.entity.*;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ItemConverter {
    private final PizzaSizesConverter pizzaSizesConverter;
    private final ItemCategoryConverter itemCategoryConverter;

    public Item convertEntityToNormal(ItemEntity entity) {
        if (entity instanceof PizzaEntity pizzaEntity) {
            return Pizza.builder()
                    .itemId(pizzaEntity.getItemId())
                    .itemName(pizzaEntity.getItemName())
                    .itemImageVersion(pizzaEntity.getItemImageVersion())
                    .itemImageUrl(pizzaEntity.getItemImageUrl())
                    .itemPrice(pizzaEntity.getItemPrice())
                    .ingredients(pizzaEntity.getIngredients())
                    .visibleInMenu(entity.getVisibleInMenu())
                    .sizes(pizzaEntity.getSizes()
                            .stream()
                            .map(pizzaSizesConverter::convertEntityToNormal).toList())
                    .base(pizzaEntity.getBase())
                    .itemCategory(itemCategoryConverter.convertEntityToNormal(entity.getItemCategory()))
                    .build();
        } else if (entity instanceof AppetizerEntity appetizerEntity) {
            return Appetizer.builder()
                    .itemId(appetizerEntity.getItemId())
                    .itemName(appetizerEntity.getItemName())
                    .itemImageVersion(appetizerEntity.getItemImageVersion())
                    .itemImageUrl(appetizerEntity.getItemImageUrl())
                    .itemPrice(appetizerEntity.getItemPrice())
                    .ingredients(appetizerEntity.getIngredients())
                    .visibleInMenu(entity.getVisibleInMenu())
                    .isVegetarian(appetizerEntity.getIsVegetarian())
                    .itemCategory(itemCategoryConverter.convertEntityToNormal(entity.getItemCategory()))
                    .build();
        } else if (entity instanceof BeverageEntity beverageEntity) {
            return Beverage.builder()
                    .itemId(beverageEntity.getItemId())
                    .itemName(beverageEntity.getItemName())
                    .itemImageVersion(beverageEntity.getItemImageVersion())
                    .itemImageUrl(beverageEntity.getItemImageUrl())
                    .itemPrice(beverageEntity.getItemPrice())
                    .ingredients(beverageEntity.getIngredients())
                    .visibleInMenu(entity.getVisibleInMenu())
                    .size(beverageEntity.getSize())
                    .itemCategory(itemCategoryConverter.convertEntityToNormal(entity.getItemCategory()))
                    .build();
        } else if (entity instanceof BurgerEntity burgerEntity) {
            return Burger.builder()
                    .itemId(burgerEntity.getItemId())
                    .itemName(burgerEntity.getItemName())
                    .itemImageVersion(burgerEntity.getItemImageVersion())
                    .itemImageUrl(burgerEntity.getItemImageUrl())
                    .itemPrice(burgerEntity.getItemPrice())
                    .ingredients(burgerEntity.getIngredients())
                    .visibleInMenu(entity.getVisibleInMenu())
                    .weight(burgerEntity.getWeight())
                    .itemCategory(itemCategoryConverter.convertEntityToNormal(entity.getItemCategory()))
                    .build();
        } else if (entity instanceof GrillEntity grillEntity) {
            return Grill.builder()
                    .itemId(grillEntity.getItemId())
                    .itemName(grillEntity.getItemName())
                    .itemImageVersion(grillEntity.getItemImageVersion())
                    .itemImageUrl(grillEntity.getItemImageUrl())
                    .itemPrice(grillEntity.getItemPrice())
                    .ingredients(grillEntity.getIngredients())
                    .visibleInMenu(entity.getVisibleInMenu())
                    .weight(grillEntity.getWeight())
                    .itemCategory(itemCategoryConverter.convertEntityToNormal(entity.getItemCategory()))
                    .build();
        } else if (entity instanceof PastaEntity pastaEntity) {
            return Pasta.builder()
                    .itemId(pastaEntity.getItemId())
                    .itemName(pastaEntity.getItemName())
                    .itemImageVersion(pastaEntity.getItemImageVersion())
                    .itemImageUrl(pastaEntity.getItemImageUrl())
                    .itemPrice(pastaEntity.getItemPrice())
                    .ingredients(pastaEntity.getIngredients())
                    .visibleInMenu(entity.getVisibleInMenu())
                    .pastaType(pastaEntity.getPastaType())
                    .weight(pastaEntity.getWeight())
                    .itemCategory(itemCategoryConverter.convertEntityToNormal(entity.getItemCategory()))
                    .build();
        } else if (entity instanceof SaladEntity saladEntity) {
            return Salad.builder()
                    .itemId(saladEntity.getItemId())
                    .itemName(saladEntity.getItemName())
                    .itemImageVersion(saladEntity.getItemImageVersion())
                    .itemImageUrl(saladEntity.getItemImageUrl())
                    .itemPrice(saladEntity.getItemPrice())
                    .ingredients(saladEntity.getIngredients())
                    .visibleInMenu(entity.getVisibleInMenu())
                    .weight(saladEntity.getWeight())
                    .itemCategory(itemCategoryConverter.convertEntityToNormal(entity.getItemCategory()))
                    .build();
        } else if (entity instanceof SoupEntity soupEntity) {
            return Soup.builder()
                    .itemId(soupEntity.getItemId())
                    .itemName(soupEntity.getItemName())
                    .itemImageVersion(soupEntity.getItemImageVersion())
                    .itemImageUrl(soupEntity.getItemImageUrl())
                    .itemPrice(soupEntity.getItemPrice())
                    .ingredients(soupEntity.getIngredients())
                    .visibleInMenu(entity.getVisibleInMenu())
                    .isVegetarian(soupEntity.getIsVegetarian())
                    .itemCategory(itemCategoryConverter.convertEntityToNormal(entity.getItemCategory()))
                    .build();
        }
        return null;
    }

    public ItemEntity convertNormalToEntity(Item item) {
        if (item instanceof Pizza pizza) {
            PizzaEntity pizzaEntity = PizzaEntity.builder()
                    .itemId(pizza.getItemId())
                    .itemName(pizza.getItemName())
                    .itemImageVersion(pizza.getItemImageVersion())
                    .itemImageUrl(pizza.getItemImageUrl())
                    .itemPrice(pizza.getItemPrice())
                    .ingredients(pizza.getIngredients())
                    .visibleInMenu(pizza.getVisibleInMenu())
                    .sizes(pizza.getSizes()
                            .stream()
                            .map(pizzaSizesConverter::convertNormalToEntity).toList())
                    .base(pizza.getBase())
                    .itemCategory(itemCategoryConverter.convertNormalToEntity(item.getItemCategory()))
                    .build();
            for (PizzaSizeEntity size : pizzaEntity.getSizes()) {
                size.setPizza(pizzaEntity);
            }
            return pizzaEntity;
        } else if (item instanceof Appetizer appetizer) {
            return AppetizerEntity.builder()
                    .itemId(appetizer.getItemId())
                    .itemName(appetizer.getItemName())
                    .itemImageVersion(appetizer.getItemImageVersion())
                    .itemImageUrl(appetizer.getItemImageUrl())
                    .itemPrice(appetizer.getItemPrice())
                    .ingredients(appetizer.getIngredients())
                    .visibleInMenu(appetizer.getVisibleInMenu())
                    .isVegetarian(appetizer.getIsVegetarian())
                    .itemCategory(itemCategoryConverter.convertNormalToEntity(item.getItemCategory()))
                    .build();
        } else if (item instanceof Beverage beverage) {
            return BeverageEntity.builder()
                    .itemId(beverage.getItemId())
                    .itemName(beverage.getItemName())
                    .itemImageVersion(beverage.getItemImageVersion())
                    .itemImageUrl(beverage.getItemImageUrl())
                    .itemPrice(beverage.getItemPrice())
                    .ingredients(beverage.getIngredients())
                    .visibleInMenu(beverage.getVisibleInMenu())
                    .size(beverage.getSize())
                    .itemCategory(itemCategoryConverter.convertNormalToEntity(item.getItemCategory()))
                    .build();
        } else if (item instanceof Burger burger) {
            return BurgerEntity.builder()
                    .itemId(burger.getItemId())
                    .itemName(burger.getItemName())
                    .itemImageVersion(burger.getItemImageVersion())
                    .itemImageUrl(burger.getItemImageUrl())
                    .itemPrice(burger.getItemPrice())
                    .ingredients(burger.getIngredients())
                    .visibleInMenu(burger.getVisibleInMenu())
                    .weight(burger.getWeight())
                    .itemCategory(itemCategoryConverter.convertNormalToEntity(item.getItemCategory()))
                    .build();
        } else if (item instanceof Grill grill) {
            return GrillEntity.builder()
                    .itemId(grill.getItemId())
                    .itemName(grill.getItemName())
                    .itemImageVersion(grill.getItemImageVersion())
                    .itemImageUrl(grill.getItemImageUrl())
                    .itemPrice(grill.getItemPrice())
                    .ingredients(grill.getIngredients())
                    .visibleInMenu(grill.getVisibleInMenu())
                    .weight(grill.getWeight())
                    .itemCategory(itemCategoryConverter.convertNormalToEntity(item.getItemCategory()))
                    .build();
        } else if (item instanceof Pasta pasta) {
            return PastaEntity.builder()
                    .itemId(pasta.getItemId())
                    .itemName(pasta.getItemName())
                    .itemImageVersion(pasta.getItemImageVersion())
                    .itemImageUrl(pasta.getItemImageUrl())
                    .itemPrice(pasta.getItemPrice())
                    .ingredients(pasta.getIngredients())
                    .visibleInMenu(pasta.getVisibleInMenu())
                    .pastaType(pasta.getPastaType())
                    .weight(pasta.getWeight())
                    .itemCategory(itemCategoryConverter.convertNormalToEntity(item.getItemCategory()))
                    .build();
        } else if (item instanceof Salad salad) {
            return SaladEntity.builder()
                    .itemId(salad.getItemId())
                    .itemName(salad.getItemName())
                    .itemImageVersion(salad.getItemImageVersion())
                    .itemImageUrl(salad.getItemImageUrl())
                    .itemPrice(salad.getItemPrice())
                    .ingredients(salad.getIngredients())
                    .visibleInMenu(salad.getVisibleInMenu())
                    .weight(salad.getWeight())
                    .itemCategory(itemCategoryConverter.convertNormalToEntity(item.getItemCategory()))
                    .build();
        } else if (item instanceof Soup soup) {
            return SoupEntity.builder()
                    .itemId(soup.getItemId())
                    .itemName(soup.getItemName())
                    .itemImageVersion(soup.getItemImageVersion())
                    .itemImageUrl(soup.getItemImageUrl())
                    .itemPrice(soup.getItemPrice())
                    .ingredients(soup.getIngredients())
                    .visibleInMenu(soup.getVisibleInMenu())
                    .isVegetarian(soup.getIsVegetarian())
                    .itemCategory(itemCategoryConverter.convertNormalToEntity(item.getItemCategory()))
                    .build();
        }
        return null;
    }
}
