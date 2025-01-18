package nl.fontys.s3.dinemasterbackend.business.item_use_cases.implementations;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import nl.fontys.s3.dinemasterbackend.business.converters.ItemConverter;
import nl.fontys.s3.dinemasterbackend.business.dtos.update.items.*;
import nl.fontys.s3.dinemasterbackend.business.exceptions.NotFound;
import nl.fontys.s3.dinemasterbackend.business.item_use_cases.UpdateItem;
import nl.fontys.s3.dinemasterbackend.domain.classes.*;
import nl.fontys.s3.dinemasterbackend.persistence.entity.ItemEntity;
import nl.fontys.s3.dinemasterbackend.persistence.repositories.ItemEntityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateItemImpl implements UpdateItem {
    private final ItemEntityRepository itemEntityRepository;
    private final ItemConverter itemConverter;
    private static final String ITEM_NOT_FOUND_MESSAGE = "Item not found";

    private Item checkExistingItem(UpdateItemRequest request) {
        Optional<ItemEntity> itemEntity = itemEntityRepository.findById(request.getItemId());
        if (itemEntity.isEmpty()) {
            throw new NotFound("Item with ID " + request.getItemId() + " not found.");
        }
        return itemConverter.convertEntityToNormal(itemEntity.get());
    }

    public Item updateCommonFields(UpdateItemRequest request, Item item) {
        item.setItemId(request.getItemId());
        item.setItemName(request.getItemName());
        item.setItemImageVersion(request.getItemImageVersion());
        item.setItemPrice(request.getItemPrice());
        item.setIngredients(request.getIngredients());
        item.setVisibleInMenu(request.getVisibleInMenu());
        return item;
    }

    @Override
    @Transactional
    public void updateItem(UpdatePizzaRequest request) {
        Item foundItem = checkExistingItem(request);
        Item itemWithUpdatedCommonFields = updateCommonFields(request, foundItem);
        if (itemWithUpdatedCommonFields instanceof Pizza pizza) {
            pizza.setBase(request.getBase());
            pizza.setSizes(request.getSizes());
            ItemEntity pizzaEntity = itemConverter.convertNormalToEntity(pizza);
            itemEntityRepository.save(pizzaEntity);
        } else {
            throw new NotFound(ITEM_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    @Transactional
    public void updateItem(UpdatePastaRequest request) {
        Item foundItem = checkExistingItem(request);
        Item itemWithUpdatedCommonFields = updateCommonFields(request, foundItem);
        if (itemWithUpdatedCommonFields instanceof Pasta pasta) {
            pasta.setPastaType(request.getPastaType());
            pasta.setWeight(request.getWeight());
            ItemEntity pastaEntity = itemConverter.convertNormalToEntity(pasta);
            itemEntityRepository.save(pastaEntity);
        } else {
            throw new NotFound(ITEM_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    @Transactional
    public void updateItem(UpdateAppetizerRequest request) {
        Item foundItem = checkExistingItem(request);
        Item itemWithUpdatedCommonFields = updateCommonFields(request, foundItem);
        if (itemWithUpdatedCommonFields instanceof Appetizer appetizer) {
            appetizer.setIsVegetarian(request.getIsVegetarian());
            ItemEntity appetizerEntity = itemConverter.convertNormalToEntity(appetizer);
            itemEntityRepository.save(appetizerEntity);
        } else {
            throw new NotFound(ITEM_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    @Transactional
    public void updateItem(UpdateBeverageRequest request) {
        Item foundItem = checkExistingItem(request);
        Item itemWithUpdatedCommonFields = updateCommonFields(request, foundItem);
        if (itemWithUpdatedCommonFields instanceof Beverage beverage) {
            beverage.setSize(request.getSize());
            ItemEntity beverageEntity = itemConverter.convertNormalToEntity(beverage);
            itemEntityRepository.save(beverageEntity);
        } else {
            throw new NotFound(ITEM_NOT_FOUND_MESSAGE);
        }
    }


    @Override
    @Transactional
    public void updateItem(UpdateBurgerRequest request) {
        Item foundItem = checkExistingItem(request);
        Item itemWithUpdatedCommonFields = updateCommonFields(request, foundItem);
        if (itemWithUpdatedCommonFields instanceof Burger burger) {
            burger.setWeight(request.getWeight());
            ItemEntity burgerEntity = itemConverter.convertNormalToEntity(burger);
            itemEntityRepository.save(burgerEntity);
        } else {
            throw new NotFound(ITEM_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    @Transactional
    public void updateItem(UpdateGrillRequest request) {
        Item foundItem = checkExistingItem(request);
        Item itemWithUpdatedCommonFields = updateCommonFields(request, foundItem);
        if (itemWithUpdatedCommonFields instanceof Grill grill) {
            grill.setWeight(request.getWeight());
            ItemEntity grillEntity = itemConverter.convertNormalToEntity(grill);
            itemEntityRepository.save(grillEntity);
        } else {
            throw new NotFound(ITEM_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    @Transactional
    public void updateItem(UpdateSaladRequest request) {
        Item foundItem = checkExistingItem(request);
        Item itemWithUpdatedCommonFields = updateCommonFields(request, foundItem);
        if (itemWithUpdatedCommonFields instanceof Salad salad) {
            salad.setWeight(request.getWeight());
            ItemEntity saladEntity = itemConverter.convertNormalToEntity(salad);
            itemEntityRepository.save(saladEntity);
        } else {
            throw new NotFound(ITEM_NOT_FOUND_MESSAGE);
        }
    }

    @Override
    @Transactional
    public void updateItem(UpdateSoupRequest request) {
        Item foundItem = checkExistingItem(request);
        Item itemWithUpdatedCommonFields = updateCommonFields(request, foundItem);
        if (itemWithUpdatedCommonFields instanceof Soup soup) {
            soup.setIsVegetarian(request.getIsVegetarian());

            ItemEntity soupEntity = itemConverter.convertNormalToEntity(soup);
            itemEntityRepository.save(soupEntity);
        } else {
            throw new NotFound(ITEM_NOT_FOUND_MESSAGE);
        }
    }
}
