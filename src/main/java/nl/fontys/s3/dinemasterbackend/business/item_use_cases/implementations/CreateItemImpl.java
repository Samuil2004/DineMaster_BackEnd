package nl.fontys.s3.dinemasterbackend.business.item_use_cases.implementations;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.ItemCategoryConverter;
import nl.fontys.s3.dinemasterbackend.business.converters.ItemConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.create.items.*;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.exceptions.OperationNotPossible;
import nl.fontys.s3.dinemasterbackend.business.item_use_cases.CreateItem;
import nl.fontys.s3.dinemasterbackend.domain.classes.*;
import nl.fontys.s3.dinemasterbackend.persistence.entity.ItemCategoryEntity;
import nl.fontys.s3.dinemasterbackend.persistence.entity.ItemEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.ItemCategoryRepository;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.ItemEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateItemImpl implements CreateItem {
    private final ItemEntityRepository itemRepository;
    private final ItemCategoryRepository itemCategoryRepository;

    private final ItemConverter itemConverter;
    private final ItemCategoryConverter itemCategoryConverter;

    @Override
    @Transactional
    public CreateItemResponse createItem(CreateItemRequest itemRequest)
    {
        if(!itemRepository.existsByItemName(itemRequest.getItemName())) {
            Item newItem = saveItem(itemRequest);
            return CreateItemResponse.builder().itemId(newItem.getItemId()).build();
        }
        throw new OperationNotPossible("Item with provided name already exists.");
    }

    private ItemCategory findCategory(String categoryName) {
        Optional<ItemCategoryEntity> foundCategory = itemCategoryRepository.findByCategoryName(categoryName);
        if(foundCategory.isPresent()) {
            return itemCategoryConverter.convertEntityToNormal(foundCategory.get());
        }
        throw new NotFound("ITEM_CATEGORY_NOT_FOUND");
    }

    private Item saveItem(CreateItemRequest itemRequest)
    {
        String initialImageVersionNaming = "v1730296343/items/errorImage";
        if(itemRequest instanceof CreatePizzaRequest pizzaRequest) {
            Pizza newPizza = Pizza.builder()
                    .itemName(pizzaRequest.getItemName())
                    .itemImageVersion(initialImageVersionNaming)
                    .itemPrice(pizzaRequest.getItemPrice())
                    .ingredients(pizzaRequest.getIngredients())
                    .visibleInMenu(pizzaRequest.getVisibleInMenu())
                    .sizes(pizzaRequest.getSizes())
                    .base(pizzaRequest.getBase())
                    .itemCategory(findCategory("PIZZA"))
                    .build();
            ItemEntity pizzaEntity = itemConverter.convertNormalToEntity(newPizza);
            ItemEntity returnedEntity = itemRepository.save(pizzaEntity);
            return itemConverter.convertEntityToNormal(returnedEntity);
        }
        else if(itemRequest instanceof CreateAppetizerRequest appetizerRequest) {
            Appetizer newAppetizer = Appetizer.builder()
                    .itemName(appetizerRequest.getItemName())
                    .itemImageVersion(initialImageVersionNaming)
                    .itemPrice(appetizerRequest.getItemPrice())
                    .ingredients(appetizerRequest.getIngredients())
                    .visibleInMenu(appetizerRequest.getVisibleInMenu())
                    .isVegetarian(appetizerRequest.getIsVegetarian())
                    .itemCategory(findCategory("APPETIZER"))
                    .build();

            ItemEntity appetizerEntity = itemConverter.convertNormalToEntity(newAppetizer);
            ItemEntity returnedEntity = itemRepository.save(appetizerEntity);
            return itemConverter.convertEntityToNormal(returnedEntity);
        }
        else if(itemRequest instanceof CreateBeverageRequest beverageRequest) {
            Beverage newBeverage = Beverage.builder()
                    .itemName(beverageRequest.getItemName())
                    .itemImageVersion(initialImageVersionNaming)
                    .itemPrice(beverageRequest.getItemPrice())
                    .ingredients(beverageRequest.getIngredients())
                    .visibleInMenu(beverageRequest.getVisibleInMenu())
                    .size(beverageRequest.getSize())
                    .itemCategory(findCategory("BEVERAGE"))
                    .build();

            ItemEntity beverageEntity = itemConverter.convertNormalToEntity(newBeverage);
            ItemEntity returnedEntity = itemRepository.save(beverageEntity);
            return itemConverter.convertEntityToNormal(returnedEntity);
        }
        else if(itemRequest instanceof CreateBurgerRequest burgerRequest) {
            Burger newBurger = Burger.builder()
                    .itemName(burgerRequest.getItemName())
                    .itemImageVersion(initialImageVersionNaming)
                    .itemPrice(burgerRequest.getItemPrice())
                    .ingredients(burgerRequest.getIngredients())
                    .visibleInMenu(burgerRequest.getVisibleInMenu())
                    .weight(burgerRequest.getWeight())
                    .itemCategory(findCategory("BURGER"))
                    .build();

            ItemEntity burgerEntity = itemConverter.convertNormalToEntity(newBurger);
            ItemEntity returnedEntity = itemRepository.save(burgerEntity);
            return itemConverter.convertEntityToNormal(returnedEntity);
        }
        else if(itemRequest instanceof CreateGrillRequest grillRequest) {
            Grill newGrill = Grill.builder()
                    .itemName(grillRequest.getItemName())
                    .itemImageVersion(initialImageVersionNaming)
                    .itemPrice(grillRequest.getItemPrice())
                    .ingredients(grillRequest.getIngredients())
                    .visibleInMenu(grillRequest.getVisibleInMenu())
                    .weight(grillRequest.getWeight())
                    .itemCategory(findCategory("GRILL"))
                    .build();

            ItemEntity grillEntity = itemConverter.convertNormalToEntity(newGrill);
            ItemEntity returnedEntity = itemRepository.save(grillEntity);
            return itemConverter.convertEntityToNormal(returnedEntity);
        }
        else if(itemRequest instanceof CreatePastaRequest pastaRequest) {
            Pasta newPasta = Pasta.builder()
                    .itemName(pastaRequest.getItemName())
                    .itemImageVersion(initialImageVersionNaming)
                    .itemPrice(pastaRequest.getItemPrice())
                    .ingredients(pastaRequest.getIngredients())
                    .visibleInMenu(pastaRequest.getVisibleInMenu())
                    .pastaType(pastaRequest.getPastaType())
                    .weight(pastaRequest.getWeight())
                    .itemCategory(findCategory("PASTA"))
                    .build();

            ItemEntity pastaEntity = itemConverter.convertNormalToEntity(newPasta);
            ItemEntity returnedEntity = itemRepository.save(pastaEntity);
            return itemConverter.convertEntityToNormal(returnedEntity);
        }
        else if(itemRequest instanceof CreateSaladRequest saladRequest) {
            Salad newSalad = Salad.builder()
                    .itemName(saladRequest.getItemName())
                    .itemImageVersion(initialImageVersionNaming)
                    .itemPrice(saladRequest.getItemPrice())
                    .ingredients(saladRequest.getIngredients())
                    .visibleInMenu(saladRequest.getVisibleInMenu())
                    .weight(saladRequest.getWeight())
                    .itemCategory(findCategory("SALAD"))
                    .build();

            ItemEntity saladEntity = itemConverter.convertNormalToEntity(newSalad);
            ItemEntity returnedEntity = itemRepository.save(saladEntity);
            return itemConverter.convertEntityToNormal(returnedEntity);
        }
        else if(itemRequest instanceof CreateSoupRequest soupRequest) {
            Soup newSoup = Soup.builder()
                    .itemName(soupRequest.getItemName())
                    .itemImageVersion(initialImageVersionNaming)
                    .itemPrice(soupRequest.getItemPrice())
                    .ingredients(soupRequest.getIngredients())
                    .visibleInMenu(soupRequest.getVisibleInMenu())
                    .isVegetarian(soupRequest.getIsVegetarian())
                    .itemCategory(findCategory("SOUP"))
                    .build();

            ItemEntity soupEntity = itemConverter.convertNormalToEntity(newSoup);
            ItemEntity returnedEntity = itemRepository.save(soupEntity);
            return itemConverter.convertEntityToNormal(returnedEntity);
        }
        throw new NotFound("The passed category does not exist");

    }
}
